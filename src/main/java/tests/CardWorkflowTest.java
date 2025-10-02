package tests;

import io.restassured.response.Response;
import models.TrelloBoardModel;
import models.TrelloCardModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.TrelloBoardServiceImpl;
import services.TrelloCardServiceImpl;
import services.TrelloListServiceImpl;
import util.BoardValidationUtil;
import util.CardValidationUtil;
import util.ListsValidationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static configs.BaseTest.objConfig;
import static constants.AutomationConstants.BOARD_NAME;
import static constants.AutomationConstants.CARD_NAME;
import static constants.AutomationConstants.DEFAULT_LIST_NAMES;
import static constants.AutomationConstants.LIST_NAME_TODO;
import static org.testng.Assert.assertNotNull;
import static util.LogUtil.logException;
import static util.LogUtil.logInfo;

public class CardWorkflowTest {

  private TrelloBoardServiceImpl boardService;
  private TrelloListServiceImpl trelloListService;
  private TrelloCardServiceImpl cardService;
  private ListsValidationUtil listsValidationUtil;
  private CardValidationUtil cardValidationUtil;
  private BoardValidationUtil boardValidationUtil;
  private TrelloBoardModel trelloBoardModel;
  private TrelloCardModel trelloCardModel;
  Map<String, String> mapOfLists;
  private boolean isTestSuccess;

  @BeforeClass
  public void setUp() {
    try {
      logInfo("Setting up CardWorkflowTest...");
      String apiKey = objConfig.getApiKey();
      String authToken = objConfig.getAuthToken();
      String baseUrl = objConfig.getBaseUrl();
      boardService = new TrelloBoardServiceImpl(apiKey, authToken, baseUrl);
      cardService = new TrelloCardServiceImpl(apiKey, authToken, baseUrl);
      trelloListService = new TrelloListServiceImpl(apiKey, authToken, baseUrl);
      listsValidationUtil = new ListsValidationUtil();
      cardValidationUtil = new CardValidationUtil();
      boardValidationUtil = new BoardValidationUtil();
      trelloBoardModel = new TrelloBoardModel(BOARD_NAME);
      // Execute prerequisite operations
      executePrerequisites();
      isTestSuccess = false;
      logInfo("CardWorkflowTest setup completed.");
    } catch (Exception e) {
      logException("Exception in setUp: " + e.getMessage(), e);
    }
  }


  @Test(priority = 1)

  public void testCreateTitledCard() {
    logInfo("Starting test: testCreateTitledCard");
    try {
      // Create a card in the "To Do" list
      String listId = mapOfLists.get(LIST_NAME_TODO);

      // Verify list ID is not null, only then proceed
      assertNotNull(listId, "'To Do' list ID should not be null");

      Response response = cardService.createCard(listId, CARD_NAME);
      // Initialize card model for further tests
      trelloCardModel = new TrelloCardModel(CARD_NAME, listId);
      trelloCardModel.setId(cardService.getCardIdByCreationResponse(response));

      // Validate response status code and body
      cardValidationUtil.assertResponseSuccess(response);
      cardValidationUtil.assertResponseBody(response, CARD_NAME);

      // Mark test as successful
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in testCreateTitledCard: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        logInfo("Test FAILED");
      } else {
        logInfo("Test PASSED");
      }
    }
  }


  @Test(priority = 2, dependsOnMethods = "testCreateTitledCard")
  public void testMoveCard() {
    logInfo("Starting test: testMoveCard");
    try {
      // Move the card through all lists it hasn't been in yet
      Set<String> visitedLists = new HashSet<>();
      visitedLists.add(trelloCardModel.getIdList()); // Start with the initial list

      for (Map.Entry<String, String> entry : mapOfLists.entrySet()) {
        String targetListId = entry.getValue();

        // Skip if already visited
        if (visitedLists.contains(targetListId)) continue;

        // Move the card
        Response moveResponse = cardService.moveCardToList(trelloCardModel.getId(), targetListId);

        // Validate move response
        cardValidationUtil.assertResponseSuccess(moveResponse);

        // Verify the card is in the expected list by fetching it and validating its 'idList'
        cardValidationUtil.validateCardInListAfterMoving(cardService, trelloCardModel, targetListId, entry.getKey());

        // Mark this list as visited
        visitedLists.add(targetListId);
      }
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in testMoveCard: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        logInfo("Test FAILED");
      } else {
        logInfo("Test PASSED");
      }
    }
  }

  @Test(priority = 3, dependsOnMethods = {"testCreateTitledCard","testMoveCard"})
  public void testAddCommentToCard() {
    logInfo("Starting test: testAddCommentToCard");
    String commentText = "This is a test comment.";
    try {
      // Add a comment to the card
      Response commentResponse = cardService.createCardComment(trelloCardModel.getId(), commentText);
      cardValidationUtil.assertResponseSuccess(commentResponse);

      // Retrieve the list of actions for the card
      Response actionsResponse = cardService.getCardActionsById(trelloCardModel.getId());
      cardValidationUtil.assertResponseSuccess(actionsResponse);

      // Validate the comment fields in the actions response
      cardValidationUtil.validateCommentFields(commentResponse, commentText);
      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in testAddCommentToCard: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        logInfo("Test FAILED");
      } else {
        logInfo("Test PASSED");
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
        boardId = boardService.createBoardAndReturnId(trelloBoardModel);
        // Validate board creation
        boardValidationUtil.assertIdNotNull(boardId);
      }

      mapOfLists = manageBoardLists(boardId);
    } catch (Exception e) {
      logException("Exception in executePrerequisites: " + e.getMessage(), e);
    }
  }


  private Map<String, String> manageBoardLists(String boardId) {
    logInfo("Managing board lists.");
    Map<String, String> mapOfLists = new LinkedHashMap<>();
    List<String> actualList = getAllListByEntity(boardId, "name");

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
                listsValidationUtil.assertIdNotNull(actualId);
                Response response = trelloListService.updateListName(actualId, expectedName);
                listsValidationUtil.assertStatusCode(response, 200);
                logInfo("Updated list from: " + actualName + " to: " + expectedName);
              });
        }
      }
    } else {
      logInfo("No existing lists found, creating default lists.");
      // Create new lists if none exist
      DEFAULT_LIST_NAMES.forEach(listName -> {
        Response response = trelloListService.createList(boardId, listName);
        listsValidationUtil.assertStatusCode(response, 200);
      });
    }

    // Repopulate map with all lists and their IDs
    getAllListByEntity(boardId, "id").forEach(listId -> {
      Response response = trelloListService.getListById(listId);
      listsValidationUtil.assertStatusCode(response, 200);
      String listName = response.jsonPath().getString("name");
      mapOfLists.put(listName, listId);
    });
    return mapOfLists;
  }


  private List<String> getAllListByEntity(String boardId, String entityName) {
    if (boardId != null) {
      Response response = trelloListService.getAllListsOnBoard(boardId);
      listsValidationUtil.assertStatusCode(response, 200);
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

}
