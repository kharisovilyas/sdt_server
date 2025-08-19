package ru.spiiran.sdt_server.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.spiiran.sdt_server.clients.tleSpaceTrackClient;
import ru.spiiran.sdt_server.obj.event.tle.request.*;
import ru.spiiran.sdt_server.utils.exception.tle.AuthenticationSpaceTrackException;
import ru.spiiran.sdt_server.utils.exception.tle.NotFoundSatelliteSpaceTrackException;
import ru.spiiran.sdt_server.utils.exception.tle.RequestSpaceTrackException;

@Service
public class tleUploadServiceImpl implements tleUploaderService {

    private final tleSpaceTrackClient spaceTrackClient;
    private final ApplicationEventPublisher eventPublisher;

    public tleUploadServiceImpl(tleSpaceTrackClient spaceTrackClient, ApplicationEventPublisher eventPublisher) {
        this.spaceTrackClient = spaceTrackClient;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String searchByNoradId(Long noradId) throws NotFoundSatelliteSpaceTrackException, AuthenticationSpaceTrackException, RequestSpaceTrackException {
        eventPublisher.publishEvent(new tleNoradIdRequestEvent(noradId));
        return spaceTrackClient.searchByNoradId(noradId);
    }

}