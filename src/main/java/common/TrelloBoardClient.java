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

  public Response getAllBoards() {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .when()
        .get("/members/me/boards");
  }
}
