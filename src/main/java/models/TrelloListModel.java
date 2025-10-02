package models;

public class TrelloListModel {
  private String name;
  private String id;

  public TrelloListModel() {
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
}
