package example;

import customlogging.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

  public static void main(String[] args) {
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    Logger logger = new Logger(Logger.LogLevel.DEBUG, "app.log");


    logger.debug("This is a debug message.");
    logger.info("This is an info message.");
    logger.warn("This is a warning message.");
    logger.error("This is an error message.");


    }
  }
