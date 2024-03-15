package itssc.kontron.erpmperformancetests.scenarios;

import io.gatling.javaapi.core.Body;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import itssc.kontron.erpmperformancetests.utils.Constants;
import itssc.kontron.erpmperformancetests.utils.GatlingProps;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class UpdateScenario {

  private final GatlingProps.Scenario props = new GatlingProps().editBase();

  private final Map<String, String> defaultHeaders = Map.of(
    props.authHeaderName(), props.authHeaderValue(),
    Constants.Headers.CONTENT_TYPE, "#{contentType}"
  );

  private final FeederBuilder.Batchable<String> feeder = csv(props.feedPath()).random();

  private final ConcurrentHashMap<String, String> idMap = new ConcurrentHashMap<>();

  private final ChainBuilder updateEntities =
    exec(session -> session.set(Constants.ScenarioVars.USER_ID, session.userId()))
      .repeat(props.repeat(), Constants.ScenarioVars.REPEAT_COUNTER).on(
        exec(session -> session.set(Constants.ScenarioVars.REPEAT_UUID, UUID.randomUUID().toString()))
          .feed(feeder).exec(
            updateEntity(ElFileBody("#{documentPath}"), "#{entityName}", "#{endpointPath}")
          )
      );

  public ScenarioBuilder build() {
    return scenario("Update entities scenario (base workflow)")
      .exec(updateEntities);
  }

  private ChainBuilder updateEntity(Body body, String entityName, String endpointPath) {
    return exec(http("PUT " + entityName)
                  .post(endpointPath)
                  .headers(defaultHeaders)
                  .body(body)
                  .check(status().is(200))
    ).pause(Duration.ofMillis(props.delay()));
  }
}
