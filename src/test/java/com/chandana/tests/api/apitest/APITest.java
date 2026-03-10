package com.chandana.tests.api.apitest;
import com.chandana.tests.api.client.GetUserClient;
import com.chandana.tests.api.dto.IssueTypeDTO;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
@Test (groups = {"api"})
public class APITest extends BaseTestAPI {

    private final GetUserClient userClient = new GetUserClient();

    @Test
    public void verifyGetUsersResponseBody() {
        // Call API from client layer with desired endpoint
        String endpoint = "/rest/api/3/project";
        System.out.println("Calling API with endpoint: " + endpoint);
        io.restassured.response.Response response = userClient.get(endpoint);

        // Validate the response is not null and status is 200
        Assert.assertNotNull(response, "Response should not be null");
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Print response for validation
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
        //deserialize response to DTO and validate expected fields
        List<IssueTypeDTO> issueTypes = response.jsonPath().getList("", IssueTypeDTO.class);
        Assert.assertNotNull(issueTypes, "Issue types list should not be null");    

        // Assert.assertNotNull(firstUser.getFirst_name(), "First name should not be null");        
        
    // Assert.assertNotNull(firstUser.getLast_name(), "Last name should not be null");
    // Validate expected user exists
    // boolean expectedUserFound = users.stream()
    //         .anyMatch(user -> "michael.lawson@reqres.in".equalsIgnoreCase(user.getEmail()));
    // Assert.assertTrue(expectedUserFound, "Expected user email not found in response");
    }
}