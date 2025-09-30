package service;

import common.TrelloListClient;
import io.restassured.response.Response;

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
}
