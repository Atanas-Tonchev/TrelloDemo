// src/main/java/service/TrelloBoardServiceImpl.java
package services;

import clients.TrelloBoardClient;
import io.restassured.response.Response;
import models.TrelloBoardObject;

public class TrelloBoardServiceImpl implements ITrelloBoardService {
  private final TrelloBoardClient client;

  public TrelloBoardServiceImpl(String apiKey, String authToken, String baseUrl) {
    this.client = new TrelloBoardClient(apiKey, authToken, baseUrl);
  }

  @Override
  public String getBoardIdByName(String boardName) {
    return client.getBoardIdByName(boardName);
  }

  @Override
  public String createBoardAndReturnId(TrelloBoardObject trelloBoardObject) {
    return client.createBoard(trelloBoardObject.getName()).jsonPath().getString("id");
  }

  @Override
  public Response createBoard(String boardName) {
    return client.createBoard(boardName);
  }

  @Override
  public String getBoardIdByCreationResponse(Response creationResponse) {
    return client.getBoardIdByCreationResponse(creationResponse);
  }

  @Override
  public Response getBoardById(String boardId) {
    return client.getBoard(boardId);
  }

  @Override
  public void deleteBoardById(String boardId) {
    client.deleteBoard(boardId).then().statusCode(200);
  }
}