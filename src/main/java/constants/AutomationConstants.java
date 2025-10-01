package constants;

import java.util.LinkedList;

public class AutomationConstants {

  public static final String CARD_NAME = "Prepare Interview Task";
  public static final String BOARD_NAME = "Interview Board";
  public static final String UPDATED_CARD_NAME = "Updated Interview Task";
  public static final String LIST_NAME_TODO = "To Do";
  public static final String LIST_NAME_IN_PROGRESS = "In Progress";
  public static final String LIST_NAME_DONE = "Done";
  public static final String CARD_DESCRIPTION = "This is a task to prepare for the interview.";
  public static final String UPDATED_CARD_DESCRIPTION = "This is an updated task description.";
  public static final LinkedList<String> DEFAULT_LIST_NAMES =
      new LinkedList<>() {
        {
          add(LIST_NAME_DONE);
          add(LIST_NAME_IN_PROGRESS);
          add(LIST_NAME_TODO);
        }
      };


}
