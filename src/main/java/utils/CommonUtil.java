package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for common operations.
 */

public class CommonUtil {

  /**
   * Load properties from a file located in the same directory as the JAR file.
   *
   * @param cls          The class whose location is used to find the JAR directory.
   * @param propertyFile The name of the properties file to load.
   * @return Properties object containing the loaded properties.
   * @throws Exception if there is an error loading the properties file.
   */
  public static Properties loadProperties(Class cls, String propertyFile) throws Exception {
    InputStream inputStream = null;
    Properties prop = new Properties();

    try {
      String filePath = cls.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
      File file = new File(filePath);
      String jarDir = file.getParentFile().getPath();
      String propFileName = jarDir + File.separator + propertyFile;
      inputStream = new FileInputStream(propFileName);
      prop.load(inputStream);
    } catch (Exception ex) {
      System.out.println("Exception in PropertiesLoader.load()" + ex.getMessage());
      throw ex;
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }

    }

    return prop;
  }

}
