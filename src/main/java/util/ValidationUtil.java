package util;

import io.restassured.response.Response;

import static org.testng.Assert.*;
import static util.LogUtil.logException;

public class ValidationUtil {

  // General assertion for successful creation (2xx status code and valid JSON with 'id')
  public void assertSuccessResponseArray(Response response) {
    try {
      assertTrue(response.statusCode() >= 200 && response.statusCode() < 300, "Creation did not return a success status code");
      assertJsonObjectArray(response);
      assertNotNull(response.jsonPath().get("id"), "Response JSON should contain 'id'");
    } catch (Exception e) {
      logException("Exception in assertSuccessResponseArray: ", e);
      throw e;
    }
  }

  // General assertion for successful retrieve (2xx status code and valid JSON with 'id')
  public void assertSuccessResponseMap(Response response) {
    try {
      assertTrue(response.statusCode() >= 200 && response.statusCode() < 300, "Creation did not return a success status code");
      assertJsonObjectMap(response);
      assertNotNull(response.jsonPath().get("id"), "Response JSON should contain 'id'");
    } catch (Exception e) {
      logException("Exception in assertSuccessResponseMap: ", e);
      throw e;
    }
  }

  // Assert HTTP status code
  public void assertStatusCode(Response response, int expectedStatus) {
    assertEquals(response.statusCode(), expectedStatus, "Unexpected HTTP status code");
  }

  // Assert JSON object structure (starts with { and ends with })
  private void assertJsonObjectArray(Response response) {
    String body = response.getBody().asString().trim();
    assertTrue(body.startsWith("{") && body.endsWith("}"),
        "Response JSON should start with '{' and end with '}'");
  }

  // Assert JSON object structure (starts with { and ends with })
  private void assertJsonObjectMap(Response response) {
    String body = response.getBody().asString().trim();
    assertTrue(body.startsWith("[") && body.endsWith("]"),
        "Response JSON should start with '{' and end with '}'");
  }

  // Assert required fields in board creation response
  public void assertResponseBody(Response response, String expectedName) {
    assertNotNull(response.jsonPath().get("name"), "Response JSON should contain 'name'");
    assertEquals(response.jsonPath().getString("name"), expectedName, "'name' does not match expected value");
    assertTrue(response.jsonPath().get("closed") instanceof Boolean, "'closed' should be a boolean");
  }

  // Assert board ID is not null
  public void assertIdNotNull(String id) {
    assertNotNull(id, "The ID should not be null");
  }

}