package com.chandana.tests.api.client;
import com.chandana.framework.constants.ApiConstants;
import com.chandana.tests.api.dto.IssueTypeDTO;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetUserClient {
        public IssueTypeDTO[] getIssueTypes() {
                Response response = given()
                                .baseUri(ApiConstants.BASE_URL)
                                .auth()
                                .basic(ApiConstants.USER_NAME, ApiConstants.PASSWORD)
                                .header("Accept", ContentType.JSON)
                                .contentType(ContentType.JSON)
                                .when()
                                .get(ApiConstants.USERS_ENDPOINT);
                System.out.println("Status Code: " + response.getStatusCode());
                System.out.println("Content-Type: " + response.getContentType());
                System.out.println("Response Body: " + response.getBody().asString());

                return response.as(IssueTypeDTO[].class);
        }
}