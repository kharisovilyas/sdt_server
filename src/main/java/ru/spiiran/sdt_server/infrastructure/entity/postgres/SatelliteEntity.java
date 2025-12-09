package ru.spiiran.sdt_server.infrastructure.entity.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "satellite")
public class SatelliteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "orbit_type")
    private String orbitType;
    @Column(name = "altitude")
    private Double altitude;
    @Column(name = "mass")
    private Double mass;
    @Column(name = "form_factor")
    private String formFactor;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "launch_date")
    private Long date;
    @Column(name = "tle")
    private String tle;
    @Column(name = "norad_id")
    private Long noradId;

    public SatelliteEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTle() {
        return tle;
    }

    public void setTle(String tle) {
        this.tle = tle;
    }

    public Long getNoradId() {
        return noradId;
    }

    public void setNoradId(Long noradId) {
        this.noradId = noradId;
    }
}
