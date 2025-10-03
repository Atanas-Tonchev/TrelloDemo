package utils;

import models.TrelloBoardModel;

public class TrelloTestContext {
  private static volatile TrelloTestContext instance;
  private TrelloBoardModel trelloBoardModel;
  private BoardValidation boardValidation;
  private ListsValidation listValidation;

  public static TrelloTestContext getInstance() {
    if (instance == null) {
      synchronized (TrelloTestContext.class) {
        if (instance == null) {
          instance = new TrelloTestContext();
        }
      }
    }
    return instance;
  }

  public TrelloBoardModel getTrelloBoardModel() {
    return trelloBoardModel;
  }

  public void setTrelloBoardModel(TrelloBoardModel trelloBoardModel) {
    this.trelloBoardModel = trelloBoardModel;
  }

  public BoardValidation getBoardValidation() {
    return boardValidation;
  }

  public void setBoardValidation(BoardValidation boardValidation) {
    this.boardValidation = boardValidation;
  }

  public ListsValidation getListValidation() {
    return listValidation;
  }

  public void setListValidation(ListsValidation listValidation) {
    this.listValidation = listValidation;
  }
}
