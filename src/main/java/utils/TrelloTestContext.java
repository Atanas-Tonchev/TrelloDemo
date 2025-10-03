package utils;

import models.TrelloBoardModel;
import models.TrelloCardModel;
import models.TrelloCheckListModel;

public class TrelloTestContext {
  private static volatile TrelloTestContext instance;
  private TrelloBoardModel trelloBoardModel;
  private BoardValidation boardValidation;
  private ListsValidation listValidation;
  private TrelloCardModel trelloCardModel;
  private CardValidation cardValidation;
  private TrelloCheckListModel trelloCheckListModel;
  private CheckListsValidation checkListValidation;


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

  public TrelloCheckListModel getTrelloCheckListModel() {
    return trelloCheckListModel;
  }

  public void setTrelloCheckListModel(TrelloCheckListModel trelloCheckListModel) {
    this.trelloCheckListModel = trelloCheckListModel;
  }

  public CheckListsValidation getCheckListValidation() {
    return checkListValidation;
  }

  public void setCheckListValidation(CheckListsValidation checkListValidation) {
    this.checkListValidation = checkListValidation;
  }

  public TrelloCardModel getTrelloCardModel() {
    return trelloCardModel;
  }

  public void setTrelloCardModel(TrelloCardModel trelloCardModel) {
    this.trelloCardModel = trelloCardModel;
  }
}
