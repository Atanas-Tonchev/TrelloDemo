package common;

import io.restassured.RestAssured;

public class TrelloCardClient {
  private final String apiKey;
  private final String token;

  public TrelloCardClient(String apiKey, String token, String baseUri) {
    this.apiKey = apiKey;
    this.token = token;
    RestAssured.baseURI = baseUri;
  }
}
