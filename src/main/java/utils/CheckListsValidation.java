package utils;

import io.restassured.response.Response;

import static utils.LogUtil.logInfo;

public class CheckListsValidation extends ValidationUtil {

  @Override
  public void assertSuccessResponseArray(Response response) {
    super.assertSuccessResponseArray(response);
    logInfo("Validation success.");
  }

  @Override
  public void assertSuccessResponseMap(Response response) {
    super.assertSuccessResponseMap(response);
    logInfo("Validation success.");
  }

}
