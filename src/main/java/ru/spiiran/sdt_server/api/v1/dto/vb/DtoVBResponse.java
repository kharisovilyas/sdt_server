package ru.spiiran.sdt_server.api.v1.dto.vb;

public class DtoVBResponse implements IDtoProgress {
    private Long satelliteId;
    private String tle;
    private DtoModelSat modelSat;

    public DtoVBResponse(Long satelliteId, String tle, DtoModelSat modelSat) {
        this.satelliteId = satelliteId;
        this.tle = tle;
        this.modelSat = modelSat;
    }

    public Long getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(Long satelliteId) {
        this.satelliteId = satelliteId;
    }

    public String getTle() {
        return tle;
    }

    public void setTle(String tle) {
        this.tle = tle;
    }

    public DtoModelSat getModelSat() {
        return modelSat;
    }

    public void setModelSat(DtoModelSat modelSat) {
        this.modelSat = modelSat;
    }
}
