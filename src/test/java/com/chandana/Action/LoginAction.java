package com.chandana.Action;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.chandana.pages.LoginPage;

public class LoginAction {
    private final LoginPage loginPage;

    public LoginAction(WebDriver driver) {
        loginPage = new LoginPage(driver);
    }

    public WebElement clickSignIn() {
        return loginPage.clickSignIn();
    }

}
