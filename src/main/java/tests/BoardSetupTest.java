package tests;

import configs.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static constants.AutomationConstants.DEFAULT_LIST_NAMES;
import static constants.AutomationConstants.TRELLO_API_TESTING;
import static utils.LogUtil.logException;
import static utils.LogUtil.logInfo;

import services.TrelloBoardServiceImpl;
import services.TrelloListServiceImpl;
import utils.ApiListener;
import utils.TrelloTestContext;
import utils.TrelloTestResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class to set up and verify Trello board operations.
 */
@Listeners({ApiListener.class})
public class BoardSetupTest extends BaseTest {

  private TrelloBoardServiceImpl boardService;
  private TrelloListServiceImpl trelloListService;
  private LinkedList<String> listNames;
  private boolean isTestSuccess;
  private Response response;
  private TrelloTestContext trelloTestContext;

  /**
   * Initializes test context, services, and configuration before running any tests in this class.
   * Sets up required Trello services and test data.
   * Logs setup progress and handles initialization exceptions.
   */
  @BeforeClass(alwaysRun = true)
  public void setUp() {
    logInfo("Setting up BoardSetupTest...");
    try {
      trelloTestContext = TrelloTestContext.getInstance();
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


  @Test(groups = {TRELLO_API_TESTING}, priority = 1)
  public void testCreateBoard() {
    logInfo("Starting test: testCreateBoard");
    try {
      // Create a new board
      String boardName = trelloTestContext.getTrelloBoardModel().getName();
      response = boardService.createBoard(boardName);

      // Verify board creation
      trelloTestContext.getBoardValidation().assertSuccessResponseArray(response);

      // Extract and set the board ID, only after successful creation
      String boardId = boardService.getBoardIdByName(boardName);
      trelloTestContext.getBoardValidation().assertIdNotNull(boardId);
      trelloTestContext.getTrelloBoardModel().setId(boardId);

      // Validate response body
      trelloTestContext.getBoardValidation().assertResponseBody(response, boardName);

      // mark test as success
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in testCreateBoard: ", e);
      throw e;
    } finally {
      if (!isTestSuccess) {
        TrelloTestResult.getInstance().putResult("BoardSetupTest.testCreateBoard", "FAILED");
      } else {
        TrelloTestResult.getInstance().putResult("BoardSetupTest.testCreateBoard", "PASSED");
      }
    }

  }

  @Test(groups = {TRELLO_API_TESTING},
      priority = 2,
      dependsOnMethods = "testCreateBoard")
  public void testCreateListsOnBoard() {
    logInfo("Starting test: testCreateListsOnBoard");
    String boardId = trelloTestContext.getTrelloBoardModel().getId();
    try {
      // Check if there are any lists exist on the board and clean them ,
      // because by default Trello creates 3 lists on new board witch are not the same as our default lists
      cleanAllBoardList(boardId);

      // Create lists on the board
      List<String> createdListIds = new ArrayList<>();
      for (String listName : listNames) {
        response = trelloListService.createList(boardId, listName);

        // Verify list creation
        trelloTestContext.getListValidation().assertSuccessResponseArray(response);

        // Extract list ID from the response, only after successful creation
        String listId = trelloListService.getListIdByName(listName, boardId);

        // Validate response body
        trelloTestContext.getListValidation().assertResponseBody(response, listName);
        createdListIds.add(listId);
      }

      // Validate all created lists belong to the correct board
      trelloTestContext.getListValidation().validateAllLists(createdListIds, listNames, boardId, trelloListService);

      // mark test as success
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in createListsOnBoard: ", e);
      throw e;
    } finally {
      if (!isTestSuccess) {
        TrelloTestResult.getInstance().putResult("BoardSetupTest.testCreateListsOnBoard", "FAILED");
      } else {
        TrelloTestResult.getInstance().putResult("BoardSetupTest.testCreateListsOnBoard", "PASSED");
      }
    }
  }

  // Region Helper Methods

  /**
   * Archives all existing lists on the specified Trello board.
   * Iterates through all lists on the board and archives each one.
   *
   * @param boardId the ID of the Trello board whose lists will be archived
   */
  private void cleanAllBoardList(String boardId) {
    logInfo("Cleaning all existing lists from board");
    List<String> allListIds = getAllListIds(boardId);
    if (allListIds != null && !allListIds.isEmpty()) {
      for (String listId : allListIds) {
        response = trelloListService.archiveListById(listId);
        // Verify the list is archived successfully
        trelloTestContext.getListValidation().assertStatusCode(response, 200);
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

  /**
   * Resets the test success flag to false after each test method.
   * Ensures that the flag is cleared before the next test execution.
   */
  @AfterMethod(alwaysRun = true)
  public void resetTestSuccessFlag() {
    isTestSuccess = false; // Always reset for the next test
  }

}
