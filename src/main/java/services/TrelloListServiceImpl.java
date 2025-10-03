package services;

import clients.TrelloListClient;
import io.restassured.response.Response;

import static utils.LogUtil.logInfo;

public class TrelloListServiceImpl implements ITrelloListService {
  private final TrelloListClient trelloListClient;

  public TrelloListServiceImpl(String apiKey, String authToken, String baseUrl) {
    this.trelloListClient = new TrelloListClient(apiKey, authToken, baseUrl);
  }

  @Override
  public Response createList(String boardId, String listName) {
    logInfo("Creating list with name: " + listName + " on board ID: " + boardId);
    return trelloListClient.createList(boardId, listName);
  }

  @Override
  public String getListIdByName(String listName, String boardId) {
    return trelloListClient.getListIdByName(listName, boardId);
  }

  @Override
  public Response getAllListsOnBoard(String boardId) {
    return trelloListClient.getAllListsOnBoard(boardId);
  }

  @Override
  public Response getListById(String listId) {
    return trelloListClient.getList(listId);
  }

  @Override
  public Response updateListName(String listId, String newName) {
    return trelloListClient.updateListName(listId, newName);
  }

  @Override
  public Response archiveListById(String listId) {
    logInfo("Archiving list with ID: " + listId);
    return trelloListClient.archiveList(listId);
  }

}
