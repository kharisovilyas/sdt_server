package ru.spiiran.sdt_server.api.v1.controllers;

import org.springframework.web.bind.annotation.*;
import ru.spiiran.sdt_server.obj.dto.dtoLaunchDateRequest;
import ru.spiiran.sdt_server.service.tleUploaderService;
import ru.spiiran.sdt_server.utils.exception.tle.AuthenticationSpaceTrackException;
import ru.spiiran.sdt_server.utils.exception.tle.NotFoundSatelliteSpaceTrackException;
import ru.spiiran.sdt_server.utils.exception.tle.RequestSpaceTrackException;

@RestController
@RequestMapping("/api/v1/tle/")
class RestTLESpaceTrackController {

    private final tleUploaderService tleUploaderService;

    public RestTLESpaceTrackController(tleUploaderService tleUploaderService) {
        this.tleUploaderService = tleUploaderService;
    }

    @PostMapping("noradId")
    public String searchByNoradId(@RequestBody Long tleId)
            throws AuthenticationSpaceTrackException, NotFoundSatelliteSpaceTrackException, RequestSpaceTrackException {
        return tleUploaderService.searchByNoradId(tleId);
    }

}
