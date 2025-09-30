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
import static util.LogUtil.logInfo;

public class CardWorkflowTest {

  private TrelloBoardService boardService;
  private TrelloListService trelloListService;
  private TrelloCardService cardService;
  private String boardId;
  private String listId;
  private String cardId;
  private String boardName;

  @BeforeClass
  public void setUp() {
    boardName = "Interview Board";
    String apiKey = objConfig.getApiKey();
    String authToken = objConfig.getAuthToken();
    String baseUrl = objConfig.getBaseUrl();
    boardService = new TrelloBoardService(apiKey, authToken, baseUrl);
    cardService = new TrelloCardService(apiKey, authToken, baseUrl);
    trelloListService = new TrelloListService(apiKey, authToken, baseUrl);

    // Execute prerequisite operations
    executePrerequisites(boardName);


  }

  private void executePrerequisites(String boardName) {
    LinkedList<String> expectedListNames = trelloListService.getAllExpectedListNames();
    // Search board by name, if not found create a new one
    boardId = boardService.getBoardIdByName(boardName);
    // If board exists, get all lists on the board
    if (boardId != null) {
      Map<String, String> mapOfLists = manageBoardLists(boardId, expectedListNames);
    }

    // If board does not exist, create it
    if (boardId == null) boardId = boardService.createBoardAndReturnId(boardName);

// 2. Вземаме default лист от борда

    Response boardResponse = boardService.getBoardById(boardId);

    listId = boardResponse.jsonPath().getList("idLists").get(0).toString();
  }

  private Map<String, String> manageBoardLists(String boardId, List<String> expectedListNames) {
    Map<String, String> mapOfLists = new HashMap<>();
    List<String> actualList = getAllListByEntity(boardId, "name");

    if (!actualList.isEmpty()) {
      // Update existing lists if names differ
      for (String expectedName : expectedListNames) {
        actualList.stream()
            .filter(actualName -> !expectedListNames.contains(actualName))
            .findFirst()
            .ifPresent(actualName -> {
              trelloListService.updateListName(trelloListService.getListIdByName(actualName, boardId), expectedName);
              logInfo("Updated list from:"+ actualName + "  name to: " + expectedName);
            });
        break;
      }

      // Populate map with list names and their IDs
      getAllListByEntity(boardId, "id").forEach(listId -> {
        Response response = trelloListService.getListById(listId);
        String listName = response.jsonPath().getString("name");
        mapOfLists.put(listName, listId);
      });
    } else {
      // Create new lists if none exist
      expectedListNames.forEach(listName -> {
        String listId = trelloListService.createListOnBoardAndReturnId(boardId, listName);
        assertNotNull(listId, "List ID should not be null for list: " + listName);
        mapOfLists.put(listName, listId);
      });
    }
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


  @Test(priority = 1)

  public void testCreateCard() {

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
}
