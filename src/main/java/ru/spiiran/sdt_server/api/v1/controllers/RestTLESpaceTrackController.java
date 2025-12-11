package ru.spiiran.sdt_server.api.v1.controllers;

import org.springframework.web.bind.annotation.*;
import ru.spiiran.sdt_server.domain.service.TLEUploaderService;
import ru.spiiran.sdt_server.infrastructure.exception.tle.AuthenticationSpaceTrackException;
import ru.spiiran.sdt_server.infrastructure.exception.tle.NotFoundSatelliteSpaceTrackException;
import ru.spiiran.sdt_server.infrastructure.exception.tle.RequestSpaceTrackException;

@RestController
@RequestMapping("/api/v1/tle/")
class RestTLESpaceTrackController {

}
