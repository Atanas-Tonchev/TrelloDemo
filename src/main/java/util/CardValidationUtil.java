package util;

import io.restassured.response.Response;
import models.TrelloCardModel;
import services.TrelloCardServiceImpl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CardValidationUtil extends ValidationUtil {

  @Override
  public void assertJsonObject(io.restassured.response.Response response) {
    super.assertJsonObject(response);
  }

  @Override
  public void assertStatusCode(Response response, int expectedStatus) {
    super.assertStatusCode(response, expectedStatus);
  }

  @Override
  public void assertCreationFields(Response response, String expectedName) {
    super.assertCreationFields(response, expectedName);
    assertNotNull(response.jsonPath().get("idList"), "Response JSON should contain 'idList'");
    assertNotNull(response.jsonPath().get("url"), "Response JSON should contain 'url'");
    assertNotNull(response.jsonPath().get("shortUrl"), "Response JSON should contain 'shortUrl'");
    assertNotNull(response.jsonPath().get("idBoard"), "Response JSON should contain 'idBoard'");
  }

  @Override
  public void assertIdNotNull(String id) {
    super.assertIdNotNull(id);
  }

  public void validateCardCreation(Response response, String expectedName, String cardId) {
    LogUtil.logInfo("Validating card creation...");
    // Verify the response body is a valid JSON object
    assertJsonObject(response);
    // Verify the response body contains expected fields
    assertCreationFields(response, expectedName);
    LogUtil.logInfo("Validation completed successfully. Created card ID: " + cardId);
  }

  public void validateCardInListAfterMoving(TrelloCardServiceImpl cardService, TrelloCardModel trelloCardModel, String expectedListId, String expectedListName) {
    LogUtil.logInfo("Validating card movement to the expected list...");
    Response getCardResponse = cardService.getCardById(trelloCardModel.getId());
    assertStatusCode(getCardResponse, 200);
    trelloCardModel.setIdList(getCardResponse.jsonPath().getString("idList"));
    assertEquals(trelloCardModel.getIdList(), expectedListId, "Card's idList should match the destination list ID");
    LogUtil.logInfo("Card with ID: " + trelloCardModel.getId() + " successfully moved to the list: \"" + expectedListName + "\"");
  }

}
