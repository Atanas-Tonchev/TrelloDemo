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

  public Response getCard(String cardId) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .when()
        .get("/cards/" + cardId);
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
}
