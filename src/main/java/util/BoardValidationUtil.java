package util;

import io.restassured.response.Response;

import static org.testng.Assert.assertNotNull;
import static util.LogUtil.logInfo;

public final class BoardValidationUtil extends ValidationUtil {

  @Override
  public void assertSuccessResponseArray(Response response) {
    super.assertSuccessResponseArray(response);
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
    assertNotNull(response.jsonPath().get("url"), "Response JSON should contain 'url'");
    assertNotNull(response.jsonPath().get("idOrganization"), "Response JSON should contain 'idOrganization'");
    assertNotNull(response.jsonPath().get("shortUrl"), "Response JSON should contain 'shortUrl'");
    logInfo("Board response body validation completed successfully.");
  }

  @Override
  public void assertIdNotNull(String id) {
    super.assertIdNotNull(id);
    logInfo("Validation success.");
  }

}
