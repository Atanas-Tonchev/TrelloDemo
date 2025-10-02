package util;

import io.restassured.response.Response;
import models.TrelloCardModel;
import services.TrelloCardServiceImpl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CardValidationUtil extends ValidationUtil {

  @Override
  public void assertPostSuccess(Response response) {
    super.assertPostSuccess(response);
  }

  @Override
  public void assertGetSuccess(Response response) {
    super.assertGetSuccess(response);
  }

  @Override
  public void assertStatusCode(Response response, int expectedStatus) {
    super.assertStatusCode(response, expectedStatus);
  }

  @Override
  public void assertResponseBody(Response response, String expectedName) {
    super.assertResponseBody(response, expectedName);
    assertNotNull(response.jsonPath().get("idList"), "Response JSON should contain 'idList'");
    assertNotNull(response.jsonPath().get("url"), "Response JSON should contain 'url'");
    assertNotNull(response.jsonPath().get("shortUrl"), "Response JSON should contain 'shortUrl'");
    assertNotNull(response.jsonPath().get("idBoard"), "Response JSON should contain 'idBoard'");
  }

  @Override
  public void assertIdNotNull(String id) {
    super.assertIdNotNull(id);
  }

  public void validateCardInListAfterMoving(TrelloCardServiceImpl cardService, TrelloCardModel trelloCardModel, String expectedListId, String expectedListName) {
    LogUtil.logInfo("Validating card movement to the expected list...");
    Response getCardResponse = cardService.getCardById(trelloCardModel.getId());
    assertStatusCode(getCardResponse, 200);
    trelloCardModel.setIdList(getCardResponse.jsonPath().getString("idList"));
    assertEquals(trelloCardModel.getIdList(), expectedListId, "Card's idList should match the destination list ID");
    LogUtil.logInfo("Card with ID: " + trelloCardModel.getId() + " successfully moved to the list: \"" + expectedListName + "\"");
  }

  public void validateCommentFields(Response response, String commentText) {
    LogUtil.logInfo("Asserting comment fields...");
    String actualText = response.jsonPath().getString("data.text");
    assertEquals(actualText, commentText, "'data.text' does not match expected value");
    LogUtil.logInfo("The comment text is present and matches the input.");

    String actionType = response.jsonPath().getString("type");
    assertNotNull(actionType, "Response JSON should contain 'type'");
    assertEquals(actionType, "commentCard", "'type' should be 'commentCard'");
    LogUtil.logInfo("The action type is commentCard.");

    LogUtil.logInfo("Comment fields assertion completed successfully.");
  }
}
