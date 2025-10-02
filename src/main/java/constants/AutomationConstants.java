package constants;

import java.util.LinkedList;

public class AutomationConstants {

  public static final String CARD_NAME = "Prepare Interview Task";
  public static final String BOARD_NAME = "Interview Board";
  public static final String UPDATED_CARD_NAME = "Updated Interview Task";
  public static final String LIST_NAME_TODO = "To Do";
  public static final String LIST_NAME_IN_PROGRESS = "In Progress";
  public static final String LIST_NAME_DONE = "Done";
  public static final String CHECKLIST_PREPARATION_STEP = "Preparation Steps";
  public static final String CHECKLIST_REVIEW_STEPS = "Review Steps";
  private static final String CHECKLIST_ITEM_1 = "Define the job requirements";
  private static final String CHECKLIST_ITEM_2 = "Prepare interview questions";
  private static final String CHECKLIST_ITEM_3 = "Schedule the interview";
  public static final LinkedList<String> DEFAULT_LIST_NAMES =
      new LinkedList<>() {
        {
          add(LIST_NAME_DONE);
          add(LIST_NAME_IN_PROGRESS);
          add(LIST_NAME_TODO);
        }
      };
  public static final LinkedList<String> DEFAULT_CHECKLIST_NAMES =
      new LinkedList<>() {
        {
          add(CHECKLIST_REVIEW_STEPS);
          add(CHECKLIST_PREPARATION_STEP);
        }
      };
  public static final LinkedList<String> DEFAULT_CHECKLIST_ITEMS =
      new LinkedList<>() {
        {
          add(CHECKLIST_ITEM_3);
          add(CHECKLIST_ITEM_2);
          add(CHECKLIST_ITEM_1);
        }
      };


}
