package com.flipkart.automation.pages;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base page class providing common WebDriver interactions, waits, and utilities
 * for all Page Object Model classes in the automation suite.
 */
public class BasePage {

    private static final Logger logger = LogManager.getLogger(BasePage.class);
    private static final int DEFAULT_EXPLICIT_WAIT_SECONDS = 10;
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Creates a BasePage instance bound to the given WebDriver.
     *
     * @param driver active WebDriver session
     */
    public BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver instance cannot be null");
        }
        this.driver = driver;
        this.wait = explicitWait();
        logger.info("BasePage initialized for driver: {}", driver.getClass().getSimpleName());
    }

    /**
     * Returns a WebDriverWait with the default 10-second timeout.
     *
     * @return configured WebDriverWait instance
     */
    public WebDriverWait explicitWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_EXPLICIT_WAIT_SECONDS));
    }

    /**
     * Returns a WebDriverWait with a custom timeout in seconds.
     *
     * @param timeoutSeconds wait duration in seconds
     * @return configured WebDriverWait instance
     */
    public WebDriverWait explicitWait(long timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Waits for element to be clickable and performs a click.
     *
     * @param locator element locator
     */
    public void clickElement(By locator) {
        try {
            logger.info("Clicking element: {}", locator);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info("Successfully clicked element: {}", locator);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for clickable element: {}", locator, e);
            throw new RuntimeException("Failed to click element: " + locator, e);
        } catch (Exception e) {
            logger.error("Error clicking element: {}", locator, e);
            throw new RuntimeException("Failed to click element: " + locator, e);
        }
    }

    /**
     * Waits for element visibility, clears existing text, and sends keys.
     *
     * @param locator element locator
     * @param text    text to enter
     */
    public void sendKeys(By locator, String text) {
        try {
            logger.info("Sending keys to element: {} with text: {}", locator, text);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            logger.info("Successfully entered text into element: {}", locator);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for visible element: {}", locator, e);
            throw new RuntimeException("Failed to send keys to element: " + locator, e);
        } catch (Exception e) {
            logger.error("Error sending keys to element: {}", locator, e);
            throw new RuntimeException("Failed to send keys to element: " + locator, e);
        }
    }

    /**
     * Waits for element visibility and returns its text content.
     *
     * @param locator element locator
     * @return visible text of the element
     */
    public String getText(By locator) {
        try {
            logger.info("Getting text from element: {}", locator);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String text = element.getText();
            logger.info("Retrieved text '{}' from element: {}", text, locator);
            return text;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for visible element: {}", locator, e);
            throw new RuntimeException("Failed to get text from element: " + locator, e);
        } catch (Exception e) {
            logger.error("Error getting text from element: {}", locator, e);
            throw new RuntimeException("Failed to get text from element: " + locator, e);
        }
    }

    /**
     * Checks whether an element is displayed on the page.
     *
     * @param locator element locator
     * @return true if displayed, false otherwise
     */
    public boolean isElementDisplayed(By locator) {
        try {
            logger.debug("Checking visibility of element: {}", locator);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            boolean displayed = element.isDisplayed();
            logger.info("Element {} displayed status: {}", locator, displayed);
            return displayed;
        } catch (TimeoutException e) {
            logger.warn("Element not visible within timeout: {}", locator);
            return false;
        } catch (Exception e) {
            logger.error("Error checking element visibility: {}", locator, e);
            return false;
        }
    }

    /**
     * Selects a dropdown option by its value attribute.
     *
     * @param locator dropdown element locator
     * @param value   option value to select
     */
    public void dropdownSelectByValue(By locator, String value) {
        try {
            logger.info("Selecting dropdown option by value '{}' for: {}", value, locator);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Select select = new Select(element);
            select.selectByValue(value);
            logger.info("Successfully selected value '{}' from dropdown: {}", value, locator);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for dropdown element: {}", locator, e);
            throw new RuntimeException("Failed to select dropdown by value: " + locator, e);
        } catch (Exception e) {
            logger.error("Error selecting dropdown by value: {}", locator, e);
            throw new RuntimeException("Failed to select dropdown by value: " + locator, e);
        }
    }

    /**
     * Selects a dropdown option by its visible text.
     *
     * @param locator dropdown element locator
     * @param text    visible option text to select
     */
    public void dropdownSelectByVisibleText(By locator, String text) {
        try {
            logger.info("Selecting dropdown option by visible text '{}' for: {}", text, locator);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Select select = new Select(element);
            select.selectByVisibleText(text);
            logger.info("Successfully selected visible text '{}' from dropdown: {}", text, locator);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for dropdown element: {}", locator, e);
            throw new RuntimeException("Failed to select dropdown by visible text: " + locator, e);
        } catch (Exception e) {
            logger.error("Error selecting dropdown by visible text: {}", locator, e);
            throw new RuntimeException("Failed to select dropdown by visible text: " + locator, e);
        }
    }

    /**
     * Hovers the mouse pointer over the specified element.
     *
     * @param locator element locator
     */
    public void hoverOverElement(By locator) {
        try {
            logger.info("Hovering over element: {}", locator);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            logger.info("Successfully hovered over element: {}", locator);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to hover: {}", locator, e);
            throw new RuntimeException("Failed to hover over element: " + locator, e);
        } catch (Exception e) {
            logger.error("Error hovering over element: {}", locator, e);
            throw new RuntimeException("Failed to hover over element: " + locator, e);
        }
    }

    /**
     * Performs a double-click on the specified element.
     *
     * @param locator element locator
     */
    public void doubleClickElement(By locator) {
        try {
            logger.info("Double-clicking element: {}", locator);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            Actions actions = new Actions(driver);
            actions.doubleClick(element).perform();
            logger.info("Successfully double-clicked element: {}", locator);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to double-click: {}", locator, e);
            throw new RuntimeException("Failed to double-click element: " + locator, e);
        } catch (Exception e) {
            logger.error("Error double-clicking element: {}", locator, e);
            throw new RuntimeException("Failed to double-click element: " + locator, e);
        }
    }

    /**
     * Performs a right-click (context click) on the specified element.
     *
     * @param locator element locator
     */
    public void rightClickElement(By locator) {
        try {
            logger.info("Right-clicking element: {}", locator);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            Actions actions = new Actions(driver);
            actions.contextClick(element).perform();
            logger.info("Successfully right-clicked element: {}", locator);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to right-click: {}", locator, e);
            throw new RuntimeException("Failed to right-click element: " + locator, e);
        } catch (Exception e) {
            logger.error("Error right-clicking element: {}", locator, e);
            throw new RuntimeException("Failed to right-click element: " + locator, e);
        }
    }

    /**
     * Scrolls the page until the specified element is in view.
     *
     * @param locator element locator
     */
    public void scrollToElement(By locator) {
        try {
            logger.info("Scrolling to element: {}", locator);
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            logger.info("Successfully scrolled to element: {}", locator);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to scroll: {}", locator, e);
            throw new RuntimeException("Failed to scroll to element: " + locator, e);
        } catch (Exception e) {
            logger.error("Error scrolling to element: {}", locator, e);
            throw new RuntimeException("Failed to scroll to element: " + locator, e);
        }
    }

    /**
     * Switches WebDriver focus to the active JavaScript alert.
     *
     * @return Alert instance
     */
    public Alert switchToAlert() {
        try {
            logger.info("Switching to browser alert");
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            logger.info("Successfully switched to alert");
            return alert;
        } catch (TimeoutException e) {
            logger.error("No alert present within timeout", e);
            throw new RuntimeException("Failed to switch to alert", e);
        } catch (Exception e) {
            logger.error("Error switching to alert", e);
            throw new RuntimeException("Failed to switch to alert", e);
        }
    }

    /**
     * Handles a browser alert based on the specified action.
     * Supported actions: accept, dismiss, getText
     *
     * @param action alert action to perform
     * @return alert text when action is "getText", otherwise null
     */
    public String handleAlert(String action) {
        try {
            logger.info("Handling alert with action: {}", action);
            Alert alert = switchToAlert();
            String result = null;

            switch (action.toLowerCase()) {
                case "accept":
                    alert.accept();
                    logger.info("Alert accepted");
                    break;
                case "dismiss":
                    alert.dismiss();
                    logger.info("Alert dismissed");
                    break;
                case "gettext":
                    result = alert.getText();
                    logger.info("Alert text retrieved: {}", result);
                    break;
                default:
                    logger.error("Unsupported alert action: {}", action);
                    throw new IllegalArgumentException("Unsupported alert action: " + action
                            + ". Supported actions: accept, dismiss, getText");
            }
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error handling alert with action: {}", action, e);
            throw new RuntimeException("Failed to handle alert with action: " + action, e);
        }
    }

    /**
     * Captures a screenshot and saves it to the test-output/screenshots directory.
     *
     * @param filename base filename (without extension)
     * @return absolute path of the saved screenshot file
     */
    public String takeScreenshot(String filename) {
        try {
            logger.info("Capturing screenshot: {}", filename);
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists() && !screenshotDir.mkdirs()) {
                logger.warn("Could not create screenshot directory: {}", SCREENSHOT_DIR);
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String sanitizedFilename = filename.replaceAll("[^a-zA-Z0-9_-]", "_");
            File destination = new File(screenshotDir, timestamp + "_" + sanitizedFilename + ".png");

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, destination);

            String filePath = destination.getAbsolutePath();
            logger.info("Screenshot saved to: {}", filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("IO error while saving screenshot: {}", filename, e);
            throw new RuntimeException("Failed to save screenshot: " + filename, e);
        } catch (Exception e) {
            logger.error("Error capturing screenshot: {}", filename, e);
            throw new RuntimeException("Failed to capture screenshot: " + filename, e);
        }
    }
}
