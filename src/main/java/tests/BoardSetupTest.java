package tests;

import config.BaseTest;
import org.testng.annotations.Test;

public class BoardSetupTest extends BaseTest {

@Test (groups = {"BoardSetup"},
    priority = 1)
  public void boardSetupTest() {
    logger.info("Starting Board Setup Test");
    // Test implementation goes here
    logger.info("Completed Board Setup Test");
  }

}
