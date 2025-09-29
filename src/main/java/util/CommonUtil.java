package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class CommonUtil {
  /**
   * Load properties file from the same directory where jar is located
   *
   * @param cls          Class from which the method is called
   * @param propertyFile Property file name
   * @return Properties object
   * @throws Exception Exception
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
