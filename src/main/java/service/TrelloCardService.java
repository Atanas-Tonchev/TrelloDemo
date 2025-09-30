package service;

import common.TrelloBoardClient;
import common.TrelloCardClient;
import io.restassured.RestAssured;

public class TrelloCardService {
  private final TrelloCardClient cardClient;

  public TrelloCardService(String apiKey, String token, String baseUri) {
    this.cardClient = new TrelloCardClient(apiKey, token, baseUri);
  }
}
