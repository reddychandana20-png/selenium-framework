package com.chandana.tests.api.apitest;
import com.chandana.tests.api.client.GetUserClient;
import com.chandana.tests.api.dto.UserResponseDTO;
import com.chandana.tests.api.dto.UserDataDTO;
import org.testng.Assert;
import com.chandana.tests.api.util.ApiResponse;
import org.testng.annotations.Test;

import java.util.List;
@Test (groups = {"api"})
public class APITest extends BaseTestAPI {

    private final GetUserClient userClient = new GetUserClient();

    @Test
    public void verifyGetUsersResponseBody() {

        // Call API from client layer
       ApiResponse<UserResponseDTO> response = userClient.getUsers();

        // Validate top level fields
        Assert.assertEquals(response.getResponse().getStatusCode(), 200, "Status code mismatch");
        Assert.assertEquals(response.getBody().getPage(), 2, "Page number mismatch");
        Assert.assertTrue(response.getBody().getPer_page() > 0, "Per page value should be greater than zero");
        Assert.assertTrue(response.getBody().getTotal() > 0, "Total users should be greater than zero");
        Assert.assertTrue(response.getBody().getTotal_pages() > 0, "Total pages should be greater than zero");

        // Validate user list
       
        List<UserDataDTO> users = response.getBody().getData();

        Assert.assertNotNull(users, "User list should not be null");
        Assert.assertFalse(users.isEmpty(), "User list should not be empty");

        // Validate first user details
        UserDataDTO firstUser = users.get(0);

        Assert.assertTrue(firstUser.getId() > 0, "User ID should be greater than zero");
        Assert.assertNotNull(firstUser.getEmail(), "User email should not be null");
        Assert.assertTrue(firstUser.getEmail().contains("@"), "Invalid email format");
        Assert.assertNotNull(firstUser.getFirst_name(), "First name should not be null");
        Assert.assertNotNull(firstUser.getLast_name(), "Last name should not be null");

        // Validate expected user exists
        boolean expectedUserFound = users.stream()
                .anyMatch(user -> "michael.lawson@reqres.in".equalsIgnoreCase(user.getEmail()));

        Assert.assertTrue(expectedUserFound, "Expected user email not found in response");
    }
}