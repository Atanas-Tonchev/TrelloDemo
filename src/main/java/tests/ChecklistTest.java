package tests;

import configs.BaseTest;
import io.restassured.response.Response;
import models.TrelloBoardModel;
import models.TrelloCardModel;
import models.TrelloCheckListModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.TrelloBoardServiceImpl;
import services.TrelloCardServiceImpl;
import services.TrelloCheckListServiceImpl;
import services.TrelloListServiceImpl;
import utils.BoardValidation;
import utils.CardValidation;
import utils.CheckListsValidation;
import utils.ListsValidation;
import utils.TrelloTestContext;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static constants.AutomationConstants.BOARD_NAME;
import static constants.AutomationConstants.CARD_NAME;
import static constants.AutomationConstants.CHECKLIST_PREPARATION_STEP;
import static constants.AutomationConstants.CHECKLIST_REVIEW_STEPS;
import static constants.AutomationConstants.DEFAULT_CHECKLIST_ITEMS;
import static constants.AutomationConstants.DEFAULT_CHECKLIST_NAMES;
import static constants.AutomationConstants.DEFAULT_LIST_NAMES;
import static constants.AutomationConstants.LIST_NAME_TODO;
import static constants.AutomationConstants.TRELLO_API_TESTING;
import static org.testng.Assert.assertNotNull;
import static utils.LogUtil.logException;
import static utils.LogUtil.logInfo;

public class ChecklistTest extends BaseTest {

  private TrelloCheckListServiceImpl trelloCheckListService;
  private TrelloBoardServiceImpl boardService;
  private TrelloListServiceImpl trelloListService;
  private TrelloCardServiceImpl cardService;
  private ListsValidation listsValidation;
  private CardValidation cardValidation;
  private BoardValidation boardValidation;
  private TrelloBoardModel trelloBoardModel;
  private TrelloCardModel trelloCardModel;
  private TrelloCheckListModel trelloCheckListModel;
  private CheckListsValidation checkListValidationUtil;
  private TrelloTestContext trelloTestContext;
  private boolean isTestSuccess;

  @BeforeClass
  public void setUp() {
    try {
      logInfo("Setting up ChecklistTest...");
      trelloTestContext = TrelloTestContext.getInstance();
      String apiKey = objConfig.getApiKey();
      String authToken = objConfig.getAuthToken();
      String baseUrl = objConfig.getBaseUrl();
      boardService = new TrelloBoardServiceImpl(apiKey, authToken, baseUrl);
      cardService = new TrelloCardServiceImpl(apiKey, authToken, baseUrl);
      trelloCheckListService = new TrelloCheckListServiceImpl(apiKey, authToken, baseUrl);
      trelloListService = new TrelloListServiceImpl(apiKey, authToken, baseUrl);
      listsValidation = new ListsValidation();
      cardValidation = new CardValidation();
      boardValidation = new BoardValidation();
      trelloBoardModel = new TrelloBoardModel(BOARD_NAME);
      trelloCheckListModel = new TrelloCheckListModel();
      trelloCardModel = new TrelloCardModel();
      checkListValidationUtil = new CheckListsValidation();
      // Execute prerequisite operations
      executePrerequisites();
      isTestSuccess = false;
      logInfo("ChecklistTest setup completed.");
    } catch (Exception e) {
      logException("Exception in setUp: " + e.getMessage(), e);
    }
  }

  @Test(groups = {TRELLO_API_TESTING}, priority = 1)
  public void addChecklistWithItemsToCard() {
    logInfo("Starting test: addChecklistWithItemsToCard");
    try {
      // Add checklists to the card
      for (String checkListName : DEFAULT_CHECKLIST_NAMES) {
        // Create checklist on the card
        Response checklistResponse = trelloCheckListService.createChecklistOnCard(trelloCardModel.getId(), checkListName);
        checkListValidationUtil.assertSuccessResponseArray(checklistResponse);
        String checkListId = checklistResponse.jsonPath().getString("id");
        assertNotNull(checkListId, "Checklist ID should not be null after creation");

        // Set checklist details in the model
        trelloCheckListModel.setCheckListIds(checkListName, checkListId);
        assertNotNull(checkListId, "Checklist ID should not be null");

        // Add items to the checklist
        for (String itemName : DEFAULT_CHECKLIST_ITEMS) {
          Response itemResponse = trelloCheckListService.addItemToChecklist(checkListId, itemName);
          checkListValidationUtil.assertSuccessResponseArray(itemResponse);
        }
      }

      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in addChecklistWithItemsToCard: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        logInfo("Test FAILED");
      } else {
        logInfo("Test PASSED");
      }
    }
  }

  @Test(groups = {TRELLO_API_TESTING},
      priority = 2,
      dependsOnMethods = "addChecklistWithItemsToCard")
  public void manegeChecklistsOnCard() {
    logInfo("Starting test: manageChecklistsOnCard");
    try {
      // Verify checklist items and mark them as complete based on checklist type
      for (String checkListName : DEFAULT_CHECKLIST_NAMES) {
        String checklistId = trelloCheckListModel.getCheckListIdByName(checkListName);
        assertNotNull(checklistId, "Checklist ID should not be null before adding items");

        // Fetch the checklist details
        Response getChecklistResponse = trelloCheckListService.getChecklistById(checklistId);
        checkListValidationUtil.assertSuccessResponseArray(getChecklistResponse);

        // Update checklist items based on checklist type
        List<Map<String, Object>> checkItems = getChecklistResponse.jsonPath().getList("checkItems");
        if (CHECKLIST_PREPARATION_STEP.equals(checkListName)) {
          for (Map<String, Object> item : checkItems) {
            String itemId = (String) item.get("id");
            Response completeItemResponse = trelloCheckListService.updateChecklistItemState(trelloCardModel.getId(), checklistId, itemId, "true");
            checkListValidationUtil.assertSuccessResponseArray(completeItemResponse);
          }
        } else if (CHECKLIST_REVIEW_STEPS.equals(checkListName)) {
          for (int i = 0; i < checkItems.size() - 1; i++) {
            String itemId = (String) checkItems.get(i).get("id");
            Response completeItemResponse = trelloCheckListService.updateChecklistItemState(trelloCardModel.getId(), checklistId, itemId, "true");
            checkListValidationUtil.assertSuccessResponseArray(completeItemResponse);
          }
        }

        // Verify all checklist items are present
        List<String> actualItems = getChecklistResponse.jsonPath().getList("checkItems.name");
        for (String expectedItem : DEFAULT_CHECKLIST_ITEMS) {
          if (!actualItems.contains(expectedItem)) {
            throw new AssertionError("Checklist item not found: " + expectedItem);
          }
        }
      }

      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in manageChecklistsOnCard: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        logInfo("Test FAILED");
      } else {
        logInfo("Test PASSED");
      }
    }
  }


  @Test(groups = {TRELLO_API_TESTING},
      priority = 3,
      dependsOnMethods = {"addChecklistWithItemsToCard","manegeChecklistsOnCard"})
  public void retrieveAndValidateCheckListsWithItems() {
    logInfo("Starting test: retrieveAndValidateCheckListsWithItems");
    try {

      // Retrieve all checklists for the card
      Response allChecklistsResponse = trelloCheckListService.getAllChecklistsOnCard(trelloCardModel.getId());
      checkListValidationUtil.assertSuccessResponseMap(allChecklistsResponse);

      List<Map<String, Object>> allChecklists = allChecklistsResponse.jsonPath().getList("$");

      // Validate both checklists exist
      Set<String> checklistNames = new HashSet<>();
      for (Map<String, Object> checklist : allChecklists) {
        checklistNames.add(String.valueOf(checklist.get("name")));
      }
      if (!checklistNames.containsAll(DEFAULT_CHECKLIST_NAMES)) {
        throw new AssertionError("Both checklists not found on the card");
      }
      logInfo("Both checklists are present on the card.");

      // Validate "Preparation Steps" items are all complete
      for (Map<String, Object> checklist : allChecklists) {
        String name = String.valueOf(checklist.get("name"));
        List<Map<String, Object>> checkItems = (List<Map<String, Object>>) checklist.get("checkItems");
        if (CHECKLIST_PREPARATION_STEP.equals(name)) {
          for (Map<String, Object> item : checkItems) {
            String state = String.valueOf(item.get("state"));
            if (!"complete".equals(state)) {
              throw new AssertionError("Not all '" + CHECKLIST_PREPARATION_STEP + "' items are complete");
            }
          }
        }

        // Validate "Review Steps" has at least one incomplete item
        if (CHECKLIST_REVIEW_STEPS.equals(name)) {
          boolean hasIncomplete = checkItems.stream().anyMatch(item -> !"complete".equals(item.get("state")));
          if (!hasIncomplete) {
            throw new AssertionError("'" + CHECKLIST_REVIEW_STEPS + "' does not have any incomplete items");
          }
        }

        // Validate all checklist items are present
        List<String> actualItems = checkItems.stream().map(i -> String.valueOf(i.get("name"))).toList();
        for (String expectedItem : DEFAULT_CHECKLIST_ITEMS) {
          if (!actualItems.contains(expectedItem)) {
            throw new AssertionError("Checklist item not found: " + expectedItem);
          }
        }
        logInfo("All expected items are present in checklist: " + name);
      }
      logInfo("All items in '" + CHECKLIST_PREPARATION_STEP + "' are complete.");
      logInfo("'" + CHECKLIST_REVIEW_STEPS + "' has at least one incomplete item.");

      isTestSuccess = true;
    } catch (Exception e) {
      logException("Exception in retrieveAndValidateCheckListsWithItems: " + e.getMessage(), e);
    } finally {
      if (!isTestSuccess) {
        logInfo("Test FAILED");
      } else {
        logInfo("Test PASSED");
      }
    }
  }

  // Region Helper Methods
  private void executePrerequisites() {
    logInfo("Executing prerequisite operations.");
    try {
      // Search board by name, if not found create a new one with all expected lists
      String boardId = boardService.getBoardIdByName(BOARD_NAME);
      if (boardId == null) {
        logInfo("Board not found, creating a new board.");
        boardId = boardService.createBoardAndReturnId(trelloBoardModel);
        boardValidation.assertIdNotNull(boardId);
      }

      // Managing board lists. Create missing lists and get map of list names to IDs
      Map<String, String> mapOfLists = trelloTestContext.getListValidation().manageBoardLists(boardId, trelloListService);

      // Check if card exists and create if not
      String existingCardId = cardService.getCardIdByName(CARD_NAME, boardId);
      if (existingCardId != null) {
        logInfo("Card already exists with ID: " + existingCardId);
        trelloCardModel.setId(existingCardId);
        return;
      }

      // Create a card for checklist tests
      logInfo("Creating a new card for checklist tests.");
      String listId = mapOfLists.get(LIST_NAME_TODO);
      assertNotNull(listId, "'To Do' list ID should not be null");
      Response response = cardService.createCard(listId, CARD_NAME);
      trelloCardModel = new TrelloCardModel(CARD_NAME, listId);
      trelloCardModel.setId(cardService.getCardIdByCreationResponse(response));
      cardValidation.assertSuccessResponseArray(response);
      cardValidation.assertResponseBody(response, CARD_NAME);
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
