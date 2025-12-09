package ru.spiiran.sdt_server.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.spiiran.sdt_server.domain.event.tle.request.TLENoradIdRequestEvent;
import ru.spiiran.sdt_server.domain.service.TLEUploaderService;
import ru.spiiran.sdt_server.infrastructure.client.tle.TLESpaceTrackClient;
import ru.spiiran.sdt_server.infrastructure.exception.tle.AuthenticationSpaceTrackException;
import ru.spiiran.sdt_server.infrastructure.exception.tle.NotFoundSatelliteSpaceTrackException;
import ru.spiiran.sdt_server.infrastructure.exception.tle.RequestSpaceTrackException;

@Service
public class TLEUploadServiceImpl implements TLEUploaderService {

    private final TLESpaceTrackClient spaceTrackClient;
    private final ApplicationEventPublisher eventPublisher;

    public TLEUploadServiceImpl(TLESpaceTrackClient spaceTrackClient, ApplicationEventPublisher eventPublisher) {
        this.spaceTrackClient = spaceTrackClient;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String searchByNoradId(Long noradId) throws NotFoundSatelliteSpaceTrackException, AuthenticationSpaceTrackException, RequestSpaceTrackException {
        eventPublisher.publishEvent(new TLENoradIdRequestEvent(this, noradId));
        return spaceTrackClient.searchByNoradId(noradId);
    }

}