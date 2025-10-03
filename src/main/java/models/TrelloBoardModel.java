package models;

public class TrelloBoardModel {

  private final String name;
  private String id;

  public TrelloBoardModel(String name) {
    this.name = name;
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
}
