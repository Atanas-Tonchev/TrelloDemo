package services;

import io.restassured.response.Response;

public interface ITrelloListService {

  default Response createList(String boardId, String listName) {
    return null;
  }

  default String getListIdByName(String listName, String boardId) {
    return null;
  }

  default Response getAllListsOnBoard(String boardId) {
    return null;
  }

  default Response getListById(String listId) {
    return null;
  }

  default Response updateListName(String listId, String newName) {
    return null;
  }

  default Response archiveListById(String listId) {
    return null;
  }
}
