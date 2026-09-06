package com.flipkart.automation.utilities;

import com.flipkart.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Shared helper methods used across page objects and test classes.
 */
public final class CommonMethods {

    private static final Logger logger = LogManager.getLogger(CommonMethods.class);

    private CommonMethods() {
    }

    /**
     * Scrolls the page until the element is centered in the viewport.
     */
    public static void scrollToElement(WebDriver driver, By locator) {
        logger.info("Scrolling to element: {}", locator);
        WebElement element = WaitUtility.waitForElementPresence(driver, locator, ConfigReader.getTimeout());
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    /**
     * Scrolls to the top of the page.
     */
    public static void scrollToTop(WebDriver driver) {
        logger.info("Scrolling to top of page");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Scrolls to the bottom of the page.
     */
    public static void scrollToBottom(WebDriver driver) {
        logger.info("Scrolling to bottom of page");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Closes all browser windows except the first one.
     */
    public static void handleWindowPopups(WebDriver driver) {
        logger.info("Handling window popups");
        String mainWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                driver.close();
                logger.info("Closed popup window: {}", window);
            }
        }
        driver.switchTo().window(mainWindow);
    }

    /**
     * Switches WebDriver focus to the most recently opened window.
     */
    public static void switchToNewWindow(WebDriver driver) {
        logger.info("Switching to newest browser window");
        List<String> windows = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windows.get(windows.size() - 1));
    }

    /**
     * Retries locating and returning an element until success or retries are exhausted.
     */
    public static WebElement retryElement(WebDriver driver, By locator, int retries) {
        logger.info("Retrying element lookup: {} | max retries: {}", locator, retries);
        RuntimeException lastException = null;

        for (int attempt = 1; attempt <= retries; attempt++) {
            try {
                WebElement element = WaitUtility.waitForElementVisibility(driver, locator, ConfigReader.getTimeout());
                logger.info("Element found on attempt {}/{}: {}", attempt, retries, locator);
                return element;
            } catch (TimeoutException e) {
                lastException = new RuntimeException("Attempt " + attempt + " failed for locator: " + locator, e);
                logger.warn("Retry attempt {}/{} failed for: {}", attempt, retries, locator);
            }
        }

        throw lastException != null ? lastException : new RuntimeException("Element not found: " + locator);
    }

    /**
     * Clears a field and sends new text using the active driver from DriverManager.
     */
    public static void clearFieldAndSendKeys(By locator, String text) {
        clearFieldAndSendKeys(DriverManager.getDriver(), locator, text);
    }

    /**
     * Clears a field and sends new text.
     */
    public static void clearFieldAndSendKeys(WebDriver driver, By locator, String text) {
        logger.info("Clearing and sending keys to: {}", locator);
        WebElement element = WaitUtility.waitForElementVisibility(driver, locator, ConfigReader.getTimeout());
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Generates a random email address for test registration flows.
     */
    public static String getRandomEmail() {
        String email = "automation." + UUID.randomUUID().toString().substring(0, 8) + "@testmail.com";
        logger.debug("Generated random email: {}", email);
        return email;
    }

    /**
     * Returns the current date in yyyy-MM-dd format.
     */
    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Returns the current time in HH:mm:ss format.
     */
    public static String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
