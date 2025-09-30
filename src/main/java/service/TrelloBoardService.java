package service;

import common.TrelloBoardClient;
import io.restassured.response.Response;
import util.LogUtil;

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
        LogUtil.getInstance().logError("Failed to create board. Status Code: " + response.statusCode());
      } else {
        boardId = response.jsonPath().getString("id");
      }
    } catch (Exception e) {
      LogUtil.getInstance().logException("An error occurred while creating the board.", e);
    }
    return boardId;
  }

  public Response getBoardById(String boardId) {
    return boardClient.getBoard(boardId);
  }

  public void deleteBoardById(String boardId) {
    boardClient.deleteBoard(boardId).then().statusCode(200);
  }

  public Response getAllBoards() {
    return boardClient.getAllBoards();
  }
}
