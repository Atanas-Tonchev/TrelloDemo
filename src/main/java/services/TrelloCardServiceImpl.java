package services;

import clients.TrelloCardClient;
import io.restassured.response.Response;

public class TrelloCardServiceImpl implements ITrelloCardService {
  private final TrelloCardClient client;

  public TrelloCardServiceImpl(String apiKey, String authToken, String baseUrl) {
    this.client = new TrelloCardClient(apiKey, authToken, baseUrl);
  }

  @Override
  public Response createCard(String listId, String name) {
    return client.createCard(listId, name);
  }

  @Override
  public Response moveCardToList(String cardId, String listId) {
    return client.moveCardToList(cardId, listId);
  }

  @Override
  public Response getCard(String cardId) {
    return client.getCard(cardId);
  }
}
