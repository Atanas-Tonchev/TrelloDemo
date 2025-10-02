package services;

import clients.TrelloCardClient;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

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
  public Response createCardComment(String cardId, String comment) {
    return client.createCardComment(cardId, comment);
  }

  @Override
  public Response moveCardToList(String cardId, String listId) {
    return client.moveCardToList(cardId, listId);
  }

  @Override
  public String getCardIdByCreationResponse(Response createCardResponse) {
    return client.getCardIdByResponse(createCardResponse);
  }

  @Override
  public Response getCardById(String cardId) {
    return client.getCardById(cardId);
  }

  @Override
  public Response getCardActionsById(String cardId) {
    return client.getCardActionsById(cardId);
  }

  @Override
  public String getCardIdByName(String cardName, String boardId) {
    Response response = client.getAllCardsOnBoard(boardId);
    // Find the card with the matching name
    List<Map<String, Object>> cards = response.jsonPath().getList("$");
    for (Map<String, Object> card : cards) {
      if (cardName.equals(card.get("name"))) {
        return String.valueOf(card.get("id"));
      }
    }
    return null; // Return null if no matching card is found
  }

}
