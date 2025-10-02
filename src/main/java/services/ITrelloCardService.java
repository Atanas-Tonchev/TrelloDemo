package services;

import io.restassured.response.Response;

public interface ITrelloCardService {

  default Response createCard(String listId, String name) {
    return null;
  }

  Response createCardComment(String cardId, String comment);

  Response moveCardToList(String cardId, String listId);

  default String getCardIdByCreationResponse(Response createCardResponse) {
    return null;
  }

  default Response getCardById(String cardId) {
    return null;
  }

  default Response getCardActionsById(String cardId) {
    return null;
  }

}
