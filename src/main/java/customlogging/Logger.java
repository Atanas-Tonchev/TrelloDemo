package customlogging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

  public enum LogLevel {
    DEBUG, INFO, WARN, ERROR
  }

  private final LogLevel currentLogLevel;
  private final boolean logToFile;
  private String logFilePath;

  public Logger(LogLevel logLevel) {
    this.currentLogLevel = logLevel;
    this.logToFile = false;
  }

  public Logger(LogLevel logLevel, String logFilePath) {
    this.currentLogLevel = logLevel;
    this.logToFile = true;
    this.logFilePath = logFilePath;
  }

  private void log(LogLevel level, String message) {
    if (level.ordinal() >= currentLogLevel.ordinal()) {
      String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      String logMessage = String.format("%s [%s] %s", timestamp, level, message);
      System.out.println(logMessage); // Always log to console
      if (logToFile) {
        writeToFile(logMessage);
      }
    }
  }

  private void writeToFile(String message) {
    try (PrintWriter out = new PrintWriter(new FileWriter(logFilePath, true))) {
      out.println(message);
    } catch (IOException e) {
      System.err.println("Failed to write log to file: " + e.getMessage());
    }
  }

  public void debug(String message) {
    log(LogLevel.DEBUG, message);
  }

  public void info(String message) {
    log(LogLevel.INFO, message);
  }

  public void warn(String message) {
    log(LogLevel.WARN, message);
  }

  public void error(String message) {
    log(LogLevel.ERROR, message);
  }



}

