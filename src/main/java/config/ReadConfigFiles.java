package config;

import common.AppConfig;
import util.CommonUtil;
import util.LogUtil;

import java.util.Properties;

public class ReadConfigFiles {
  public AppConfig readConfiguration() throws Exception {
    AppConfig config = new AppConfig(); // Instantiate AppConfig
    try {
      Properties properties =
          CommonUtil.loadProperties(ReadConfigFiles.class, "app.config.properties");
      String apiKey = properties.getProperty("ApiKey");
      String authToken = properties.getProperty("AuthToken");
      config.setApiKey(apiKey);
      config.setAuthToken(authToken);
    } catch (Exception e) {
      LogUtil.logError("Exception in reading config file: " + e.getMessage());
      throw e;
    }
    return config;
  }
}
