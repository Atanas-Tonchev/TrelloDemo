package tests;

import config.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static util.LogUtil.logException;
import static util.LogUtil.logInfo;

import service.TrelloBoardService;
import service.TrelloListService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class to set up and verify Trello board operations.
 */

public class BoardSetupTest extends BaseTest {

  private TrelloBoardService boardService;
  private TrelloListService trelloListService;
  private String boardId;
  private String newBoardName;
  private LinkedList<String> listNames;

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    logInfo("Setting up BoardSetupTest...");
    try {
      boardId = null;
      newBoardName = "Interview Board";
      String apiKey = objConfig.getApiKey();
      String authToken = objConfig.getAuthToken();
      String baseUrl = objConfig.getBaseUrl();
      boardService = new TrelloBoardService(apiKey, authToken, baseUrl);
      trelloListService = new TrelloListService(apiKey, authToken, baseUrl);
      listNames = trelloListService.getAllExpectedListNames();
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
      logInfo("Creating board with name: " + newBoardName);
      boardId = boardService.createBoardAndReturnId(newBoardName);

      // Verify board creation
      assertNotNull(boardId, "Board ID should not be null");
      logInfo("Board created successfully!");
    } catch (Exception e) {
      logException("Exception in testCreateBoard: ", e);
      throw e;
    }

  }


  @Test(priority = 2, dependsOnMethods = "testCreateBoard")
  public void createListsOnBoard() {
    try {
      // Check if there are any lists exist on the board and clean them
      cleanAllBoardList();

      // Create lists on the board
      logInfo("Creating lists on board.");
      List<String> createdListIds = new ArrayList<>();
      for (String listName : listNames) {
        String listId = trelloListService.createListOnBoardAndReturnId(boardId, listName);
        assertNotNull(listId, "List ID should not be null for list: " + listName);
        createdListIds.add(listId);
      }

      // Verify lists creation
      assertEquals(createdListIds.size(), listNames.size(), "Number of created lists should match");
      logInfo("All lists created successfully on the board!");

      // Verify all created lists are belong to the newly created board
      List<String> fetchedListIds = getAllListIds(boardId);
      assertEqualsNoOrder(fetchedListIds.toArray(), createdListIds.toArray(), "Fetched list IDs should match created list IDs");
      logInfo("Verified all lists belong to the board with ID: " + boardId);

    } catch (Exception e) {
      logException("Exception in createListsOnBoard: ", e);
      throw e;
    }
  }

  // Region Helper Methods

  public void cleanAllBoardList() {
    logInfo("Cleaning all existing lists from board");
    List<String> allListIds = getAllListIds(boardId);
    if (allListIds != null && !allListIds.isEmpty()) {
      for (String listId : allListIds) {
        trelloListService.archiveListById(listId);
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

  // End Region

  /*@AfterClass
  public void saveBoardId() {
    if (boardId != null) {
      LogUtil.getInstance().logInfo("Saving created board ID to configuration: " + boardId);
      objConfig.setBoardId(boardId);
    }
  }*/

}
