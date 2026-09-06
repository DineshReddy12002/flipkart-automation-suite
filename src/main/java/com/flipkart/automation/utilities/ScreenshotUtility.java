package com.flipkart.automation.utilities;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures WebDriver screenshots and stores them under test-output/screenshots.
 */
public final class ScreenshotUtility {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtility.class);
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtility() {
    }

    /**
     * Captures a screenshot for the given test name.
     *
     * @param driver   active WebDriver session
     * @param testName test or method name
     * @return absolute path of the saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists() && !screenshotDir.mkdirs()) {
                logger.warn("Could not create screenshot directory: {}", SCREENSHOT_DIR);
            }

            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String sanitizedName = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
            File destination = new File(screenshotDir, timestamp + "_" + sanitizedName + ".png");

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, destination);

            String filePath = destination.getAbsolutePath();
            logger.info("Screenshot captured: {}", filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test: {}", testName, e);
            throw new RuntimeException("Failed to capture screenshot for test: " + testName, e);
        }
    }
}
