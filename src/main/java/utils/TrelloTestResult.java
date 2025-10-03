package utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class TrelloTestResult {

  private static volatile TrelloTestResult instance;
  private final LinkedHashMap<String, String> testResult = new LinkedHashMap<>();

  private TrelloTestResult() {
  }

  public static TrelloTestResult getInstance() {
    if (instance == null) {
      synchronized (TrelloTestResult.class) {
        if (instance == null) {
          instance = new TrelloTestResult();
        }
      }
    }
    return instance;
  }

  public LinkedHashMap<String, String> getResultsByClass(String className) {
    LinkedHashMap<String, String> results = new LinkedHashMap<>();
    for (Map.Entry<String, String> entry : testResult.entrySet()) {
      // Assumes testName format: ClassName.methodName
      if (entry.getKey().startsWith(className + ".")) {
        results.put(entry.getKey(), entry.getValue());
      }
    }
    return results;
  }

  public void putResult(String testName, String result) {
    testResult.put(testName, result);
  }

}
