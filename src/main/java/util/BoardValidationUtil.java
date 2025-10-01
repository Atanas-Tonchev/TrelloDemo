package util;

import io.restassured.response.Response;

import static org.testng.Assert.assertNotNull;
import static util.LogUtil.logInfo;

public final class BoardValidationUtil extends ValidationUtil {

  @Override
  public void assertJsonObject(Response response) {
    super.assertJsonObject(response);
  }

  @Override
  public void assertStatusCode(Response response, int expectedStatus) {
    super.assertStatusCode(response, expectedStatus);
  }

  @Override
  public void assertCreationFields(Response response, String expectedName) {
    super.assertCreationFields(response, expectedName);
    assertNotNull(response.jsonPath().get("url"), "Response JSON should contain 'url'");
    assertNotNull(response.jsonPath().get("idOrganization"), "Response JSON should contain 'idOrganization'");
    assertNotNull(response.jsonPath().get("shortUrl"), "Response JSON should contain 'shortUrl'");
  }

  @Override
  public void assertIdNotNull(String id) {
    super.assertIdNotNull(id);
  }

  public void validateBoard(Response response, String expectedName, String id) {
    logInfo("Validating board creation...");
    // Verify the response body is a valid JSON object
    assertJsonObject(response);
    // Verify the response body contains expected fields
    assertCreationFields(response, expectedName);
    // Verify the board ID is not null
    assertIdNotNull(id);
    logInfo("Validation completed successfully. Created board ID: " + id);
  }

}
