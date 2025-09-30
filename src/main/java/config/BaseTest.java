package config;

import common.AppConfig;
import util.LogUtil;

public class BaseTest {
  public static AppConfig objConfig;

  static {
    try {
      ReadConfigFiles configReader = ReadConfigFiles.getInstance();
      objConfig = configReader.readConfiguration();
    } catch (Exception e) {
      LogUtil.getInstance().logException("Failed to read configuration: ", e);
    }
  }
}

