package services;

import io.restassured.response.Response;

public interface ITrelloCardService {

  Response createCard(String listId, String name);

  Response moveCardToList(String cardId, String listId);

  String getCardIdByCreationResponse(Response createCardResponse);

  Response getCardById(String cardId);
}
