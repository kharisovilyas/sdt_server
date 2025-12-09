package ru.spiiran.sdt_server.infrastructure.dto;

import ru.spiiran.sdt_server.infrastructure.entity.postgres.ExtremePointsOfRegion;

public class DtoExtremePointsOfRegion {
    private double west;   // западная граница (minLon)
    private double east;   // восточная граница (maxLon)
    private double north;  // северная граница (maxLat)
    private double south;  // южная граница (minLat)

    public DtoExtremePointsOfRegion(ExtremePointsOfRegion extremePointsOfRegion) {
        this.west = extremePointsOfRegion.getWestPoint().getLongitude();
        this.east = extremePointsOfRegion.getEastPoint().getLongitude();
        this.north = extremePointsOfRegion.getNorthPoint().getLatitude();
        this.south = extremePointsOfRegion.getSouthPoint().getLatitude();
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }
}
