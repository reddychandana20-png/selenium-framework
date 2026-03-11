package com.chandana.framework.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.chandana.framework.annotations.JiraTestCase;
import com.chandana.framework.config.ConfigReader;
import com.chandana.framework.driver.DriverFactory;
import com.chandana.framework.reporting.ExtentManager;
import com.chandana.framework.utils.JiraTestUpdateService;
import com.chandana.framework.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.lang.reflect.Method;

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
        
        // Extract Jira test case ID from annotation
        String jiraTestCaseId = extractJiraTestCaseId(result);
        if (jiraTestCaseId != null) {
            String testName = result.getMethod().getMethodName();
            JiraTestUpdateService.updateTestStatus(jiraTestCaseId, testName, "PASS");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = TL.get();
        test.fail(result.getThrowable());

        try {
            WebDriver driver = DriverFactory.getDriver();
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
        
        // Extract Jira test case ID from annotation
        String jiraTestCaseId = extractJiraTestCaseId(result);
        if (jiraTestCaseId != null) {
            String testName = result.getMethod().getMethodName();
            String errorMsg = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test failed";
            JiraTestUpdateService.updateTestStatus(jiraTestCaseId, testName, "FAIL", errorMsg);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TL.get().skip("SKIPPED");
        
        // Extract Jira test case ID from annotation
        String jiraTestCaseId = extractJiraTestCaseId(result);
        if (jiraTestCaseId != null) {
            String testName = result.getMethod().getMethodName();
            JiraTestUpdateService.updateTestStatus(jiraTestCaseId, testName, "SKIP");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
    }

    /**
     * Extract Jira Test Case ID from @JiraTestCase annotation
     */
    private String extractJiraTestCaseId(ITestResult result) {
        try {
            Method method = result.getMethod().getRealClass().getMethod(result.getMethod().getMethodName());
            JiraTestCase annotation = method.getAnnotation(JiraTestCase.class);
            
            if (annotation != null) {
                return annotation.id();
            } else {
                System.out.println("Warning: @JiraTestCase annotation not found on method: " + result.getMethod().getMethodName());
            }
        } catch (NoSuchMethodException e) {
            System.out.println("Error extracting Jira annotation: " + e.getMessage());
        }
        return null;
    }
}