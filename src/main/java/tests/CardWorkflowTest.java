package tests;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.TrelloBoardService;
import service.TrelloCardService;
import service.TrelloListService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static config.BaseTest.objConfig;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertNotNull;
import static util.LogUtil.logException;
import static util.LogUtil.logInfo;

public class CardWorkflowTest {

  private TrelloBoardService boardService;
  private TrelloListService trelloListService;
  private TrelloCardService cardService;
  private String boardId;
  Map<String, String> mapOfLists;
  private String cardId;
  private String boardName;

  @BeforeClass
  public void setUp() {
    try {
      logInfo("Setting up CardWorkflowTest...");
      boardName = "Interview Board";
      String apiKey = objConfig.getApiKey();
      String authToken = objConfig.getAuthToken();
      String baseUrl = objConfig.getBaseUrl();
      boardService = new TrelloBoardService(apiKey, authToken, baseUrl);
      cardService = new TrelloCardService(apiKey, authToken, baseUrl);
      trelloListService = new TrelloListService(apiKey, authToken, baseUrl);
      // Execute prerequisite operations
      executePrerequisites(boardName);
    } catch (Exception e) {
      logException("Exception in setUp: " + e.getMessage(), e);
    }
  }


  @Test(priority = 1)

  public void testCreateTitledCard() {

    //cardId = cardService.createCardAndReturnId(listId, "Automation_Test_Card");

    assertNotNull(cardId, "Card ID should not be null");

  }


  /*@Test(priority = 2, dependsOnMethods = "testCreateCard")

  public void testGetCard() {

    Response response = cardService.getCardById(cardId);

    assertEquals(response.statusCode(), 200);

    assertEquals(response.jsonPath().getString("id"), cardId);

  }


  @AfterClass

  public void tearDown() {

    if (cardId != null) {

      cardService.deleteCardById(cardId);

    }

    if (boardId != null) {

      boardService.deleteBoardById(boardId);

    }

  }*/

  // Region: Helper Methods

  private void executePrerequisites(String boardName) {
    try {
      LinkedList<String> expectedListNames = trelloListService.getAllExpectedListNames();
      // Search board by name, if not found create a new one with all expected lists
      boardId = boardService.getBoardIdByName(boardName);
      if (boardId == null) {
        boardId = boardService.createBoardAndReturnId(boardName);
        assertNotNull(boardId, "Board ID should not be null after creation");
      }
      mapOfLists = manageBoardLists(boardId, expectedListNames);
    } catch (Exception e) {
      logException("Exception in executePrerequisites: " + e.getMessage(), e);
    }
  }


  private Map<String, String> manageBoardLists(String boardId, List<String> expectedListNames) {
    Map<String, String> mapOfLists = new HashMap<>();
    List<String> actualList = getAllListByEntity(boardId, "name");

    if (!actualList.isEmpty()) {
      // Update existing lists if names differ
      for (String expectedName : expectedListNames) {
        if (!actualList.contains(expectedName)) {
          actualList.stream()
              .filter(actualName -> !expectedListNames.contains(actualName))
              .findFirst()
              .ifPresent(actualName -> {
                String actualId = trelloListService.getListIdByName(actualName, boardId);
                trelloListService.updateListName(actualId, expectedName);
                logInfo("Updated list from: " + actualName + " to: " + expectedName);
              });
        }
      }
    } else {
      // Create new lists if none exist
      expectedListNames.forEach(listName -> {
        String listId = trelloListService.createListOnBoardAndReturnId(boardId, listName);
        assertNotNull(listId, "List ID should not be null for list: " + listName);
      });
    }

    // Repopulate map with all lists and their IDs
    getAllListByEntity(boardId, "id").forEach(listId -> {
      Response response = trelloListService.getListById(listId);
      String listName = response.jsonPath().getString("name");
      mapOfLists.put(listName, listId);
    });
    return mapOfLists;
  }


  private List<String> getAllListByEntity(String boardId, String entityName) {
    if (boardId != null) {
      Response response = trelloListService.getAllListsOnBoard(boardId);
      return response.jsonPath().getList(entityName);
    } else {
      logInfo("Board ID is null, cannot fetch lists.");
      return new ArrayList<>();
    }
  }

  // End Region
}
