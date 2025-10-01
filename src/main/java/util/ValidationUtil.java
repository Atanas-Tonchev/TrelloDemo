package util;

import io.restassured.response.Response;

import static org.testng.Assert.*;

public final class ValidationUtil {

  private ValidationUtil() {}

  // Assert HTTP status code
  public static void assertStatusCode(Response response, int expectedStatus) {
    assertEquals(response.statusCode(), expectedStatus, "Unexpected HTTP status code");
  }

  // Assert JSON object structure (starts with { and ends with })
  public static void assertJsonObject(Response response) {
    String body = response.getBody().asString().trim();
    assertTrue(body.startsWith("{") && body.endsWith("}"),
        "Response JSON should start with '{' and end with '}'");
  }

  // Assert required fields in board creation response
  public static void assertCreationFields(Response response, String expectedName) {
    assertNotNull(response.jsonPath().get("id"), "Response JSON should contain 'id'");
    assertNotNull(response.jsonPath().get("name"), "Response JSON should contain 'name'");
    assertEquals(response.jsonPath().getString("name"), expectedName, "'name' does not match expected value");
    assertTrue(response.jsonPath().get("closed") instanceof Boolean, "'closed' should be a boolean");
    assertNotNull(response.jsonPath().get("url"), "Response JSON should contain 'url'");
  }

  // Assert board ID is not null
  public static void assertIdNotNull(String id) {
    assertNotNull(id, "The ID should not be null");
  }

}