package util;

import io.restassured.response.Response;

import static org.testng.Assert.*;

public class ValidationUtil {

  // Assert HTTP status code
  public void assertStatusCode(Response response, int expectedStatus) {
    assertEquals(response.statusCode(), expectedStatus, "Unexpected HTTP status code");
  }

  // Assert JSON object structure (starts with { and ends with })
  public void assertJsonObject(Response response) {
    String body = response.getBody().asString().trim();
    assertTrue(body.startsWith("{") && body.endsWith("}"),
        "Response JSON should start with '{' and end with '}'");
  }

  // Assert required fields in board creation response
  public void assertCreationFields(Response response, String expectedName) {
    assertNotNull(response.jsonPath().get("id"), "Response JSON should contain 'id'");
    assertNotNull(response.jsonPath().get("name"), "Response JSON should contain 'name'");
    assertEquals(response.jsonPath().getString("name"), expectedName, "'name' does not match expected value");
    assertTrue(response.jsonPath().get("closed") instanceof Boolean, "'closed' should be a boolean");
  }

  // Assert board ID is not null
  public void assertIdNotNull(String id) {
    assertNotNull(id, "The ID should not be null");
  }

}