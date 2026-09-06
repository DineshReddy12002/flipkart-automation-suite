package com.flipkart.automation.utilities;

import com.flipkart.automation.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Thread-safe WebDriver lifecycle manager with WebDriverManager integration.
 */
public final class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
    }

    /**
     * Initializes and stores a WebDriver instance for the current thread.
     *
     * @param browser browser name: chrome, firefox, or edge
     * @return initialized WebDriver instance
     */
    public static WebDriver initializeDriver(String browser) {
        if (driverThreadLocal.get() != null) {
            logger.warn("Driver already initialized for this thread. Returning existing instance.");
            return driverThreadLocal.get();
        }

        WebDriver driver;
        String normalizedBrowser = browser == null ? "chrome" : browser.trim().toLowerCase();

        switch (normalizedBrowser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--width=1920", "--height=1080");
                driver = new FirefoxDriver(firefoxOptions);
                logger.info("Firefox browser initialized");
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized", "--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                logger.info("Edge browser initialized");
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--disable-blink-features=AutomationControlled",
                        "--disable-notifications",
                        "--start-maximized"
                );
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.addArguments("--user-data-dir=" + System.getProperty("java.io.tmpdir")
                        + "flipkart-chrome-profile-" + Thread.currentThread().threadId());
                driver = new ChromeDriver(chromeOptions);
                logger.info("Chrome browser initialized");
                break;
        }

        int implicitWait = ConfigReader.getImplicitWait();
        int pageLoadTimeout = ConfigReader.getPageLoadTimeout();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        logger.info("Driver configured with implicitWait={}s, pageLoadTimeout={}s", implicitWait, pageLoadTimeout);
        return driver;
    }

    /**
     * Returns the WebDriver instance bound to the current thread.
     *
     * @return active WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call initializeDriver() first.");
        }
        return driver;
    }

    /**
     * Quits the WebDriver instance and removes it from the current thread.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
