package itssc.kontron.erpmperformancetests.scenarios;

import io.gatling.javaapi.core.*;
import itssc.kontron.erpmperformancetests.utils.Constants;
import itssc.kontron.erpmperformancetests.utils.GatlingProps;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CreateScenario {

  private final GatlingProps.Scenario props = new GatlingProps().create();

  private final Map<String, String> defaultHeaders = Map.of(
    props.authHeaderName(), "Bearer #{accessToken}",
    Constants.Headers.CONTENT_TYPE, "#{contentType}"
  );

  private final FeederBuilder.Batchable<String> feeder = csv(props.feedPath()).random();

  private final AuthenticateScenario authenticate = new AuthenticateScenario();

  private final ChainBuilder createEntities =
    exec(session -> session.set(Constants.ScenarioVars.USER_ID, session.userId()))
      .exec(session -> session.set(Constants.ScenarioVars.CAMUNDA_PROCESSES, new LinkedList<>()))
      .repeat(props.repeat(), Constants.ScenarioVars.REPEAT_COUNTER).on(
        exec(session -> session.set(Constants.ScenarioVars.REPEAT_UUID, UUID.randomUUID().toString()))
          .feed(feeder).exec(
            createEntity(ElFileBody("#{documentPath}"), "#{entityName}", "#{endpointPath}")
          )
      );

  public ScenarioBuilder build() {
    return scenario("Create entities scenario (base workflow)")
      .exec(authenticate.authenticate())
      .exec(createEntities);
  }

  private ChainBuilder createEntity(Body body, String entityName, String endpointPath) {
    return exec(http("POST " + entityName)
                  .post(endpointPath)
                  .headers(defaultHeaders)
                  .body(body)
                  .check(status().is(200))
                  .check(jmesPath("process_id").saveAs("process_id"))
    ).exec(session -> {
        session.getSet(Constants.ScenarioVars.CAMUNDA_PROCESSES).add("#{process_id}");
       return session;
     })
     .pause(Duration.ofMillis(props.delay()));
  }

  public ChainBuilder createEntities() {
    return this.createEntities;
  }

}
