package tests;

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

  @BeforeClass
  public void setUp() {
    String apiKey = objConfig.getApiKey();
    String authToken = objConfig.getAuthToken();
    String baseUrl = objConfig.getBaseUrl();
    boardService = new TrelloBoardService(apiKey, authToken, baseUrl);
    cardService = new TrelloCardService(apiKey, authToken, baseUrl);

    // Execute prerequisite operations
    executePrerequisites();



  }

  private void executePrerequisites() {
    // Create or get a board

    boardId = boardService.createBoardAndReturnId("Automation_Card_Board");


// 2. Вземаме default лист от борда

    Response boardResponse = boardService.getBoardById(boardId);

    listId = boardResponse.jsonPath().getList("idLists").get(0).toString();
  }


  @Test(priority = 1)

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

  }
}
