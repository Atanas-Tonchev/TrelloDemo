package config;

import common.AppConfig;
import util.CommonUtil;
import util.LogUtil;
import java.util.Properties;

import static util.LogUtil.logException;

/**
 * Singleton class to read configuration from properties file.
 */

public class ReadConfigFiles {
  private static ReadConfigFiles instance;

  private ReadConfigFiles() {
    // Private constructor to prevent instantiation
  }

  public static synchronized ReadConfigFiles getInstance() {
    if (instance == null) {
      instance = new ReadConfigFiles();
    }
    return instance;
  }

  public AppConfig readConfiguration() throws Exception {
    AppConfig config = new AppConfig(); // Instantiate AppConfig
    try {
      Properties properties =
          CommonUtil.loadProperties(ReadConfigFiles.class, "app.config.properties");
      String apiKey = properties.getProperty("ApiKey");
      String authToken = properties.getProperty("AuthToken");
      String baseUrl = properties.getProperty("BaseUrl");
      config.setApiKey(apiKey);
      config.setAuthToken(authToken);
      config.setBaseUrl(baseUrl);
    } catch (Exception e) {
      logException("Exception in reading config file: ", e);
      throw e;
    }
    return config;
  }
}
