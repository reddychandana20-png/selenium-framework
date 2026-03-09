package com.chandana.tests.api.client;
import com.chandana.framework.constants.ApiConstants;
import com.chandana.tests.api.util.ApiResponse;
import com.chandana.tests.api.dto.UserResponseDTO;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetUserClient {

   public ApiResponse<UserResponseDTO> getUsers() {

    Response response = given()
            .header(ApiConstants.API_KEY_HEADER, ApiConstants.API_KEY_VALUE)
            .contentType(ContentType.JSON)
            .when()
            .get(ApiConstants.USERS_ENDPOINT);

    UserResponseDTO dto = response.as(UserResponseDTO.class);

    return new ApiResponse<>(response, dto);
}
}