package itssc.kontron.erpmperformancetests.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import itssc.kontron.erpmperformancetests.scenarios.CreateScenario;
import itssc.kontron.erpmperformancetests.scenarios.DeleteScenario;
import itssc.kontron.erpmperformancetests.scenarios.UpdateScenario;
import itssc.kontron.erpmperformancetests.utils.GatlingProps;

import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.http.HttpDsl.http;

public class LoadTestSimulation extends Simulation {

  private final GatlingProps props = new GatlingProps();


  ScenarioBuilder createScenario = new CreateScenario().build();
  ScenarioBuilder updateScenario = new UpdateScenario().build();
  ScenarioBuilder deleteScenario = new DeleteScenario().build();

  HttpProtocolBuilder httpProtocol = http.baseUrl(props.baseUrl());

  {
    setUp(
      createScenario.injectOpen(
        rampUsers(props.readPreload().users()).during(props.readPreload().rampDuration())
      )
    ).protocols(httpProtocol);
  }
}
