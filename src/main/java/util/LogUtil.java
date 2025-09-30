package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LogUtil is a singleton utility class for logging messages and exceptions using Log4j2.
 * It provides methods to log informational messages, error messages, and exceptions.
 */

public class LogUtil {
  private static Logger logger;

  private LogUtil() {
    logger = LogManager.getLogger(LogUtil.class);
  }

  public static void logInfo(String message) {
    logger.info(message);
  }

  public static void logError(String message) {
    logger.error(message);
  }

  public static void logException(String message, Exception exception) {
    logger.error(message, exception);
  }
}
