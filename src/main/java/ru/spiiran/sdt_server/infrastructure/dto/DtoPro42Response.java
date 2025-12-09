package ru.spiiran.sdt_server.infrastructure.dto;

import java.util.List;

public class DtoPro42Response {
    private List<DtoPro42Coordinate> coordinates;
    private Long satelliteId;

    public DtoPro42Response(List<DtoPro42Coordinate> coordinates, Long satelliteId) {
        this.coordinates = coordinates;
        this.satelliteId = satelliteId;
    }

    public List<DtoPro42Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<DtoPro42Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public Long getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(Long satelliteId) {
        this.satelliteId = satelliteId;
    }
}
