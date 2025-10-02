package services;

import clients.TrelloCardClient;
import clients.TrelloCheckListClient;
import io.restassured.response.Response;

public class TrelloCheckListServiceImpl implements ITrelloCheckListService {
  private final TrelloCheckListClient client;

  public TrelloCheckListServiceImpl(String apiKey, String authToken, String baseUrl) {
    this.client = new TrelloCheckListClient(apiKey, authToken, baseUrl);
  }

  @Override
  public Response createChecklistOnCard(String id, String checklistName) {
    return client.createChecklistOnCard(id, checklistName);
  }

  @Override
  public Response addItemToChecklist(String checklistId, String itemName) {
    return client.addItemToChecklist(checklistId, itemName);
  }

  @Override
  public Response getChecklistById(String checklistId) {
    return client.getChecklistById(checklistId);
  }

  @Override
  public Response updateChecklistItemState(String cardId, String checklistId, String itemId, String state) {
    return client.updateChecklistItemState(cardId, checklistId, itemId, state);
  }

  @Override
  public Response getAllChecklistsOnCard(String cardId) {
    return client.getAllChecklistsOnCard(cardId);
  }
}
