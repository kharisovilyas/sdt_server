package ru.spiiran.sdt_server.application.dto.selection;

public class DtoTimeRegion {
    private Long seesRegion;
    private Long notSeeRegion;

    public DtoTimeRegion(Long seesRegion, Long notSeeRegion) {
        this.seesRegion = seesRegion;
        this.notSeeRegion = notSeeRegion;
    }

    public Long getSeesRegion() {
        return seesRegion;
    }

    public void setSeesRegion(Long seesRegion) {
        this.seesRegion = seesRegion;
    }

    public Long getNotSeeRegion() {
        return notSeeRegion;
    }

    public void setNotSeeRegion(Long notSeeRegion) {
        this.notSeeRegion = notSeeRegion;
    }
}
