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

  public String getListIdByName(String listName, String boardId) {
    Response response = getAllListsOnBoard(boardId);
    if (response.statusCode() != 200) {
      return null;
    } else {
      return searchListIdByName(listName, boardId);
    }
  }

  private String searchListIdByName(String listName, String boardId) {
    Response response = getAllListsOnBoard(boardId);
    return response.jsonPath().getList("", java.util.Map.class).stream()
        .filter(list -> list.get("name") != null && list.get("name").equals(listName))
        .findFirst()
        .map(list -> list.get("id").toString())
        .orElse(null);
    /*return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .queryParam("fields", "name")
        .when()
        .get("/lists/" + listName);*/
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

  public Response updateListName(String listId, String newName) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", token)
        .queryParam("value", newName)
        .when()
        .put("/lists/" + listId + "/name");
  }
}
