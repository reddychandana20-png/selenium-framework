
package com.chandana.tests;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
public class SampleTest{


    public static void main(String[] args){
        ChromeDriver driver = new ChromeDriver();
        driver.navigate().to("https://www.facebook.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        driver.findElement(By.xpath("//span[contains(text(),'Create new account')]")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(),'Get started on Facebook')]")).isDisplayed(), "Sign Up page is not displayed");
        driver.findElement(By.xpath("//label[normalize-space()='First name']/preceding:: input")).sendKeys("Chandanatest");;
        driver.findElement(By.xpath("//label[text()='Last name']/preceding-sibling:: input")).sendKeys("Reddy");
        driver.findElement(By.xpath("//span[normalize-space()='Month']/following :: div[1]")).click();
        driver.findElement(By.xpath("//div[text()='March']")).click();
        driver.findElement(By.xpath("//span[normalize-space()='Day']/following::*[name() = 'svg'][1]")).click();
        driver.findElement(By.xpath("//div[text()='15']")).click();
        driver.quit();
    }
}