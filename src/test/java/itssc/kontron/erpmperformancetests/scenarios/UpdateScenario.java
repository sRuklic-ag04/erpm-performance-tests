package itssc.kontron.erpmperformancetests.scenarios;

import camundajar.impl.scala.collection.immutable.List;
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

  private final GatlingProps.Scenario props = new GatlingProps().update();

  private final Map<String, String> defaultHeaders = Map.of(
    props.authHeaderName(), "Bearer #{accessToken}",
    Constants.Headers.CONTENT_TYPE, "#{contentType}"
  );

  private final FeederBuilder.Batchable<String> feeder = csv(props.feedPath()).random();

  private final ConcurrentHashMap<String, String> idMap = new ConcurrentHashMap<>();

  private final AuthenticateScenario authenticate = new AuthenticateScenario();

  private final ChainBuilder updateEntities =
    exec(session -> session.set(Constants.ScenarioVars.USER_ID, session.userId()))
      .repeat(props.repeat(), Constants.ScenarioVars.REPEAT_COUNTER).on(
        exec(session -> session.set(Constants.ScenarioVars.REPEAT_UUID, UUID.randomUUID().toString()))
          .feed(feeder).exec(
            session -> {
              String id = session.getList(Constants.ScenarioVars.CAMUNDA_PROCESSES).get(0).toString();
              session.getList(Constants.ScenarioVars.CAMUNDA_PROCESSES).remove(0); // TODO will need in delete, if run
              // TODO Thread safe list
              updateEntity(ElFileBody("#{documentPath}"), id, "#{entityName}", "#{endpointPath}");
              return session;
            }
          )
      );

  public ScenarioBuilder build() {
    return scenario("Update entities scenario (base workflow)")
      .exec(authenticate.authenticate())
      .exec(updateEntities);
  }

  private ChainBuilder updateEntity(Body body, String id, String entityName, String endpointPath) {
    return exec(http("PUT " + entityName)
                  .post(endpointPath + "/" + id)
                  .headers(defaultHeaders)
                  .body(body)
                  .check(status().is(200))
    ).pause(Duration.ofMillis(props.delay()));
  }

  public ChainBuilder updateEntities() {
    return this.updateEntities;
  }
}
