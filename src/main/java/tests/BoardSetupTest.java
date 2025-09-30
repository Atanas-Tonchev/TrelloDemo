package tests;

import config.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import service.TrelloBoardService;
import util.LogUtil;

public class BoardSetupTest extends BaseTest {
  private TrelloBoardService boardService;
  private String boardId;
  private String newBoardName;

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    LogUtil.getInstance().logInfo("Setting up BoardSetupTest...");
    try {
      newBoardName = "Automation_Test_Board";
      String apiKey = objConfig.getApiKey();
      String authToken = objConfig.getAuthToken();
      String baseUrl = objConfig.getBaseUrl();
      boardService = new TrelloBoardService(apiKey, authToken, baseUrl);
    } catch (Exception e) {
      LogUtil.getInstance().logException("Exception in BoardSetupTest setup: ", e);
      throw e;
    }
  }

  @Test(priority = 1)
  public void testCreateBoard() {
    try {
      // Check if a board with the same name already exists
      LogUtil.getInstance().logInfo("Checking for existing boards with name: " + newBoardName);
      Response existingBoardsResponse = boardService.getAllBoards();
      assertEquals(existingBoardsResponse.statusCode(), 200);
      boolean boardExists = existingBoardsResponse.jsonPath().getList("name").contains(newBoardName);
      if (boardExists) {
        LogUtil.getInstance().logInfo("Board with name " + newBoardName + " already exists. Deleting it.");
        String existingBoardId = existingBoardsResponse.jsonPath()
            .getString("find { it.name == '" + newBoardName + "' }.id");
        boardService.deleteBoardById(existingBoardId);
        LogUtil.getInstance().logInfo("Deleted existing board with ID: " + existingBoardId);
      }

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
  public void testGetBoard() {
    Response response = boardService.getBoardById(boardId);
    assertEquals(response.statusCode(), 200);
    assertEquals(response.jsonPath().getString("id"), boardId);
  }

 /* @AfterClass
  public void cleanUp() {
    if (boardId != null) {
      LogUtil.getInstance().logInfo("Deleting board with ID: " + boardId);
      boardService.deleteBoardById(boardId);
      LogUtil.getInstance().logInfo("Board deleted successfully!");
    }

  }*/

}
