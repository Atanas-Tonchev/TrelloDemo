package tests;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import service.TrelloBoardService;
import service.TrelloCardService;

import static config.BaseTest.objConfig;

public class CardWorkflowTest {
  private TrelloBoardService boardService;
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

    // Execute prerequisite operations
    executePrerequisites(boardName);



  }

  private void executePrerequisites(String boardName) {
    // Search board by name, if not found create a new one
    boardId = boardService.getBoardIdByName(boardName);

    // If board does not exist, create it
    if (boardId == null) boardId = boardService.createBoardAndReturnId(boardName);



// 2. Вземаме default лист от борда

    Response boardResponse = boardService.getBoardById(boardId);

    listId = boardResponse.jsonPath().getList("idLists").get(0).toString();
  }


  /*@Test(priority = 1)

  public void testCreateCard() {

    cardId = cardService.createCardAndReturnId(listId, "Automation_Test_Card");

    assertNotNull(cardId, "Card ID should not be null");

  }


  @Test(priority = 2, dependsOnMethods = "testCreateCard")

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
