package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for logging messages and exceptions using Log4j2.
 */

public class LogUtil {

  private static final Logger logger = LogManager.getLogger(LogUtil.class);

  public static void logInfo(String message) {
    logger.info(message);
  }

  public static void logException(String message, Exception exception) {
    logger.error(message, exception);
  }
}
