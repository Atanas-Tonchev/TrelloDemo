package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TrelloBoardClient extends AbstractTrelloClient{

  public TrelloBoardClient(String apiKey, String authToken, String baseUrl) {
    super(apiKey, authToken, baseUrl);
  }

  public Response createBoard(String name) {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .queryParam("name", name)
        .when()
        .post("/boards/");
  }

  public String getBoardIdByCreationResponse(Response creationResponse) {
    return creationResponse.jsonPath().getString("id");
  }

  public String getBoardIdByName(String boardName) {
    Response response = getAllBoards();
    if (response.statusCode() != 200) {
      return null;
    } else {
      return searchBoardByNameAndGetId(boardName);
    }
  }

  private Response getAllBoards() {
    return RestAssured
        .given()
        .queryParam("key", apiKey)
        .queryParam("token", authToken)
        .when()
        .get("/members/me/boards");
  }

  private String searchBoardByNameAndGetId(String boardName) {
    Response response = getAllBoards();
    return response.jsonPath().getList("", java.util.Map.class).stream()
        .filter(board -> board.get("name") != null && board.get("name").equals(boardName))
        .findFirst()
        .map(board -> board.get("id").toString())
        .orElse(null);
  }

}
