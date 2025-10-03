package utils;

import io.restassured.response.Response;
import services.TrelloListServiceImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static constants.AutomationConstants.DEFAULT_LIST_NAMES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertNotNull;
import static utils.LogUtil.logInfo;

public class ListsValidation extends ValidationUtil {

  @Override
  public void assertSuccessResponseArray(Response response) {
    super.assertSuccessResponseArray(response);
    logInfo("Validation success.");
  }

  @Override
  public void assertStatusCode(Response response, int expectedStatus) {
    super.assertStatusCode(response, expectedStatus);
  }

  @Override
  public void assertResponseBody(Response response, String expectedName) {
    super.assertResponseBody(response, expectedName);
    assertNotNull(response.jsonPath().get("idBoard"), "Response JSON should contain 'idBoard'");
    assertNotNull(response.jsonPath().get("pos"), "Response JSON should contain 'pos'");
    assertNotNull(response.jsonPath().get("datasource"), "Response JSON should contain 'datasource'");
    logInfo("List response body validation completed successfully.");
  }

  @Override
  public void assertIdNotNull(String id) {
    super.assertIdNotNull(id);
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

  public Map<String, String> manageBoardLists(String boardId, TrelloListServiceImpl trelloListService) {
    logInfo("Managing board lists.");
    Map<String, String> mapOfLists = new LinkedHashMap<>();
    List<String> actualList = getAllListByEntity(boardId, "name", trelloListService);

    // Update existing lists or create new ones as necessary
    if (!actualList.isEmpty()) {
      logInfo("Existing lists found on the board, updating if necessary.");
      // Update existing lists if names differ
      for (String expectedName : DEFAULT_LIST_NAMES) {
        if (!actualList.contains(expectedName)) {
          actualList.stream()
              .filter(actualName -> !DEFAULT_LIST_NAMES.contains(actualName))
              .findFirst()
              .ifPresent(actualName -> {
                String actualId = trelloListService.getListIdByName(actualName, boardId);
                assertIdNotNull(actualId);
                Response response = trelloListService.updateListName(actualId, expectedName);
                assertStatusCode(response, 200);
                logInfo("Updated list from: " + actualName + " to: " + expectedName);
              });
        }
      }
    } else {
      logInfo("No existing lists found, creating default lists.");
      // Create new lists if none exist
      DEFAULT_LIST_NAMES.forEach(listName -> {
        Response response = trelloListService.createList(boardId, listName);
        assertStatusCode(response, 200);
      });
    }

    // Repopulate map with all lists and their IDs
    getAllListByEntity(boardId, "id", trelloListService).forEach(listId -> {
      Response response = trelloListService.getListById(listId);
      assertStatusCode(response, 200);
      String listName = response.jsonPath().getString("name");
      mapOfLists.put(listName, listId);
    });
    return mapOfLists;
  }

  public List<String> getAllListByEntity(String boardId, String entityName, TrelloListServiceImpl trelloListService) {
    if (boardId != null) {
      Response response = trelloListService.getAllListsOnBoard(boardId);
      return response.jsonPath().getList(entityName);
    } else {
      logInfo("Board ID is null, cannot fetch lists.");
      return new ArrayList<>();
    }
  }

}
