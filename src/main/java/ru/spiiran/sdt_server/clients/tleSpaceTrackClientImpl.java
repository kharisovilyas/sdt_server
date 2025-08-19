package ru.spiiran.sdt_server.clients;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.spiiran.sdt_server.utils.exception.tle.AuthenticationSpaceTrackException;
import ru.spiiran.sdt_server.utils.exception.tle.NotFoundSatelliteSpaceTrackException;
import ru.spiiran.sdt_server.utils.exception.tle.RequestSpaceTrackException;
import ru.spiiran.sdt_server.utils.properties.SpaceTrackProperties;

@Component
public class tleSpaceTrackClientImpl implements tleSpaceTrackClient {

    private static final String LOGIN_URL = "https://www.space-track.org/ajaxauth/login";
    private static final String TLE_URL_TEMPLATE = "https://www.space-track.org/basicspacedata/query/class/tle_latest/NORAD_CAT_ID/%d/format/tle";

    private final RestTemplate restTemplate;
    private final SpaceTrackProperties properties;

    private String sessionCookie;

    public tleSpaceTrackClientImpl(RestTemplate restTemplate, SpaceTrackProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    private void login() throws AuthenticationSpaceTrackException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("identity", properties.getUsername());
        body.add("password", properties.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URL, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationSpaceTrackException("Failed to login to Space-Track: " + response.getStatusCode());
        }

        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (cookie == null) {
            throw new AuthenticationSpaceTrackException("No session cookie received from Space-Track");
        }
        sessionCookie = cookie;
    }

    @Override
    public String searchByNoradId(Long noradId) throws AuthenticationSpaceTrackException, NotFoundSatelliteSpaceTrackException, RequestSpaceTrackException {
        if (sessionCookie == null) {
            login();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionCookie);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        String url = String.format(TLE_URL_TEMPLATE, noradId);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            // Сессия могла устареть — повторяем логин и запрос
            login();
            headers.set(HttpHeaders.COOKIE, sessionCookie);
            ResponseEntity<String> retryResponse = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            if (retryResponse.getStatusCode() != HttpStatus.OK) {
                throw new RequestSpaceTrackException("Failed to fetch TLE after re-login: " + retryResponse.getStatusCode());
            }
            return retryResponse.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new NotFoundSatelliteSpaceTrackException("TLE not found for NORAD ID: " + noradId);
        } else if (response.getStatusCode() != HttpStatus.OK) {
            throw new RequestSpaceTrackException("Failed to fetch TLE: " + response.getStatusCode());
        }

        return response.getBody();
    }
}
