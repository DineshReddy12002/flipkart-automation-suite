package com.flipkart.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for order confirmation page after successful checkout.
 */
public class OrderConfirmationPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(OrderConfirmationPage.class);

    private final By orderNumber = By.xpath("//span[contains(text(),'Order') and contains(text(),'ID')] | //div[contains(@class,'order-id')]");
    private final By confirmationMessage = By.xpath("//div[contains(text(),'Thank') or contains(text(),'confirmed') or contains(text(),'Successfully')]");
    private final By orderDetails = By.xpath("//div[contains(@class,'order-details') or contains(text(),'Order Details')]");
    private final By trackOrderBtn = By.xpath("//button[contains(text(),'Track') or contains(text(),'TRACK')] | //a[contains(text(),'Track')]");

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns the order number/ID text.
     */
    public String getOrderNumber() {
        logger.info("Fetching order number");
        return getText(orderNumber);
    }

    /**
     * Returns the order confirmation message text.
     */
    public String getConfirmationMessage() {
        logger.info("Fetching confirmation message");
        return getText(confirmationMessage);
    }

    /**
     * Returns the full order details section text.
     */
    public String getOrderDetails() {
        logger.info("Fetching order details");
        return getText(orderDetails);
    }

    /**
     * Clicks the track order button on the confirmation page.
     */
    public void clickTrackOrder() {
        logger.info("Clicking track order button");
        clickElement(trackOrderBtn);
        logger.info("Track order clicked");
    }
}
