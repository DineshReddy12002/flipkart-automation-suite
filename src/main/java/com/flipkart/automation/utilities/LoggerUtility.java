package com.flipkart.automation.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Centralized Log4j2 logging wrapper with timestamped messages.
 */
public final class LoggerUtility {

    private static final Logger logger = LogManager.getLogger(LoggerUtility.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LoggerUtility() {
    }

    private static String withTimestamp(String message) {
        return "[" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + "] " + message;
    }

    /**
     * Logs an informational message.
     */
    public static void logInfo(String message) {
        logger.info(withTimestamp(message));
    }

    /**
     * Logs a warning message.
     */
    public static void logWarning(String message) {
        logger.warn(withTimestamp(message));
    }

    /**
     * Logs an error message.
     */
    public static void logError(String message) {
        logger.error(withTimestamp(message));
    }

    /**
     * Logs a debug message.
     */
    public static void logDebug(String message) {
        logger.debug(withTimestamp(message));
    }
}
