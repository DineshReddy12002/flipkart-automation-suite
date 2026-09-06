package com.flipkart.automation.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.flipkart.automation.config.ConfigReader;
import com.flipkart.automation.pages.HomePage;
import com.flipkart.automation.pages.LoginPage;
import com.flipkart.automation.utilities.DriverManager;
import com.flipkart.automation.utilities.ExtentReportManager;
import com.flipkart.automation.utilities.LoggerUtility;
import com.flipkart.automation.utilities.ScreenshotUtility;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base test class providing WebDriver lifecycle, navigation, and reporting hooks.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected HomePage homePage;
    protected LoginPage loginPage;
    protected ExtentReports extentReports;
    protected ExtentTest extentTest;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        String resolvedBrowser = System.getProperty("browser", browser);
        LoggerUtility.logInfo("Starting test setup with browser: " + resolvedBrowser);

        extentReports = ExtentReportManager.getInstance();
        driver = DriverManager.initializeDriver(resolvedBrowser);
        driver.get(ConfigReader.getBaseURL());

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        LoggerUtility.logInfo("Navigated to: " + ConfigReader.getBaseURL());
    }

    /**
     * Closes Flipkart's login popup so guest-user tests (search, browse) can proceed.
     */
    protected void prepareGuestSession() {
        loginPage.closeBlockingLoginModal();
        LoggerUtility.logInfo("Guest session prepared — login modal closed");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            String screenshotPath = ScreenshotUtility.captureScreenshot(driver, testName);
            LoggerUtility.logError("Test failed: " + testName + " | Screenshot: " + screenshotPath);
            if (extentTest != null) {
                extentTest.fail("Test failed. Screenshot: " + screenshotPath);
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            LoggerUtility.logInfo("Test passed: " + testName);
            if (extentTest != null) {
                extentTest.pass("Test passed");
            }
        } else if (result.getStatus() == ITestResult.SKIP && extentTest != null) {
            extentTest.skip("Test skipped");
        }

        DriverManager.quitDriver();
        LoggerUtility.logInfo("Test teardown completed");
    }

    protected void startExtentTest(String testName) {
        extentTest = extentReports.createTest(testName);
    }

    protected void logExtentInfo(String message) {
        LoggerUtility.logInfo(message);
        if (extentTest != null) {
            extentTest.info(message);
        }
    }
}
