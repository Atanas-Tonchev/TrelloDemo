// src/main/java/service/TrelloBoardServiceImpl.java
package services;

import clients.TrelloBoardClient;
import io.restassured.response.Response;
import models.TrelloBoardModel;

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
  public String createBoardAndReturnId(TrelloBoardModel trelloBoardModel) {
    return client.createBoard(trelloBoardModel.getName()).jsonPath().getString("id");
  }

  @Override
  public Response createBoard(String boardName) {
    return client.createBoard(boardName);
  }

  @Override
  public String getBoardIdByCreationResponse(Response creationResponse) {
    return client.getBoardIdByCreationResponse(creationResponse);
  }

}