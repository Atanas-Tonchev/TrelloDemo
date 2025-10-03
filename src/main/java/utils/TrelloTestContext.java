package utils;

import models.TrelloBoardModel;

public class TrelloTestContext {
  private static volatile TrelloTestContext instance;
  private TrelloBoardModel trelloBoardModel;
  private BoardValidation boardValidation;
  private ListsValidation listValidation;
  private CardValidation cardValidation;

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

  public CardValidation getCardValidation() {
    return cardValidation;
  }

  public void setCardValidation(CardValidation cardValidation) {
    this.cardValidation = cardValidation;
  }
}
