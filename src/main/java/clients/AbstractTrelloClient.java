// src/main/java/client/AbstractTrelloClient.java
package clients;

public abstract class AbstractTrelloClient {
  protected String apiKey;
  protected String authToken;
  protected String baseUrl;

  public AbstractTrelloClient(String apiKey, String authToken, String baseUrl) {
    this.apiKey = apiKey;
    this.authToken = authToken;
    this.baseUrl = baseUrl;
  }

  protected String buildAuthParams() {
    return "key=" + apiKey + "&token=" + authToken;
  }
}