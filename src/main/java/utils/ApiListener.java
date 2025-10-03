package utils;

import models.TrelloBoardModel;
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
    // Execute prerequisite operations according to the class
    if (className.equals("BoardSetupTest")) {
      System.out.println(">> Executing prerequisite: initialize Trello board model and validations");
      TrelloTestContext trelloTestContext = TrelloTestContext.getInstance();
      TrelloBoardModel trelloBoardModel = new TrelloBoardModel(BOARD_NAME);
      trelloBoardModel.setId(null);
      trelloTestContext.setTrelloBoardModel(trelloBoardModel);
      BoardValidation boardValidation = new BoardValidation();
      trelloTestContext.setBoardValidation(boardValidation);
      ListsValidation listValidation = new ListsValidation();
      trelloTestContext.setListValidation(listValidation);
    } else if (className.equals("OrderApiTest")) {
      System.out.println(">> Пререквизит: създаване на тестова поръчка");
      // Тук може да извикаш API за създаване на order
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
    logInfo("=== End of class:" + className + " ===");
  }

}
