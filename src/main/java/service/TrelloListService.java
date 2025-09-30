package service;

import common.TrelloListClient;
import io.restassured.response.Response;

import java.util.LinkedList;

public class TrelloListService {
  private final TrelloListClient listClient;

  public TrelloListService(String apiKey, String token, String baseURI) {
    this.listClient = new TrelloListClient(apiKey, token, baseURI);
  }

  public Response getListById(String listId) {
    return listClient.getList(listId);
  }

  public void archiveListById(String listId) {
    listClient.archiveList(listId).then().statusCode(200);
  }

  public Response getAllListsOnBoard(String boardId) {
    return listClient.getAllListsOnBoard(boardId);
  }

  public String createListOnBoardAndReturnId(String boardId, String listName) {
    Response response = listClient.createList(boardId, listName);
    response.then().statusCode(200);
    return response.jsonPath().getString("id");
  }

  public String getListIdByName(String listName, String boardId) {
    return listClient.getListIdByName(listName, boardId);
  }

  public void updateListName(String listId, String newName) {
    Response response = listClient.updateListName(listId, newName);
    response.then().statusCode(200);
  }

  public LinkedList<String> getAllExpectedListNames() {
    LinkedList<String> listNames = new LinkedList<>();
    listNames.add("Done");
    listNames.add("In Progress");
    listNames.add("To Do");
    return listNames;
  }
}
