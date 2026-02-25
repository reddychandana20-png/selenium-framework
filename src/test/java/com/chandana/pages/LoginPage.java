package com.chandana.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private final WebDriver driver;

    // Page layer locator: keep all xpaths/selectors here
    private final By signInLocator = By.xpath("//a[contains(text(),'Sign In')][1]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Intent-driven method: wait for the Sign In link to be clickable and click it
    public WebElement clickSignIn() {
        //driver.findElement(signInLocator).click();
        return driver.findElement(signInLocator);
        
    }

}
