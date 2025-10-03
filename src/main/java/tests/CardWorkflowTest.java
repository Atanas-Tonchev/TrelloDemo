package tests;

import configs.BaseTest;
import io.restassured.response.Response;
import models.TrelloCardModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.TrelloBoardServiceImpl;
import services.TrelloCardServiceImpl;
import services.TrelloListServiceImpl;
import utils.ApiListener;
import utils.TrelloTestContext;
import utils.TrelloTestResult;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static constants.AutomationConstants.BOARD_NAME;
import static constants.AutomationConstants.CARD_NAME;
import static constants.AutomationConstants.LIST_NAME_TODO;
import static constants.AutomationConstants.TRELLO_API_TESTING;
import static constants.AutomationConstants.TRELLO_COMMENT_TEXT;
import static org.testng.Assert.assertNotNull;
import static utils.LogUtil.logException;
import static utils.LogUtil.logInfo;

@Listeners({ApiListener.class})
public class CardWorkflowTest extends BaseTest {

  private TrelloTestContext trelloTestContext;
  private TrelloBoardServiceImpl boardService;
  private TrelloListServiceImpl trelloListService;
  private TrelloCardServiceImpl cardService;
  Map<String, String> mapOfLists;
  private boolean isTestSuccess;

  @BeforeClass
  public void setUp() {
    try {
      logInfo("Setting up CardWorkflowTest...");
      trelloTestContext = TrelloTestContext.getInstance();
      String apiKey = objConfig.getApiKey();
      String authToken = objConfig.getAuthToken();
      String baseUrl = objConfig.getBaseUrl();
      boardService = new TrelloBoardServiceImpl(apiKey, authToken, baseUrl);
      cardService = new TrelloCardServiceImpl(apiKey, authToken, baseUrl);
      trelloListService = new TrelloListServiceImpl(apiKey, authToken, baseUrl);
      // Execute prerequisite operations
      executePrerequisites();
      isTestSuccess = false;
      logInfo("CardWorkflowTest setup completed.");
    } catch (Exception e) {
      logException("Exception in setUp: " + e.getMessage(), e);
    }
  }

  @BeforeMethod
  public void beforeEachTest() {
    // Refresh test context before each test
    trelloTestContext = TrelloTestContext.getInstance();
  }


  @Test(groups = {TRELLO_API_TESTING}, priority = 1)

  public void testCreateTitledCard() {
    logInfo("Starting test: testCreateTitledCard");
    try {
      // Create a card in the "To Do" list
      String listId = mapOfLists.get(LIST_NAME_TODO);

      // Verify list ID is not null, only then proceed
      assertNotNull(listId, "'To Do' list ID should not be null");

      // Create the card
      Response response = cardService.createCard(listId, CARD_NAME);
      // Initialize card model for further tests
      trelloTestContext.setTrelloCardModel(new TrelloCardModel(CARD_NAME, listId));
      trelloTestContext.getTrelloCardModel().setId(cardService.getCardIdByCreationResponse(response));

      // Validate response status code and body
      trelloTestContext.getCardValidation().assertSuccessResponseArray(response);
      trelloTestContext.getCardValidation().assertResponseBody(response, CARD_NAME);

      // Mark test as successful
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in testCreateTitledCard: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        TrelloTestResult.getInstance().putResult("CardWorkflowTest.testCreateTitledCard", "FAILED");
      } else {
        TrelloTestResult.getInstance().putResult("CardWorkflowTest.testCreateTitledCard", "PASSED");
      }
    }
  }


  @Test(groups = {TRELLO_API_TESTING},
      priority = 2,
      dependsOnMethods = "testCreateTitledCard")
  public void testMoveCard() {
    logInfo("Starting test: testMoveCard");
    try {
      // Move the card through all lists it hasn't been in yet
      Set<String> visitedLists = new HashSet<>();
      visitedLists.add(trelloTestContext.getTrelloCardModel().getIdList()); // Start with the initial list

      for (Map.Entry<String, String> entry : mapOfLists.entrySet()) {
        String targetListId = entry.getValue();

        // Skip if already visited
        if (visitedLists.contains(targetListId)) continue;

        // Move the card
        Response moveResponse = cardService.moveCardToList(trelloTestContext.getTrelloCardModel().getId(), targetListId);

        // Validate move response
        trelloTestContext.getCardValidation().assertSuccessResponseArray(moveResponse);

        // Verify the card is in the expected list by fetching it and validating its 'idList'
        trelloTestContext.getCardValidation().validateCardInListAfterMoving(cardService, trelloTestContext.getTrelloCardModel(), targetListId, entry.getKey());

        // Mark this list as visited
        visitedLists.add(targetListId);
      }
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in testMoveCard: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        TrelloTestResult.getInstance().putResult("CardWorkflowTest.testMoveCard", "FAILED");
      } else {
        TrelloTestResult.getInstance().putResult("CardWorkflowTest.testMoveCard", "PASSED");
      }
    }
  }

  @Test(groups = {TRELLO_API_TESTING},
      priority = 3,
      dependsOnMethods = {"testCreateTitledCard", "testMoveCard"})
  public void testAddCommentToCard() {
    logInfo("Starting test: testAddCommentToCard");
    try {
      // Add a comment to the card
      Response commentResponse = cardService.createCardComment(trelloTestContext.getTrelloCardModel().getId(), TRELLO_COMMENT_TEXT);
      trelloTestContext.getCardValidation().assertSuccessResponseArray(commentResponse);

      // Retrieve the list of actions for the card
      Response actionsResponse = cardService.getCardActionsById(trelloTestContext.getTrelloCardModel().getId());
      trelloTestContext.getCardValidation().assertSuccessResponseMap(actionsResponse);

      // Validate the comment fields in the actions response
      trelloTestContext.getCardValidation().validateCommentFields(commentResponse, TRELLO_COMMENT_TEXT);
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in testAddCommentToCard: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        TrelloTestResult.getInstance().putResult("CardWorkflowTest.testAddCommentToCard", "FAILED");
      } else {
        TrelloTestResult.getInstance().putResult("CardWorkflowTest.testAddCommentToCard", "PASSED");
      }
    }
  }

  // Region: Helper Methods

  private void executePrerequisites() {
    logInfo("Executing prerequisite operations.");
    try {
      // Search board by name, if not found create a new one with all expected lists
      String boardId = boardService.getBoardIdByName(BOARD_NAME);

      // Create board if not found
      if (boardId == null) {
        logInfo("Board not found, creating a new board.");
        boardId = boardService.createBoardAndReturnId(trelloTestContext.getTrelloBoardModel().getName());
        // Validate board creation
        trelloTestContext.getBoardValidation().assertIdNotNull(boardId);
      }

      // Managing board lists. Create missing lists and get map of list names to IDs
      mapOfLists = trelloTestContext.getListValidation().manageBoardLists(boardId, trelloListService);
    } catch (Exception e) {
      logException("Exception in executePrerequisites: " + e.getMessage(), e);
    }
  }

  // End Region

  @AfterMethod(alwaysRun = true)
  public void resetTestSuccessFlag() {
    isTestSuccess = false; // Always reset for the next test
  }

}
