package services;

import clients.TrelloCheckListClient;
import io.restassured.response.Response;

import static util.LogUtil.logInfo;

public class TrelloCheckListServiceImpl implements ITrelloCheckListService {
  private final TrelloCheckListClient client;

  public TrelloCheckListServiceImpl(String apiKey, String authToken, String baseUrl) {
    this.client = new TrelloCheckListClient(apiKey, authToken, baseUrl);
  }

  @Override
  public Response createChecklistOnCard(String id, String checklistName) {
    logInfo("Adding checklist with name: " + checklistName + " on card ID: " + id);
    return client.createChecklistOnCard(id, checklistName);
  }

  @Override
  public Response addItemToChecklist(String checklistId, String itemName) {
    logInfo("Adding item with name: " + itemName + " to checklist ID: " + checklistId);
    return client.addItemToChecklist(checklistId, itemName);
  }

  @Override
  public Response getChecklistById(String checklistId) {
    logInfo("Retrieving checklist with ID: " + checklistId);
    return client.getChecklistById(checklistId);
  }

  @Override
  public Response updateChecklistItemState(String cardId, String checklistId, String itemId, String state) {
    logInfo("Updating state of item ID: " + itemId + " in checklist ID: " + checklistId + " on card ID: " + cardId + " to state: " + state);
    return client.updateChecklistItemState(cardId, checklistId, itemId, state);
  }

  @Override
  public Response getAllChecklistsOnCard(String cardId) {
    logInfo("Retrieving all checklists on card ID: " + cardId);
    return client.getAllChecklistsOnCard(cardId);
  }
}
