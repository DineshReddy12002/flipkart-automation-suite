package com.flipkart.automation.constants;

/**
 * Application-wide constants used across the automation framework.
 */
public final class TestConstants {

    /** Classpath-relative paths — work in Eclipse and Maven without hardcoded filesystem paths. */
    public static final String CREDENTIALS_EXCEL = "testdata/credentials.xlsx";
    public static final String PRODUCT_SEARCH_EXCEL = "testdata/productSearch.xlsx";
    public static final String ADDRESS_DATA_EXCEL = "testdata/addressData.xlsx";

    public static final String CREDENTIALS_SHEET = "Credentials";
    public static final String PRODUCT_SEARCH_SHEET = "SearchData";
    public static final String ADDRESS_DATA_SHEET = "Addresses";

    public static final String EXTENT_REPORT_PATH = "test-output/ExtentReport.html";
    public static final String SCREENSHOT_DIR = "test-output/screenshots/";

    private TestConstants() {
    }
}
