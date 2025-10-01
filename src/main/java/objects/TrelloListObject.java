package objects;

public class TrelloListObject {
  private String id;
  private String name;
  private String idBoard;
  private boolean closed;

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

  public String getIdBoard() {
    return idBoard;
  }

  public void setIdBoard(String idBoard) {
    this.idBoard = idBoard;
  }

  public boolean isClosed() {
    return closed;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }
}
