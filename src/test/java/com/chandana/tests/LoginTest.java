package com.chandana.tests;


import com.chandana.framework.base.BaseTest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.chandana.Action.LoginAction;

public class LoginTest extends BaseTest{
     private LoginAction loginAction;

    @BeforeMethod(alwaysRun = true)
    
    public void setUp(){
         super.setUp();
         loginAction = new LoginAction(driver());
    }

    @Test
    public void SignIn() {
        loginAction.clickSignIn();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Sign In to Your Account");
    }
    
    @Test
    public void DashboardVerification(){
        loginAction.clickSignIn();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Sign In to Your Account");
    }

     @Test(groups = {"Regression"})
    public void NotificationVerification(){
        loginAction.clickSignIn();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Sign In  to Your Account");
    }

}

