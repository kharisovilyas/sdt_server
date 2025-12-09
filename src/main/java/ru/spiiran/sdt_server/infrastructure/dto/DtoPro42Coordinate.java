package ru.spiiran.sdt_server.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DtoPro42Coordinate {
    @JsonIgnore
    private Long timePoint;
    private Double longitude;
    private Double latitude;

    public DtoPro42Coordinate(Long timePoint, Double latitude, Double longitude) {
        this.timePoint = timePoint;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Long timePoint) {
        this.timePoint = timePoint;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
