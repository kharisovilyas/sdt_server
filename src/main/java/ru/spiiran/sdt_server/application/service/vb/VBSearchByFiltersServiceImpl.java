package ru.spiiran.sdt_server.application.service.vb;

import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.llm.DtoFilters;
import ru.spiiran.sdt_server.domain.service.VBSearchByFiltersService;
import ru.spiiran.sdt_server.infrastructure.entity.postgres.SatelliteEntity;
import ru.spiiran.sdt_server.infrastructure.mapper.SatelliteMapper;
import ru.spiiran.sdt_server.infrastructure.repository.postgres.SatelliteRepository;
import ru.spiiran.sdt_server.util.time.TimeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class VBSearchByFiltersServiceImpl implements VBSearchByFiltersService {

    private static final Logger logger = LoggerFactory.getLogger(VBSearchByFiltersServiceImpl.class);
    private static final double MASS_TOLERANCE = 0.1; // ±10% допуск по массе

    private final SatelliteRepository satelliteRepository;
    private final SatelliteMapper satelliteMapper;

    public VBSearchByFiltersServiceImpl(SatelliteRepository satelliteRepository,
                                        SatelliteMapper satelliteMapper) {
        this.satelliteRepository = satelliteRepository;
        this.satelliteMapper = satelliteMapper;
    }

    @Override
    public List<DtoVBResponse> searchByFilters(DtoFilters filters) {
        List<Specification<SatelliteEntity>> specifications = new ArrayList<>();

        addEqualFilter(specifications, "orbitType", filters.getOrbitType());

        // Обработка форм-фактора с учетом CUBESAT
        addFormFactorFilter(specifications, filters.getFormFactor());

        addDoubleRangeFilter(specifications, "altitude",
                parseDoubleSafe(filters.getMinAltitude()),
                parseDoubleSafe(filters.getMaxAltitude()));

        // Обработка массы с допуском ±10%
        addMassWithToleranceFilter(specifications, parseDoubleSafe(filters.getMass()));

        addEqualFilter(specifications, "status", parseBooleanSafe(filters.getStatus()));
        addEqualFilter(specifications, "date", parseUnixLongSafe(filters.getDate()));

        Specification<SatelliteEntity> finalSpec = specifications.stream()
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());

        List<SatelliteEntity> entities = satelliteRepository.findAll(finalSpec);
        return satelliteMapper.toDtoLLMResponseList(entities);
    }

    /**
     * Фильтр для форм-фактора с учетом CUBESAT
     * Если указан форм-фактор, проверяем, содержит ли поле formFactor значение "CUBESAT"
     */
    private void addFormFactorFilter(List<Specification<SatelliteEntity>> specifications, String formFactor) {
        if (formFactor != null && !formFactor.trim().isEmpty()) {
            String formFactorValue = formFactor.trim().toUpperCase();
            specifications.add((root, query, cb) -> {
                if ("CUBESAT".equals(formFactorValue)) {
                    // Ищем записи, где форм-фактор содержит "CUBESAT"
                    return cb.like(cb.upper(root.get("formFactor")), "%CUBESAT%");
                } else {
                    // Для других форм-факторов точное сравнение
                    return cb.equal(cb.upper(root.get("formFactor")), formFactorValue);
                }
            });
        }
    }

    /**
     * Фильтр для массы с допуском ±10%
     */
    private void addMassWithToleranceFilter(List<Specification<SatelliteEntity>> specifications, Double mass) {
        if (mass != null && mass > 0) {
            double tolerance = mass * MASS_TOLERANCE;
            double minMass = mass - tolerance;
            double maxMass = mass + tolerance;

            specifications.add((root, query, cb) ->
                    cb.between(root.get("mass"), minMass, maxMass)
            );
            logger.debug("Searching for mass in range: {} ± {} kg ({} - {})",
                    mass, tolerance, minMass, maxMass);
        }
    }

    private void addEqualFilter(List<Specification<SatelliteEntity>> specifications,
                                String fieldName, Object value) {
        if (value instanceof String) {
            if (!((String) value).isEmpty()) {
                specifications.add((root, query, cb) -> cb.equal(root.get(fieldName), value));
            }
        } else {
            if (value != null) {
                specifications.add((root, query, cb) -> cb.equal(root.get(fieldName), value));
            }
        }
    }

    private void addDoubleRangeFilter(List<Specification<SatelliteEntity>> specifications,
                                      String fieldName, Double min, Double max) {
        if (min != null || max != null) {
            specifications.add((root, query, cb) -> {
                Predicate predicate = cb.conjunction();
                if (min != null) {
                    predicate = cb.and(predicate, cb.ge(root.get(fieldName), min));
                }
                if (max != null) {
                    predicate = cb.and(predicate, cb.le(root.get(fieldName), max));
                }
                return predicate;
            });
        }
    }

    private Double parseDoubleSafe(String value) {
        return parseSafe(value, Double::parseDouble);
    }

    private Long parseUnixLongSafe(String value) {
        return parseSafe(value, TimeConverter::timeUdtToUnix);
    }

    private Long parseLongSafe(String value) {
        return parseSafe(value, Long::parseLong);
    }

    private Boolean parseBooleanSafe(String value) {
        return parseSafe(value, Boolean::parseBoolean);
    }

    private <T> T parseSafe(String value, Function<String, T> parser) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return parser.apply(value.trim());
        } catch (Exception e) {
            logger.warn("Failed to parse value: '{}'", value, e);
            return null;
        }
    }
}