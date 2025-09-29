package config;

import common.AppConfig;
import customlogging.Logger;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {
  public static Logger logger = new Logger(Logger.LogLevel.DEBUG, "app.log");
  public static Map<String, String> apiendpoints = new HashMap<>();
  public static AppConfig objConfig;

  static {
    try {
      ReadConfigFiles configReader = new ReadConfigFiles();
      objConfig = configReader.readConfiguration();
    } catch (Exception e) {
      logger.error("Failed to read configuration: " + e.getMessage());
    }
  }

}
