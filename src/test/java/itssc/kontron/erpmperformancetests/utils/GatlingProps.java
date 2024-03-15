package itssc.kontron.erpmperformancetests.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class GatlingProps {

  private final String baseUrl;
  private final Scenario preload;
  private final Scenario create;
  private final Scenario update;
  private final Scenario delete;
  private final Keycloak keycloak;

  public GatlingProps() {
    Config config = ConfigFactory.load();

    this.keycloak = new Keycloak(config);
    this.baseUrl = config.getString("base-url");
    this.preload = new Scenario("preload", config);
    this.create = new Scenario("create", config);
    this.update = new Scenario("update", config);
    this.delete = new Scenario("delete", config);
  }

  public static class Keycloak {
    private final String url;
    private final String host;
    private final String username;
    private final String password;
    private final String clientId;
    private final String grantType;
    private final String redirectUrl;
    private final String scope;
    private final String realm;
    private final String contentType;

    public Keycloak(Config config) {
      final String prefix = "keycloak.";

      this.url = config.getString(prefix + "url");
      this.host = config.getString(prefix + "host");
      this.username = config.getString(prefix + "username");
      this.password = config.getString(prefix + "password");
      this.clientId = config.getString(prefix + "client-id");
      this.grantType = config.getString(prefix + "grant-type");
      this.redirectUrl = config.getString(prefix + "redirect-url");
      this.scope = config.getString(prefix + "scope");
      this.realm = config.getString(prefix + "realm");
      this.contentType = config.getString(prefix + "content-type");
    }

    public String url() {
      return this.url;
    }

    public String host() {
      return this.host;
    }

    public String username() {
      return this.username;
    }

    public String password() {
      return this.password;
    }

    public String clientId() {
      return this.clientId;
    }

    public String grantType() {
      return this.grantType;
    }

    public String redirectUrl() {
      return this.redirectUrl;
    }

    public String scope() {
      return this.scope;
    }

    public String realm() {
      return this.realm;
    }

    public String contentType() {
      return this.contentType;
    }

  }

  public static class Scenario {
    private final String feedPath;
    private final String authHeaderName;
    private final String authHeaderValue;
    private final int repeat;
    private final int delay;
    private final int users;
    private final int rampDuration;

    public Scenario(String scenario, Config config) {
      final String prefix = "scn." + scenario + ".";

      this.feedPath = config.hasPath(prefix + "feed-path") ? config.getString(prefix + "feed-path") : null;
      this.authHeaderName = config.getString(prefix + "auth-header-name");
      this.authHeaderValue = config.getString(prefix + "auth-header-value");
      this.repeat = config.getInt(prefix + "repeat");
      this.delay = config.getInt(prefix + "delay");
      this.users = config.getInt(prefix + "users");
      this.rampDuration = config.getInt(prefix + "ramp-duration");
    }

    public String feedPath() {
      return this.feedPath;
    }

    public String authHeaderName() {
      return this.authHeaderName;
    }

    public String authHeaderValue() {
      return this.authHeaderValue;
    }

    public int repeat() {
      return this.repeat;
    }

    public int delay() {
      return this.delay;
    }

    public int users() {
      return this.users;
    }

    public int rampDuration() {
      return this.rampDuration;
    }
  }

  public Keycloak keycloak() {
    return this.keycloak;
  }

  public String baseUrl() {
    return this.baseUrl;
  }

  public Scenario preload() {
    return this.preload;
  }

  public Scenario create() {
    return this.create;
  }

  public Scenario update() {
    return this.update;
  }

  public Scenario delete() {
    return this.delete;
  }

}
