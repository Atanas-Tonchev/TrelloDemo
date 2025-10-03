package utils;

import models.TrelloBoardModel;
import models.TrelloCardModel;
import models.TrelloCheckListModel;
import org.testng.IClassListener;
import org.testng.ISuiteListener;
import org.testng.ITestClass;

import java.util.LinkedHashMap;
import java.util.Map;

import static constants.AutomationConstants.BOARD_NAME;
import static utils.LogUtil.logInfo;

public class ApiListener implements IClassListener, ISuiteListener {

  @Override
  public void onStart(org.testng.ISuite iSuite) {
    logInfo("=== === Executing test suit: " + iSuite.getName() + " === ===");
  }

  @Override
  public void onFinish(org.testng.ISuite iSuite) {
    logInfo("=== === " + iSuite.getName() + " Test Suite execution completed. === ===");
  }

  @Override
  public void onBeforeClass(ITestClass testClass) {
    String className = testClass.getRealClass().getSimpleName();
    logInfo("=== Starting class: " + className + " ===");
    logInfo(">>>>>> Executing prerequisite: initialize Trello All models and validations <<<<<<");
    TrelloTestContext trelloTestContext = TrelloTestContext.getInstance();
    TrelloBoardModel trelloBoardModel = new TrelloBoardModel(BOARD_NAME);
    trelloBoardModel.setId(null);
    trelloTestContext.setTrelloBoardModel(trelloBoardModel);
    BoardValidation boardValidation = new BoardValidation();
    trelloTestContext.setBoardValidation(boardValidation);
    ListsValidation listValidation = new ListsValidation();
    trelloTestContext.setListValidation(listValidation);
    CardValidation cardValidation = new CardValidation();
    TrelloCardModel trelloCardModel = new TrelloCardModel();

    // Execute prerequisite operations according to the class
    if (className.equalsIgnoreCase("CardWorkflowTest")) {
      logInfo(">>>> Executing prerequisite: initialize Trello Card model and validations <<<<");
      trelloTestContext.setCardValidation(cardValidation);
      trelloTestContext.setTrelloCardModel(trelloCardModel);


    } else if (className.equalsIgnoreCase("ChecklistTest")) {
      logInfo(">> Executing prerequisite: initialize Trello Card model and validations");
      trelloTestContext.setTrelloCardModel(trelloCardModel);
      trelloTestContext.setCardValidation(cardValidation);
      TrelloCheckListModel trelloCheckListModel = new TrelloCheckListModel();
      trelloTestContext.setTrelloCheckListModel(trelloCheckListModel);
      CheckListsValidation checkListValidation = new CheckListsValidation();
      trelloTestContext.setCheckListValidation(checkListValidation);
    }
  }

  @Override
  public void onAfterClass(ITestClass testClass) {
    String className = testClass.getRealClass().getSimpleName();
    LinkedHashMap<String, String> testResult = TrelloTestResult.getInstance().getResultsByClass(className);
    if (testResult != null) {
      logInfo("Test results for " + className + ":");
      for (Map.Entry<String, String> entry : testResult.entrySet()) {
        logInfo(" - " + entry.getKey() + ": " + entry.getValue());
      }
    } else {
      logInfo("No test results for " + className);
    }
    logInfo("=== End of class: " + className + " ===");
  }

}
