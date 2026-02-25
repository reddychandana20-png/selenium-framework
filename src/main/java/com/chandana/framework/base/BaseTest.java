package com.chandana.framework.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.chandana.framework.driver.DriverFactory;
import com.chandana.framework.config.ConfigReader;

public class BaseTest {

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    protected WebDriver driver() {
        return tlDriver.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = DriverFactory.createDriver();  
        tlDriver.set(driver);
         driver.get(ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            driver.quit();
        }
        tlDriver.remove();
    }
}