package com.chandana.framework.utils;

import com.chandana.framework.config.ConfigReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class JiraTestUpdateService {

    /**
     * Update Jira issue with test execution status
     */
    public static void updateTestStatus(String issueKey, String testName, String status, String comment) {
        if (!ConfigReader.isJiraUpdateEnabled()) {
            System.out.println("Jira update is disabled");
            return;
        }

        try {
            String jiraUrl = "https://parusonly.atlassian.net/rest/api/3/issue/" + issueKey;
            String testResult = String.format("Test: %s | Status: %s | Comment: %s", testName, status, comment);

            // Update Jira issue with test result via HTTP PUT
            String updatePayload = "{\n" +
                    "  \"fields\": {\n" +
                    "    \"comment\": {\n" +
                    "      \"add\": [\n" +
                    "        {\n" +
                    "          \"body\": \"" + testResult + "\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

            URL url = new URL(jiraUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            
            // Add Basic Authentication
            String username = ConfigReader.getUsername();
            String password = ConfigReader.getPassword();
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = updatePayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 204) {
                System.out.println("✓ Jira issue " + issueKey + " updated successfully with test result");
            } else {
                System.out.println("✗ Failed to update Jira issue. Status code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("✗ Error updating Jira: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update test status with only PASS or FAIL
     */
    public static void updateTestStatus(String issueKey, String testName, String status) {
        updateTestStatus(issueKey, testName, status, "Test executed via automation");
    }
}
