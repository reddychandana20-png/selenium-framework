package com.chandana.tests.api.client;
import com.chandana.framework.constants.ApiConstants;
import com.chandana.framework.config.ConfigReader;
import com.chandana.tests.api.dto.IssueTypeDTO;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetUserClient {
    public Response get(String endpoint) {
        // Construct full URL by combining BASE_URL with endpoint
        String fullUrl = ApiConstants.BASE_URL + endpoint;
        System.out.println("Full URL: " + fullUrl);
        
        Response response = given()
                .auth().basic(ConfigReader.getUsername(), ConfigReader.getPassword())
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(fullUrl);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Content-Type: " + response.getContentType());
        System.out.println("Response Body: " + response.getBody().asString());
        return response;
    }
}