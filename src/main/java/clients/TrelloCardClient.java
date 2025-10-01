package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TrelloCardClient extends AbstractTrelloClient{

  public TrelloCardClient(String apiKey, String authToken, String baseUrl) {
    super(apiKey, authToken, baseUrl);
  }

  public Response createCard(String listId, String name) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("idList", listId)
        .queryParam("name", name)
        .when()
        .post("/cards");
  }

  public String getCardIdByResponse(Response response) {
    return response.jsonPath().getString("id");
  }

  public Response moveCardToList(String cardId, String listId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("idList", listId)
        .when()
        .put("/cards/" + cardId);
  }

  public Response getCardById(String cardId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .when()
        .get("/cards/" + cardId);
  }
}
