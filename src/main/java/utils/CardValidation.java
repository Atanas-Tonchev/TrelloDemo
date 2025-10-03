package utils;

import io.restassured.response.Response;
import models.TrelloCardModel;
import services.TrelloCardServiceImpl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static utils.LogUtil.logInfo;

public class CardValidation extends ValidationUtil {

  @Override
  public void assertSuccessResponseArray(Response response) {
    super.assertSuccessResponseArray(response);
    logInfo("Validation success.");
  }

  @Override
  public void assertSuccessResponseMap(Response response) {
    super.assertSuccessResponseMap(response);
    logInfo("Validation success.");
  }

  @Override
  public void assertStatusCode(Response response, int expectedStatus) {
    super.assertStatusCode(response, expectedStatus);
    logInfo("Validation success.");
  }

  @Override
  public void assertResponseBody(Response response, String expectedName) {
    super.assertResponseBody(response, expectedName);
    assertNotNull(response.jsonPath().get("idList"), "Response JSON should contain 'idList'");
    assertNotNull(response.jsonPath().get("url"), "Response JSON should contain 'url'");
    assertNotNull(response.jsonPath().get("shortUrl"), "Response JSON should contain 'shortUrl'");
    assertNotNull(response.jsonPath().get("idBoard"), "Response JSON should contain 'idBoard'");
    logInfo("Card response body validation completed successfully.");
  }

  @Override
  public void assertIdNotNull(String id) {
    super.assertIdNotNull(id);
    logInfo("Validation success.");
  }

  public void validateCardInListAfterMoving(TrelloCardServiceImpl cardService, TrelloCardModel trelloCardModel, String expectedListId, String expectedListName) {
    logInfo("Validating card movement to the expected list...");
    Response getCardResponse = cardService.getCardById(trelloCardModel.getId());
    assertStatusCode(getCardResponse, 200);
    trelloCardModel.setIdList(getCardResponse.jsonPath().getString("idList"));
    assertEquals(trelloCardModel.getIdList(), expectedListId, "Card's idList should match the destination list ID");
    logInfo("Card with ID: " + trelloCardModel.getId() + " successfully moved to the list: \"" + expectedListName + "\"");
  }

  public void validateCommentFields(Response response, String commentText) {
    logInfo("Asserting comment fields...");
    String actualText = response.jsonPath().getString("data.text");
    assertEquals(actualText, commentText, "'data.text' does not match expected value");
    logInfo("The comment text is present and matches the input.");

    String actionType = response.jsonPath().getString("type");
    assertNotNull(actionType, "Response JSON should contain 'type'");
    assertEquals(actionType, "commentCard", "'type' should be 'commentCard'");
    logInfo("The action type is commentCard.");

    logInfo("Comment fields assertion completed successfully.");
  }

}
