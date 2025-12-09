package ru.spiiran.sdt_server.application.dto.llm;

public class DtoFilters {
    private String number;
    private String coverage;
    private String orbitType;
    private String minAltitude;
    private String maxAltitude;
    private String mass;
    private String formFactor;
    private String status;
    private String date;
    private String scale;

    public DtoFilters() {}

    public DtoFilters(
            String number,
            String coverage,
            String orbitType,
            String minAltitude,
            String maxAltitude,
            String mass,
            String formFactor,
            String status,
            String date,
            String scale
    ) {
        this.number = number;
        this.coverage = coverage;
        this.orbitType = orbitType;
        this.minAltitude = minAltitude;
        this.maxAltitude = maxAltitude;
        this.mass = mass;
        this.formFactor = formFactor;
        this.status = status;
        this.date = date;
        this.scale = scale;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getOrbitType() {
        return orbitType;
    }

    public void setOrbitType(String orbitType) {
        this.orbitType = orbitType;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getMinAltitude() {
        return minAltitude;
    }

    public void setMinAltitude(String minAltitude) {
        this.minAltitude = minAltitude;
    }

    public String getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(String maxAltitude) {
        this.maxAltitude = maxAltitude;
    }
}
