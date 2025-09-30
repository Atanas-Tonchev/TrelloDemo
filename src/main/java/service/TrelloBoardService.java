package service;

import common.TrelloBoardClient;
import io.restassured.response.Response;

import static util.LogUtil.logError;
import static util.LogUtil.logException;

/**
 * Service class to interact with Trello boards using TrelloBoardClient.
 */

public class TrelloBoardService {
  private final TrelloBoardClient boardClient;

  public TrelloBoardService(String apiKey, String token, String baseUri) {
    this.boardClient = new TrelloBoardClient(apiKey, token, baseUri);
  }

  public String createBoardAndReturnId(String name) {
    String boardId = null;
    try {
      Response response = boardClient.createBoard(name);
      if (response.statusCode() != 200) {
        logError("Failed to create board. Status Code: " + response.statusCode());
      } else {
        boardId = response.jsonPath().getString("id");
      }
    } catch (Exception e) {
      logException("An error occurred while creating the board.", e);
    }
    return boardId;
  }

  public Response getBoardById(String boardId) {
    return boardClient.getBoard(boardId);
  }

  public void deleteBoardById(String boardId) {
    boardClient.deleteBoard(boardId).then().statusCode(200);
  }

  public String getBoardIdByName(String boardName) {
    return boardClient.getBoardIdByName(boardName);
  }
}
