package configs;

import static util.LogUtil.logException;

/**
 * Base test class to initialize configuration.
 */

public class BaseTest {
  public static AppConfig objConfig;

  static {
    try {
      ReadConfigFiles configReader = ReadConfigFiles.getInstance();
      objConfig = configReader.readConfiguration();
    } catch (Exception e) {
      logException("Failed to read configuration: ", e);
    }
  }
}

