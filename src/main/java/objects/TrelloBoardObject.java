package objects;

public class TrelloBoardObject {
  private final String name;
  private String id;

  public TrelloBoardObject(String name) {
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
