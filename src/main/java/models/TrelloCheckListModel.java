package models;

import java.util.HashMap;
import java.util.Map;

public class TrelloCheckListModel {
  private String id;
  private String name;
  private final Map<String,String> checkListIds = new HashMap<>();

  public TrelloCheckListModel() {
    // No-args constructor
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCheckListIdByName(Object key) {
    return checkListIds.get(key);
  }

  public void setCheckListIds(String checkListName, String checkListId) {
    this.checkListIds.put(checkListName, checkListId);
  }
}
