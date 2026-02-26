package com.chandana.tests;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.chandana.Action.LoginAction;
import com.chandana.framework.base.BaseTest;

public class LoginTest extends BaseTest{
     private LoginAction loginAction;
//summary for 
    @BeforeMethod(alwaysRun = true)
    
    public void setUp(){
         super.setUp();
         loginAction = new LoginAction(driver());
    }

    @Test (groups = {"smoke"})
    public void SignIn() {
        loginAction.clickSignIn();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Sign into your Account");
    }
    
    @Test 
    public void DashboardVerification(){
        loginAction.clickSignIn();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Sign into your Account");
    }

     @Test(groups = {"regression"})
    public void NotificationVerification(){
        loginAction.clickSignIn();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Sign into your Account");
    }

}

