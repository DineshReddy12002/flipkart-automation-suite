package com.flipkart.automation.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.flipkart.automation.constants.TestConstants;

/**
 * Thread-safe ExtentReports manager for HTML test reporting.
 */
public final class ExtentReportManager {

    private static ExtentReports extentReports;

    private ExtentReportManager() {
    }

    /**
     * Returns the singleton ExtentReports instance, creating it if necessary.
     */
    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(TestConstants.EXTENT_REPORT_PATH);
            sparkReporter.config().setDocumentTitle("Flipkart Automation Suite");
            sparkReporter.config().setReportName("Flipkart E2E Test Report");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Project", "Flipkart Automation Suite");
            extentReports.setSystemInfo("Framework", "Selenium + TestNG + POM");
        }
        return extentReports;
    }
}
