// src/main/java/service/ITrelloBoardService.java
package services;

import io.restassured.response.Response;
import models.TrelloBoardModel;

public interface ITrelloBoardService {

  default Response createBoard(String boardName) {
    return null;
  }

  default String createBoardAndReturnId(TrelloBoardModel trelloBoardModel) {
    return null;
  }

  default String getBoardIdByCreationResponse(Response creationResponse) {
    return null;
  }

  default String getBoardIdByName(String boardName) {
    return null;
  }
}
