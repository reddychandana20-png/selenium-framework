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
    // Call API from client layer
    IssueTypeDTO[] issueTypes = userClient.getIssueTypes();

    // Validate the response is not null and not empty
    Assert.assertNotNull(issueTypes, "Issue types array should not be null");
    Assert.assertTrue(issueTypes.length > 0, "Issue types array should not be empty");

    // Validate first issue type fields (basic check)
    IssueTypeDTO firstType = issueTypes[0];
    Assert.assertNotNull(firstType.getId(), "Issue type ID should not be null");
    Assert.assertNotNull(firstType.getName(), "Issue type name should not be null");
    Assert.assertNotNull(firstType.getDescription(), "Issue type description should not be null");
    // Optionally print for debug
    System.out.println("First Issue Type: " + firstType.getName());
    // Assert.assertNotNull(firstUser.getLast_name(), "Last name should not be null");
    // Validate expected user exists
    // boolean expectedUserFound = users.stream()
    //         .anyMatch(user -> "michael.lawson@reqres.in".equalsIgnoreCase(user.getEmail()));
    // Assert.assertTrue(expectedUserFound, "Expected user email not found in response");
    }
}