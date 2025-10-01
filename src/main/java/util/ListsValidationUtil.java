package util;

import io.restassured.response.Response;
import services.TrelloListServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertNotNull;
import static util.LogUtil.logInfo;

public class ListsValidationUtil extends ValidationUtil {

  @Override
  public void assertJsonObject(io.restassured.response.Response response) {
    super.assertJsonObject(response);
  }

  @Override
  public void assertStatusCode(io.restassured.response.Response response, int expectedStatus) {
    super.assertStatusCode(response, expectedStatus);
  }

  @Override
  public void assertCreationFields(io.restassured.response.Response response, String expectedName) {
    super.assertCreationFields(response, expectedName);
    assertNotNull(response.jsonPath().get("idBoard"), "Response JSON should contain 'idBoard'");
    assertNotNull(response.jsonPath().get("pos"), "Response JSON should contain 'pos'");
    assertNotNull(response.jsonPath().get("datasource"), "Response JSON should contain 'datasource'");
  }

  @Override
  public void assertIdNotNull(String id) {
    super.assertIdNotNull(id);
  }

  public void validateListResponseBody(Response response, String expectedName, String id) {
    // Verify the response body is a valid JSON object
    assertJsonObject(response);
    // Verify the response body contains expected fields
    assertCreationFields(response, expectedName);
    // Verify the list ID is not null
    assertIdNotNull(id);
  }

  public void validateAllLists(List<String> actualListsCreated, List<String> expectedListsCreated, String boardId, TrelloListServiceImpl trelloListService) {
    logInfo("Validating all lists created on the board...");
    // Verify the number of created lists matches the expected count
    assertEquals(actualListsCreated.size(), expectedListsCreated.size(), "Number of created lists should match");
    // Verify that all three lists are created and belong to the newly created board
    List<String> fetchedListByEntity = getAllListByEntity(boardId, "id", trelloListService);
    assertEqualsNoOrder(fetchedListByEntity.toArray(), actualListsCreated.toArray(), "Fetched list IDs should match created list IDs");
    // Verify each list belongs to the correct board
    fetchedListByEntity = getAllListByEntity(boardId, "idBoard", trelloListService);
    for (String idBoard : fetchedListByEntity) {
      assertEquals(idBoard, boardId, "List should belong to the correct board");
    }
    logInfo("All lists are created and belong to the newly created board.");
  }

  private List<String> getAllListByEntity(String boardId, String entityName, TrelloListServiceImpl trelloListService) {
    if (boardId != null) {
      Response response = trelloListService.getAllListsOnBoard(boardId);
      return response.jsonPath().getList(entityName);
    } else {
      logInfo("Board ID is null, cannot fetch lists.");
      return new ArrayList<>();
    }
  }

}
