package constants;

import java.util.LinkedList;

public class AutomationConstants {

  public static final String BOARD_NAME = "Interview Board";
  public static final LinkedList<String> DEFAULT_LIST_NAMES =
      new LinkedList<>() {
        {
          add("Done");
          add("In Progress");
          add("To Do");
        }
      };


}
