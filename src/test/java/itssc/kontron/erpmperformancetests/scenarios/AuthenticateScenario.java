package itssc.kontron.erpmperformancetests.scenarios;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import itssc.kontron.erpmperformancetests.utils.Constants;
import itssc.kontron.erpmperformancetests.utils.GatlingProps;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AuthenticateScenario {

  private final GatlingProps.Keycloak props = new GatlingProps().keycloak();

  private final Map<String, String> keycloakHeader = Map.of(
    Constants.Headers.CONTENT_TYPE, props.contentType()
  );

  private final Map<String, String> requestBody = Map.of(
    "username", props.username(),
    "password", props.password(),
    "grant_type", props.grantType(),
    "client_id", props.clientId(),
    "redirect_uri", props.redirectUrl(),
    "scope", props.scope()
  );

  private final ChainBuilder authenticate =
    exec(http("Get Access Token")
           .post(props.url())
           .body(StringBody(requestBody.toString()))
           .headers(keycloakHeader)
           .check(status().is(200))
           .check(jmesPath("access_token").saveAs("accessToken")));

  public ScenarioBuilder build() {
    return scenario("Fetch Authentication Access Token scenario (base workflow)")
      .exec(authenticate);
  }

  public ChainBuilder authenticate() {
    return this.authenticate;
  }

}
