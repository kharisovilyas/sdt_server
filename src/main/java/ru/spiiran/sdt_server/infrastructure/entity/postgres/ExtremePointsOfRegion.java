package ru.spiiran.sdt_server.infrastructure.entity.postgres;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "coverage")
public class ExtremePointsOfRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @CollectionTable(
            name = "coverage_regions",
            joinColumns = @JoinColumn(name = "coverage_id")
    )
    @Column(name = "region_name")
    private List<String> region;

    /** Западная точка */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "west_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "west_lon"))
    })
    private Coordinate westPoint;

    /** Восточная точка */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "east_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "east_lon"))
    })
    private Coordinate eastPoint;

    /** Северная точка */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "north_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "north_lon"))
    })
    private Coordinate northPoint;

    /** Южная точка */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "south_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "south_lon"))
    })
    private Coordinate southPoint;

    public ExtremePointsOfRegion(Long id, List<String> region, Coordinate westPoint, Coordinate eastPoint, Coordinate northPoint, Coordinate southPoint) {
        this.id = id;
        this.region = region;
        this.westPoint = westPoint;
        this.eastPoint = eastPoint;
        this.northPoint = northPoint;
        this.southPoint = southPoint;
    }

    public ExtremePointsOfRegion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRegion() {
        return region;
    }

    public void setRegion(List<String> region) {
        this.region = region;
    }

    public Coordinate getWestPoint() {
        return westPoint;
    }

    public void setWestPoint(Coordinate westPoint) {
        this.westPoint = westPoint;
    }

    public Coordinate getEastPoint() {
        return eastPoint;
    }

    public void setEastPoint(Coordinate eastPoint) {
        this.eastPoint = eastPoint;
    }

    public Coordinate getNorthPoint() {
        return northPoint;
    }

    public void setNorthPoint(Coordinate northPoint) {
        this.northPoint = northPoint;
    }

    public Coordinate getSouthPoint() {
        return southPoint;
    }

    public void setSouthPoint(Coordinate southPoint) {
        this.southPoint = southPoint;
    }
}
