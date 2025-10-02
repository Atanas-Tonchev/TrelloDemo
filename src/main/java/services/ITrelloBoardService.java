// src/main/java/service/ITrelloBoardService.java
package services;

import io.restassured.response.Response;
import models.TrelloBoardModel;

public interface ITrelloBoardService {
  Response createBoard(String boardName);
  String createBoardAndReturnId(TrelloBoardModel trelloBoardModel);
  String getBoardIdByCreationResponse(Response creationResponse);
  String getBoardIdByName(String boardName);
}
