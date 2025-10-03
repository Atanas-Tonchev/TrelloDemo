// src/main/java/service/ITrelloBoardService.java
package services;

import io.restassured.response.Response;

public interface ITrelloBoardService {

  default Response createBoard(String boardName) {
    return null;
  }

  default String createBoardAndReturnId(String boardName) {
    return null;
  }

  default String getBoardIdByName(String boardName) {
    return null;
  }
}
