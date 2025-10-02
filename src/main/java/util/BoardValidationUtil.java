package util;

import io.restassured.response.Response;

import static org.testng.Assert.assertNotNull;

public final class BoardValidationUtil extends ValidationUtil {

  @Override
  public void assertResponseSuccess(Response response) {
    super.assertResponseSuccess(response);
  }

  @Override
  public void assertStatusCode(Response response, int expectedStatus) {
    super.assertStatusCode(response, expectedStatus);
  }

  @Override
  public void assertResponseBody(Response response, String expectedName) {
    super.assertResponseBody(response, expectedName);
    assertNotNull(response.jsonPath().get("url"), "Response JSON should contain 'url'");
    assertNotNull(response.jsonPath().get("idOrganization"), "Response JSON should contain 'idOrganization'");
    assertNotNull(response.jsonPath().get("shortUrl"), "Response JSON should contain 'shortUrl'");
  }

  @Override
  public void assertIdNotNull(String id) {
    super.assertIdNotNull(id);
  }

}
