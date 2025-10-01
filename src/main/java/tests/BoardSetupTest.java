package tests;

import configs.BaseTest;
import io.restassured.response.Response;
import objects.TrelloBoardObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static constants.AutomationConstants.BOARD_NAME;
import static constants.AutomationConstants.DEFAULT_LIST_NAMES;
import static org.testng.Assert.*;
import static util.LogUtil.logException;
import static util.LogUtil.logInfo;
import static util.ValidationUtil.assertCreationFields;
import static util.ValidationUtil.assertIdNotNull;
import static util.ValidationUtil.assertJsonObject;
import static util.ValidationUtil.assertStatusCode;

import services.TrelloBoardServiceImpl;
import services.TrelloListServiceImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class to set up and verify Trello board operations.
 */

public class BoardSetupTest extends BaseTest {

  private TrelloBoardObject trelloBoardObject;
  private TrelloBoardServiceImpl boardService;
  private TrelloListServiceImpl trelloListService;
  private LinkedList<String> listNames;
  private boolean isTestSuccess;
  private Response response;

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    logInfo("Setting up BoardSetupTest...");
    try {
      trelloBoardObject = new TrelloBoardObject(BOARD_NAME);
      trelloBoardObject.setId(null);
      isTestSuccess = false;
      String apiKey = objConfig.getApiKey();
      String authToken = objConfig.getAuthToken();
      String baseUrl = objConfig.getBaseUrl();
      boardService = new TrelloBoardServiceImpl(apiKey, authToken, baseUrl);
      trelloListService = new TrelloListServiceImpl(apiKey, authToken, baseUrl);
      listNames = DEFAULT_LIST_NAMES;
      logInfo("BoardSetupTest setup completed.");
    } catch (Exception e) {
      logException("Exception in BoardSetupTest setup: ", e);
      throw e;
    }
  }


  @Test(priority = 1)
  public void testCreateBoard() {
    try {
      // Create a new board
      logInfo("Creating board with name: " + trelloBoardObject.getName());
      response = boardService.createBoard(trelloBoardObject.getName());
      // Verify board creation
      assertStatusCode(response, 200);
      // Verify the response body is a valid JSON object
      assertJsonObject(response);
      // Verify the response body contains expected fields
      assertCreationFields(response, trelloBoardObject.getName());
      // Verify the board ID is not null
      trelloBoardObject.setId(boardService.getBoardIdByCreationResponse(response));
      assertIdNotNull(trelloBoardObject.getId());
      logInfo("Board created successfully.");
      // mark test as success
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in testCreateBoard: ", e);
      throw e;
    } finally {
      if (!isTestSuccess) {
        logInfo("Test FAILED");
      } else {
        logInfo("Test PASSED");
      }
    }

  }

  @Test(priority = 2, dependsOnMethods = "testCreateBoard")
  public void createListsOnBoard() {
    String boardId = trelloBoardObject.getId();
    String boardName = trelloBoardObject.getName();
    try {
      logInfo("Starting create Lists On Board: " + boardName + " with ID: " + boardId);
      // Check if there are any lists exist on the board and clean them ,
      // because by default Trello creates 3 lists on new board witch are not the same as our default lists
      cleanAllBoardList(boardId);

      // Create lists on the board
      List<String> createdListIds = new ArrayList<>();
      for (String listName : listNames) {
        response = trelloListService.createList(boardId, listName);
        // Verify list creation response
        assertStatusCode(response, 200);
        String listId = trelloListService.getListIdByName(listName,boardId);
        // Verify the list ID is not null
        assertIdNotNull(listId);
        // Verify the response body is a valid JSON object
        assertJsonObject(response);
        // Verify the response body contains expected fields
        assertCreationFields(response, listName);
        createdListIds.add(listId);
      }

      // Verify lists creation
      assertEquals(createdListIds.size(), listNames.size(), "Number of created lists should match");
      logInfo("All lists created successfully on the board!");

      // Verify that all three lists are created and belong to the newly created board
      List<String> fetchedListByEntity = getAllListByEntity(boardId, "id");
      assertEqualsNoOrder(fetchedListByEntity.toArray(), createdListIds.toArray(), "Fetched list IDs should match created list IDs");
      logInfo("Verified all three lists are created");

      fetchedListByEntity = getAllListByEntity(boardId, "idBoard");
      for (String idBoard : fetchedListByEntity) {
        assertEquals(idBoard, boardId, "List should belong to the correct board");
      }
      logInfo("All lists verified to belong to the correct board.");
      // mark test as success
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in createListsOnBoard: ", e);
      throw e;
    } finally {
      if (!isTestSuccess) {
        logInfo("Test FAILED");
      } else {
        logInfo("Test PASSED");
      }
    }
  }


  /*@Test(priority = 2, dependsOnMethods = "testCreateBoard")
  public void createListsOnBoard() {
    String boardId = trelloBoardObject.getId();
    String boardName = trelloBoardObject.getName();
    try {
      logInfo("Starting create Lists On Board: " + boardName + " with ID: " + boardId);
      // Check if there are any lists exist on the board and clean them ,
      // because by default Trello creates 3 lists on new board witch are not the same as our default lists
      cleanAllBoardList(boardId);

      // Create lists on the board
      List<String> createdListIds = new ArrayList<>();
      for (String listName : listNames) {
        String listId = trelloListService.createListOnBoardAndReturnId(boardId, listName);
        assertIdNotNull(listId);
        createdListIds.add(listId);
      }

      // Verify lists creation
      assertEquals(createdListIds.size(), listNames.size(), "Number of created lists should match");
      logInfo("All lists created successfully on the board!");

      // Verify that all three lists are created and belong to the newly created board
      List<String> fetchedListByEntity = getAllListByEntity(boardId, "id");
      assertEqualsNoOrder(fetchedListByEntity.toArray(), createdListIds.toArray(), "Fetched list IDs should match created list IDs");
      logInfo("Verified all three lists are created");

      fetchedListByEntity = getAllListByEntity(boardId, "idBoard");
      for (String idBoard : fetchedListByEntity) {
        assertEquals(idBoard, boardId, "List should belong to the correct board");
      }
      logInfo("All lists verified to belong to the correct board.");

    } catch (Exception e) {
      logException("Exception in createListsOnBoard: ", e);
      throw e;
    }
  }*/

  // Region Helper Methods

  public void cleanAllBoardList(String boardId) {
    logInfo("Cleaning all existing lists from board");
    List<String> allListIds = getAllListIds(boardId);
    if (allListIds != null && !allListIds.isEmpty()) {
      for (String listId : allListIds) {
        response = trelloListService.archiveListById(listId);
        assertStatusCode(response, 200);
      }
    } else {
      logInfo("No lists to clean.");
    }
  }

  private List<String> getAllListIds(String boardId) {
    if (boardId != null) {
      Response response = trelloListService.getAllListsOnBoard(boardId);
      return response.jsonPath().getList("id");
    } else {
      logInfo("Board ID is null, cannot fetch lists.");
      return new ArrayList<>();
    }
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

  @AfterMethod(alwaysRun = true)
  public void resetTestSuccessFlag() {
    isTestSuccess = false; // Always reset for the next test
  }
  /*@AfterClass
  public void saveBoardId() {
    if (boardId != null) {
      LogUtil.getInstance().logInfo("Saving created board ID to configuration: " + boardId);
      objConfig.setBoardId(boardId);
    }
  }*/

}
