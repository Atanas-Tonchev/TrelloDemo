package util;

import customlogging.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.http.HttpResponse;

public class LogUtil {
  public static Logger logger = new Logger(Logger.LogLevel.DEBUG, "app.log");

  public static String logException(Exception ex, String classAndFunction) {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw)) {
      ex.printStackTrace(pw);
    }
    String message = "Exception in " + classAndFunction + " function: " + sw;
    logger.error(message);
    return sw.toString();
  }

  public static String logException(Exception ex) {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw)) {
      ex.printStackTrace(pw);
    }
    String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
    String message = "Exception in " + methodName + " function: " + sw;
    logger.error(message);
    return sw.toString();
  }

  public static void logInfo(String message) {
    logger.info(message);
  }

  public static String buildErrorMessage(String operation, HttpResponse response) {
    return buildErrorMessage(operation, response.statusCode(), response.body());
  }

  public static String buildErrorMessage(HttpResponse response, int expectedStatusCode) {
    return "Expected status code: " + expectedStatusCode + " vs actual status code: " + response.statusCode() + ", " + response.body();
  }

  private static String buildErrorMessage(String operation, int statusCode, Object body) {
    return "Unsuccessful request for " + operation + " Response status code: " + statusCode + ", " + body;
  }

  public static String buildErrorMessage(String operation, String errorMessage) {
    return "Unsuccessful request for " + operation + " : " + errorMessage;
  }

  public static void logError(String message) {
    logger.error(message);
  }
}