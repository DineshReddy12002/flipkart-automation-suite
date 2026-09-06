package com.flipkart.automation.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Reusable explicit wait helpers for WebDriver elements and page conditions.
 */
public final class WaitUtility {

    private static final Logger logger = LogManager.getLogger(WaitUtility.class);

    private WaitUtility() {
    }

    /**
     * Waits until the element is present in the DOM.
     */
    public static WebElement waitForElementPresence(WebDriver driver, By locator, long timeoutSeconds) {
        logger.debug("Waiting for element presence: {} ({}s)", locator, timeoutSeconds);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits until the element is visible on the page.
     */
    public static WebElement waitForElementVisibility(WebDriver driver, By locator, long timeoutSeconds) {
        logger.debug("Waiting for element visibility: {} ({}s)", locator, timeoutSeconds);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is clickable.
     */
    public static WebElement waitForElementClickability(WebDriver driver, By locator, long timeoutSeconds) {
        logger.debug("Waiting for element clickability: {} ({}s)", locator, timeoutSeconds);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the element becomes invisible or is removed from the DOM.
     */
    public static boolean waitForElementInvisibility(WebDriver driver, By locator, long timeoutSeconds) {
        logger.debug("Waiting for element invisibility: {} ({}s)", locator, timeoutSeconds);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until the page title contains the expected value.
     */
    public static boolean waitForTitle(WebDriver driver, String title, long timeoutSeconds) {
        logger.debug("Waiting for page title to contain: '{}' ({}s)", title, timeoutSeconds);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.titleContains(title));
    }
}
