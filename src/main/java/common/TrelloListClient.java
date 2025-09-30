package common;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TrelloListClient {

  private final String apiKey;
  private final String token;

  public TrelloListClient(String apiKey, String token, String baseURI) {
    this.apiKey = apiKey;
    this.token = token;
    RestAssured.baseURI = baseURI;

  }

  public Response createList(String boardId, String name) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .queryParam("idBoard", boardId)
        .queryParam("name", name)
        .when()
        .post("/lists");
  }


  public Response getList(String listId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .when()
        .get("/lists/" + listId);
  }

  public Response getAllListsOnBoard(String boardId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .when()
        .get("/boards/" + boardId + "/lists");
  }

  // Note: Trello does not directly support DELETE for a list,
  // it is usually "closed" (archived) with a PUT request.

  public Response archiveList(String listId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .queryParam("value", "true")
        .when()
        .put("/lists/" + listId + "/closed");
  }
}
