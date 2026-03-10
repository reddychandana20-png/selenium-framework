package com.chandana.framework.constants;

public class ApiConstants {
    public static final String BASE_URL = "https://parusonly.atlassian.net";
    public static final String USERS_ENDPOINT = "/rest/api/3/issue/";
    public static final String GET_METHOD = "GET";
    public static final String CONTENT_TYPE = "application/json";
    public static final int TIMEOUT = 30;
    public static final String API_KEY_HEADER = "x-api-key";
    // Sensitive credentials (username, password, api_key) are now loaded from config.properties using ConfigReader.
}

