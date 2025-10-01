package tests;

import configs.BaseTest;
import io.restassured.response.Response;
import models.TrelloBoardModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static constants.AutomationConstants.BOARD_NAME;
import static constants.AutomationConstants.DEFAULT_LIST_NAMES;
import static util.LogUtil.logException;
import static util.LogUtil.logInfo;

import services.TrelloBoardServiceImpl;
import services.TrelloListServiceImpl;
import util.BoardValidationUtil;
import util.ListsValidationUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class to set up and verify Trello board operations.
 */

public class BoardSetupTest extends BaseTest {

  private TrelloBoardModel trelloBoardModel;
  private TrelloBoardServiceImpl boardService;
  private TrelloListServiceImpl trelloListService;
  private BoardValidationUtil boardValidationUtil;
  private ListsValidationUtil listValidationUtil;
  private LinkedList<String> listNames;
  private boolean isTestSuccess;
  private Response response;

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    logInfo("Setting up BoardSetupTest...");
    try {
      trelloBoardModel = new TrelloBoardModel(BOARD_NAME);
      trelloBoardModel.setId(null);
      boardValidationUtil = new BoardValidationUtil();
      listValidationUtil = new ListsValidationUtil();
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
      logInfo("Starting test: testCreateBoard");
      response = boardService.createBoard(trelloBoardModel.getName());

      // Verify board creation
      boardValidationUtil.assertStatusCode(response, 200);

      // Extract and set the board ID, only after successful creation
      trelloBoardModel.setId(boardService.getBoardIdByCreationResponse(response));

      // Validate rest of the response
      boardValidationUtil.validateBoard(response, trelloBoardModel.getName(), trelloBoardModel.getId());

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
  public void testCreateListsOnBoard() {
    logInfo("Starting test: testCreateListsOnBoard");
    String boardId = trelloBoardModel.getId();
    try {
      // Check if there are any lists exist on the board and clean them ,
      // because by default Trello creates 3 lists on new board witch are not the same as our default lists
      cleanAllBoardList(boardId);

      // Create lists on the board
      List<String> createdListIds = new ArrayList<>();
      for (String listName : listNames) {
        response = trelloListService.createList(boardId, listName);

        // Verify list creation
        listValidationUtil.assertStatusCode(response, 200);

        // Extract list ID from the response, only after successful creation
        String listId = trelloListService.getListIdByName(listName,boardId);

        // Validate response body
        listValidationUtil.validateListResponseBody(response, listName, listId);
        createdListIds.add(listId);
      }

      // Validate all created lists belong to the correct board
      listValidationUtil.validateAllLists(createdListIds, listNames, boardId, trelloListService);

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


  // Region Helper Methods

  public void cleanAllBoardList(String boardId) {
    logInfo("Cleaning all existing lists from board");
    List<String> allListIds = getAllListIds(boardId);
    if (allListIds != null && !allListIds.isEmpty()) {
      for (String listId : allListIds) {
        response = trelloListService.archiveListById(listId);
        // Verify the list is archived successfully
        listValidationUtil.assertStatusCode(response, 200);
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

  @AfterMethod(alwaysRun = true)
  public void resetTestSuccessFlag() {
    isTestSuccess = false; // Always reset for the next test
  }

}
