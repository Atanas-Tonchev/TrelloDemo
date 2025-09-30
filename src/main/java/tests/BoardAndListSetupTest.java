package tests;

import config.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import service.TrelloBoardService;
import service.TrelloListService;
import util.LogUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class to set up and verify Trello board operations.
 */

public class BoardAndListSetupTest extends BaseTest {

  private TrelloBoardService boardService;
  private TrelloListService trelloListService;
  private String boardId;
  private String newBoardName;
  private LinkedList<String> listNames;

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    LogUtil.getInstance().logInfo("Setting up BoardAndListSetupTest...");
    try {
      boardId = null;
      listNames = getAllListNames();
      newBoardName = "Interview Board";
      String apiKey = objConfig.getApiKey();
      String authToken = objConfig.getAuthToken();
      String baseUrl = objConfig.getBaseUrl();
      boardService = new TrelloBoardService(apiKey, authToken, baseUrl);
      trelloListService = new TrelloListService(apiKey, authToken, baseUrl);
      LogUtil.getInstance().logInfo("BoardAndListSetupTest setup completed.");
    } catch (Exception e) {
      LogUtil.getInstance().logException("Exception in BoardAndListSetupTest setup: ", e);
      throw e;
    }
  }


  @Test(priority = 1)
  public void testCreateBoard() {
    try {
      // Create a new board
      LogUtil.getInstance().logInfo("Creating board with name: " + newBoardName);
      boardId = boardService.createBoardAndReturnId("Automation_Test_Board");

      // Verify board creation
      assertNotNull(boardId, "Board ID should not be null");
      LogUtil.getInstance().logInfo("Board created successfully!");
    } catch (Exception e) {
      LogUtil.getInstance().logException("Exception in testCreateBoard: ", e);
      throw e;
    }

  }


  @Test(priority = 2, dependsOnMethods = "testCreateBoard")
  public void createListsOnBoard() {
    try {
      // Check if there are any lists exist on the board and clean them
      cleanAllBoardList();

      // Create lists on the board
      LogUtil.getInstance().logInfo("Creating lists on board.");
      List<String> createdListIds = new ArrayList<>();
      for (String listName : listNames) {
        String listId = trelloListService.createListOnBoardAndReturnId(boardId, listName);
        assertNotNull(listId, "List ID should not be null for list: " + listName);
        createdListIds.add(listId);
      }

      // Verify lists creation
      assertEquals(createdListIds.size(), listNames.size(), "Number of created lists should match");
      LogUtil.getInstance().logInfo("All lists created successfully on the board!");

      // Verify all created lists are belong to the newly created board
      List<String> fetchedListIds = getAllListIds(boardId);
      assertEqualsNoOrder(fetchedListIds.toArray(), createdListIds.toArray(), "Fetched list IDs should match created list IDs");
      LogUtil.getInstance().logInfo("Verified all lists belong to the board with ID: " + boardId);

    } catch (Exception e) {
      LogUtil.getInstance().logException("Exception in createListsOnBoard: ", e);
      throw e;
    }
  }

  // Region Helper Methods

  public void cleanAllBoardList() {
    LogUtil.getInstance().logInfo("Cleaning all existing lists from board");
    List<String> allListIds = getAllListIds(boardId);
    if (allListIds != null && !allListIds.isEmpty()) {
      for (String listId : allListIds) {
        trelloListService.archiveListById(listId);
      }
    } else {
      LogUtil.getInstance().logInfo("No lists to clean.");
    }
  }

  private List<String> getAllListIds(String boardId) {
    if (boardId != null) {
      Response response = trelloListService.getAllListsOnBoard(boardId);
      return response.jsonPath().getList("id");
    } else {
      LogUtil.getInstance().logInfo("Board ID is null, cannot fetch lists.");
      return new ArrayList<>();
    }
  }

  private LinkedList<String> getAllListNames() {
    LinkedList<String> listNames = new LinkedList<>();
    listNames.add("Done");
    listNames.add("In Progress");
    listNames.add("To Do");
    return listNames;
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
