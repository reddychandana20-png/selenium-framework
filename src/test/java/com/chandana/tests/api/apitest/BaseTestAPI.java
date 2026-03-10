package com.chandana.tests.api.apitest;

import com.chandana.framework.constants.ApiConstants;
import com.chandana.framework.config.ConfigReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseTestAPI {

    @BeforeClass
    public void setUp() {
        System.out.println("Setting up BaseTestAPI");
        // Credentials are loaded from config.properties
        String username = ConfigReader.getUsername();
        System.out.println("API Testing setup with user: " + username);
    }
}