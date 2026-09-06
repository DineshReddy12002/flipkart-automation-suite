package com.flipkart.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads configuration values from config.properties on the classpath.
 */
public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final String CONFIG_PATH = "properties/config.properties";
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private ConfigReader() {
    }

    private static void loadProperties() {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Configuration file not found: " + CONFIG_PATH);
            }
            properties.load(inputStream);
            logger.info("Configuration loaded from {}", CONFIG_PATH);
        } catch (IOException e) {
            logger.error("Failed to load configuration file: {}", CONFIG_PATH, e);
            throw new RuntimeException("Failed to load configuration file: " + CONFIG_PATH, e);
        }
    }

    /**
     * @return application base URL
     */
    public static String getBaseURL() {
        return getProperty("baseURL");
    }

    /**
     * @return default browser name
     */
    public static String getBrowser() {
        String browser = System.getProperty("browser");
        return browser != null && !browser.isBlank() ? browser : getProperty("browser");
    }

    /**
     * @return explicit wait timeout in seconds
     */
    public static int getTimeout() {
        return Integer.parseInt(getProperty("timeout"));
    }

    /**
     * @return implicit wait timeout in seconds
     */
    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicitWait"));
    }

    /**
     * @return page load timeout in seconds
     */
    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("pageLoadTimeout"));
    }

    /**
     * @return test user email
     */
    public static String getEmail() {
        return getProperty("email");
    }

    /**
     * @return test user password
     */
    public static String getPassword() {
        return getProperty("password");
    }

    private static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Missing or empty configuration property: " + key);
        }
        return value.trim();
    }
}
