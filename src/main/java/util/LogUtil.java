package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
  private static LogUtil instance;
  private final Logger logger;

  private LogUtil() {
    logger = LogManager.getLogger(LogUtil.class);
  }

  public static synchronized LogUtil getInstance() {
    if (instance == null) {
      instance = new LogUtil();
    }
    return instance;
  }

  public void logInfo(String message) {
    logger.info(message);
  }

  public void logError(String message) {
    logger.error(message);
  }

  public void logException(String message, Exception exception) {
    logger.error(message, exception);
  }
}
