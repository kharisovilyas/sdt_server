package ru.spiiran.sdt_server.application.service.vb;

import org.springframework.stereotype.Service;
import ru.spiiran.sdt_server.application.dto.selection.DtoSelectionResponse;
import ru.spiiran.sdt_server.application.dto.selection.DtoTimeRegion;
import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Coordinate;
import ru.spiiran.sdt_server.infrastructure.dto.DtoPro42Response;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.llm.DtoLLMFilterResponse;
import ru.spiiran.sdt_server.infrastructure.exception.mapper.ExtremePointsMappingException;
import ru.spiiran.sdt_server.infrastructure.pro42.Pro42Service;
import ru.spiiran.sdt_server.domain.service.VBSelectionRegionService;
import ru.spiiran.sdt_server.infrastructure.database.DatabaseAccess;
import ru.spiiran.sdt_server.infrastructure.dto.DtoExtremePointsOfRegion;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VBSelectionRegionServiceImpl implements VBSelectionRegionService {
    private final Pro42Service pro42Service;
    private final DatabaseAccess databaseAccess;

    public VBSelectionRegionServiceImpl(Pro42Service pro42Service, DatabaseAccess databaseAccess) {
        this.pro42Service = pro42Service;
        this.databaseAccess = databaseAccess;
    }

    @Override
    public List<DtoSelectionResponse> selectionByTargetRegion(DtoLLMFilterResponse filterResponse,  List<DtoVBResponse> satellites) throws ExtremePointsMappingException {
        // Получаем целевой регион наблюдения
        String coverage = filterResponse.getFilters().getCoverage();
        // Получаем с pro42 результаты ИМ для каждого КА
        List<DtoPro42Response> pro42Responses = pro42Service.pro42(satellites);
        // Получаем с БД информацию о целевом регионе
        DtoExtremePointsOfRegion extremePointsOfRegion = databaseAccess.extremePointsOfRegion(coverage);
        // Формируем ответ с учетом информации о целевом регионе
        return formingResponses(
                satellites, /* Информация о всех КА-кандидатах (после атрибутной выборки)*/
                pro42Responses, /* Все траектории движения КА-кандидатов*/
                extremePointsOfRegion /* Условия при которых мы отбираем точки траектории по принадлежности региону*/
        ); // Должен сформировать из satellites -> (correct satellites + timeRegion) [DtoSelectionResponse]
    }

    private List<DtoSelectionResponse> formingResponses(
            List<DtoVBResponse> satellites,
            List<DtoPro42Response> pro42Responses,
            DtoExtremePointsOfRegion region
    ) {
        Map<Long, DtoPro42Response> pro42Map = pro42Responses.stream()
                .collect(Collectors.toMap(DtoPro42Response::getSatelliteId, Function.identity()));

        return satellites.stream()
                .map(sat -> formingSelectionResponse(sat, pro42Map.get(sat.getSatelliteId()), region))
                .collect(Collectors.toList());
    }

    private DtoSelectionResponse formingSelectionResponse(
            DtoVBResponse sat,
            DtoPro42Response response,
            DtoExtremePointsOfRegion region
    ) {
        DtoTimeRegion timeRegion = defineTimeForRegion(response, region);
        return new DtoSelectionResponse(timeRegion, sat);
    }

    private DtoTimeRegion defineTimeForRegion(DtoPro42Response pro42response, DtoExtremePointsOfRegion region) {
        List<DtoPro42Coordinate> coordinates = pro42response.getCoordinates();
        Long numSeesRegion = defineNumSeesRegion(coordinates, region);
        Long numNotSeesRegion = coordinates.size() - numSeesRegion;
        return new DtoTimeRegion(numSeesRegion, numNotSeesRegion);
    }

    private Long defineNumSeesRegion(List<DtoPro42Coordinate> coordinates, DtoExtremePointsOfRegion region) {
        return coordinates.stream().filter(coordinate -> isInsideRegion(coordinate, region)).count();
    }

    private boolean isInsideRegion(DtoPro42Coordinate coordinate, DtoExtremePointsOfRegion region) {
        double lat = coordinate.getLatitude();
        double lon = coordinate.getLongitude();

        return lat >= region.getSouth() && lat <= region.getNorth()
                && lon >= region.getWest()  && lon <= region.getEast();
    }

}
