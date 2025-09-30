package config;

import common.AppConfig;
import util.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {
  public static final Map<String, String> apiendpoints = new HashMap<>();
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

