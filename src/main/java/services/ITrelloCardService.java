package services;

import io.restassured.response.Response;

public interface ITrelloCardService {

  default Response createCard(String listId, String name) {
    return null;
  }

  default Response createCardComment(String cardId, String comment) {
    return null;
  }

  default Response moveCardToList(String cardId, String listId) {
    return null;
  }

  default String getCardIdByCreationResponse(Response createCardResponse) {
    return null;
  }

  default Response getCardById(String cardId) {
    return null;
  }

  default Response getCardActionsById(String cardId) {
    return null;
  }

  default String getCardIdByName(String cardName, String boardId) {
    return null;
  }
}
