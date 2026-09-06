package com.flipkart.automation.tests;

import com.aventstack.extentreports.ExtentReports;
import com.flipkart.automation.utilities.ExtentReportManager;
import com.flipkart.automation.utilities.LoggerUtility;
import com.flipkart.automation.utilities.ScreenshotUtility;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.flipkart.automation.utilities.DriverManager;

/**
 * TestNG listener for logging, screenshots on failure, and ExtentReports flush.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtility.logInfo("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerUtility.logInfo("Test succeeded: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LoggerUtility.logError("Test failed: " + result.getMethod().getMethodName()
                + " | " + result.getThrowable());
        try {
            WebDriver driver = DriverManager.getDriver();
            ScreenshotUtility.captureScreenshot(driver, result.getMethod().getMethodName() + "_failure");
        } catch (Exception e) {
            LoggerUtility.logWarning("Could not capture failure screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerUtility.logWarning("Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReports extentReports = ExtentReportManager.getInstance();
        extentReports.flush();
        LoggerUtility.logInfo("Test suite finished. Extent report flushed.");
    }
}
