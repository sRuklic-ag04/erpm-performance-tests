package itssc.kontron.erpmperformancetests.service.dto;

public class AccessTokenResponseDto {
  private String accessToken;
  private int expiresIn;
  private int refreshExpiresIn;
  private String refreshToken;
  private String tokenType;
  private String idToken;
  private int notBeforePolicy;
  private String sessionState;
  private String scope;

  public String getAccessToken() {
    return accessToken;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public int getRefreshExpiresIn() {
    return refreshExpiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public String getIdToken() {
    return idToken;
  }

  public int getNotBeforePolicy() {
    return notBeforePolicy;
  }

  public String getSessionState() {
    return sessionState;
  }

  public String getScope() {
    return scope;
  }

}
