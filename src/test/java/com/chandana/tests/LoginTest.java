package com.chandana.tests;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.chandana.action.LoginAction;
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
        loginAction.clickSignIn().click();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Via Benefits | Sign In");
     
    }
    
    @Test 
    public void DashboardVerification(){
        loginAction.clickSignIn().click();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Via Benefits | Sign In");
    }

     @Test(groups = {"regression"})
    public void NotificationVerification(){
        loginAction.clickSignIn().click();
        // Add assertions in test layer, using TestNG asserts as needed
         Assert.assertEquals(driver().getTitle(), "Via Benefits | Sign In");
     
    }

   

}

