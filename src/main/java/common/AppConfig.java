package common;

/**
 * Class to hold application configuration details.
 */

public class AppConfig {
  private String ApiKey;
  private String AuthToken;
  private String BaseUrl;

  public String getApiKey() {
    return ApiKey;
  }

  public void setApiKey(String apiKey) {
    ApiKey = apiKey;
  }

  public String getAuthToken() {
    return AuthToken;
  }

  public void setAuthToken(String authToken) {
    AuthToken = authToken;
  }

  public String getBaseUrl() {
    return BaseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    BaseUrl = baseUrl;
  }

}
