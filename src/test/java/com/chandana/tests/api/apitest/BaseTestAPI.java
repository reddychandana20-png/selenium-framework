package com.chandana.tests.api.apitest;

import com.chandana.framework.constants.ApiConstants;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseTestAPI {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
    }
}