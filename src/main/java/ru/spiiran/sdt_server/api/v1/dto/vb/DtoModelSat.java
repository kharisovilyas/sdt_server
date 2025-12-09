package ru.spiiran.sdt_server.api.v1.dto.vb;

import ru.spiiran.sdt_server.infrastructure.entity.postgres.SatelliteEntity;

public class DtoModelSat {
    private String orbitType;
    private Double altitude;
    private Double mass;
    private String formFactor;
    private Boolean status;
    private Long date;

    public DtoModelSat(SatelliteEntity satelliteEntity) {
        this.orbitType = satelliteEntity.getOrbitType();
        this.altitude = satelliteEntity.getAltitude();
        this.mass = satelliteEntity.getMass();
        this.formFactor = satelliteEntity.getFormFactor();
        this.status = satelliteEntity.getStatus();
        this.date = satelliteEntity.getDate();
    }

    public String getOrbitType() {
        return orbitType;
    }

    public void setOrbitType(String orbitType) {
        this.orbitType = orbitType;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
