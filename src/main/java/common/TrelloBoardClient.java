package common;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * TrelloBoardClient is a client for interacting with the Trello API to manage boards.
 * It provides methods to create, retrieve, and delete Trello boards using the provided API key and token.
 */


public class TrelloBoardClient {
  private final String apiKey;
  private final String token;

  public TrelloBoardClient(String apiKey, String token, String baseURI) {
    this.apiKey = apiKey;
    this.token = token;
    RestAssured.baseURI = baseURI;
  }


  public Response createBoard(String name) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .queryParam("name", name)
        .when()
        .post("/boards/");
  }

  public Response getBoard(String boardId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .when()
        .get("/boards/" + boardId);
  }

  public Response deleteBoard(String boardId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .when()
        .delete("/boards/" + boardId);
  }

  private Response getAllBoards() {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .when()
        .get("/members/me/boards");
  }

  public String getBoardIdByName(String boardName) {
    Response response = getAllBoards();
    if (response.statusCode() != 200) {
      return null;
    } else {
      return searchBoardByNameAndGetId(boardName);
    }
  }

  private String searchBoardByNameAndGetId(String boardName) {
    Response response = getAllBoards();
    return response.jsonPath().getList("", Object.class).stream()
        .filter(board -> ((String) ((java.util.Map<?, ?>) board).get("name")).equals(boardName))
        .findFirst()
        .map(board -> (String) ((java.util.Map<?, ?>) board).get("id"))
        .orElse(null);
  }
}
