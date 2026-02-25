package com.chandana.framework.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.chandana.framework.driver.DriverFactory;
import com.chandana.framework.reporting.ExtentManager;
import com.chandana.framework.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestNGListener implements ITestListener {

    private static final ThreadLocal<ExtentTest> TL = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.createTest(result.getMethod().getMethodName());
        TL.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TL.get().pass("PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = TL.get();
        test.fail(result.getThrowable());

        try {
            WebDriver driver = DriverFactory.getDriver(); // ✅ now exists
            if (driver != null) {
                String screenshotPath = ScreenshotUtil.takeScreenshot(driver, result.getMethod().getMethodName());
                if (screenshotPath != null) {
                    test.fail("Screenshot of failure",
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }
            }
        } catch (Exception e) {
            test.warning("Screenshot capture failed: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TL.get().skip("SKIPPED");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
    }
}