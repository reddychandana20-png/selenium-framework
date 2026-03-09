package com.chandana.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;

    // Page layer locator: keep all xpaths/selectors here
    private final By signInLocator = By.xpath("//a[contains(text(),'Sign In')][1]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }


    // Intent-driven method: wait for the Sign In link to be clickable and click it
    public WebElement clickSignIn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(signInLocator)).click();
        //driver.findElement(signInLocator).click();
        return driver.findElement(signInLocator);
      
     
    }
}
