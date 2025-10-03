package services;

import io.restassured.response.Response;

public interface ITrelloCheckListService {

  default Response createChecklistOnCard(String id, String checklistName) {
    return null;
  }

  default Response addItemToChecklist(String checklistId, String itemName) {
    return null;
  }

  default Response getChecklistById(String checklistId) {
    return null;
  }

  default Response updateChecklistItemState(String cardId, String checklistId, String itemId, String state) {
    return null;
  }

  default Response getAllChecklistsOnCard(String cardId) {
    return null;
  }

}
