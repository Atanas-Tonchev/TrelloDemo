// src/main/java/service/ITrelloBoardService.java
package services;

import io.restassured.response.Response;
import objects.TrelloBoardObject;

public interface ITrelloBoardService {
  Response createBoard(String boardName);
  String createBoardAndReturnId(TrelloBoardObject trelloBoardObject);
  String getBoardIdByCreationResponse(Response creationResponse);
  Response getBoardById(String boardId);
  String getBoardIdByName(String boardName);
  void deleteBoardById(String boardId);
}
