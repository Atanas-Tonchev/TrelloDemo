package services;

import io.restassured.response.Response;

public interface ITrelloListService {
  Response createList(String boardId, String listName);
  String getListIdByName(String listName, String boardId);
  Response getAllListsOnBoard(String boardId);
  Response getListById(String listId);
  Response updateListName(String listId, String newName);
  Response archiveListById(String listId);
}
