package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TrelloListClient extends AbstractTrelloClient {

  public TrelloListClient(String apiKey, String authToken, String baseUrl) {
    super(apiKey, authToken, baseUrl);
  }

  public Response createList(String boardId, String name) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("idBoard", boardId)
        .queryParam("name", name)
        .when()
        .post("/lists");
  }


  public Response getList(String listId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
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
  }

  public Response getAllListsOnBoard(String boardId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .when()
        .get("/boards/" + boardId + "/lists");
  }

  // Note: Trello does not directly support DELETE for a list,
  // it is usually "closed" (archived) with a PUT request.

  public Response archiveList(String listId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("value", "true")
        .when()
        .put("/lists/" + listId + "/closed");
  }

  public Response updateListName(String listId, String newName) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("value", newName)
        .when()
        .put("/lists/" + listId + "/name");
  }
}
