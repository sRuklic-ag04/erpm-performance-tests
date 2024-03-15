package itssc.kontron.erpmperformancetests.service.impl;

import itssc.kontron.erpmperformancetests.service.KeycloakService;
import itssc.kontron.erpmperformancetests.service.dto.AccessTokenResponseDto;
import itssc.kontron.erpmperformancetests.utils.Constants;
import itssc.kontron.erpmperformancetests.utils.GatlingProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

@Service
public class KeycloakServiceImpl implements KeycloakService {

  private final GatlingProps.Keycloak props = new GatlingProps().keycloak();
  private final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>(
    Map.of(
      "username", List.of(props.username()),
      "password", List.of(props.password()),
      "grant_type", List.of(props.grantType()),
      "client_id", List.of(props.clientId()),
      "redirect_uri", List.of(props.redirectUrl()),
      "scope", List.of(props.scope())
    )
  );

  private final HttpHeaders headers = new HttpHeaders(
    new LinkedMultiValueMap<>(
      Map.of(
        Constants.Headers.CONTENT_TYPE, List.of(props.contentType())
      )
    )
  );

  @Autowired
  private RestTemplate restTemplate;

  private String requestAccessToken() {
    try {
      return restTemplate.exchange(props.url(), HttpMethod.POST, new HttpEntity<>(requestBody, headers),
                                   AccessTokenResponseDto.class)
                         .getBody().getAccessToken();
    } catch (Exception e) {
      throw new RuntimeException("Failed fetching Keycloak access token");
    }
  }

  @Override
  public String getAccessToken() {
    return requestAccessToken();
  }

}
