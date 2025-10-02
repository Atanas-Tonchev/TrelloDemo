package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TrelloCheckListClient extends AbstractTrelloClient {

  public TrelloCheckListClient(String apiKey, String authToken, String baseUrl) {
    super(apiKey, authToken, baseUrl);
  }

  public Response createChecklistOnCard(String id, String checklistName) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("name", checklistName)
        .when()
        .post("/cards/" + id + "/checklists");
  }

  public Response addItemToChecklist(String checklistId, String itemName) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("name", itemName)
        .when()
        .post("/checklists/" + checklistId + "/checkItems");
  }

  public Response getChecklistById(String checklistId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .when()
        .get("/checklists/" + checklistId);
  }

  public Response updateChecklistItemState(String cardId, String checklistId, String itemId, String state) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("value", state)
        .when()
        .put("/cards/" + cardId + "/checklist/" + checklistId + "/checkItem/" + itemId + "/state");

  }

  public Response getAllChecklistsOnCard(String cardId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .when()
        .get("/cards/" + cardId + "/checklists");
  }

}
