package com.flipkart.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for order history and order management flows.
 */
public class OrdersPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(OrdersPage.class);

    private final By ordersLink = By.xpath("//a[contains(text(),'Orders') or contains(@href,'orders')]");
    private final By orderList = By.xpath("//div[contains(@class,'order') and contains(@class,'item')] | //a[contains(@href,'order')]");
    private final By orderStatus = By.xpath("//span[contains(@class,'status') or contains(text(),'Delivered') or contains(text(),'Shipped')]");
    private final By cancelOrderBtn = By.xpath("//button[contains(text(),'Cancel') or contains(text(),'CANCEL')]");
    private final By downloadInvoiceBtn = By.xpath("//a[contains(text(),'Invoice') or contains(text(),'invoice')]");
    private final By trackOrderLink = By.xpath("//a[contains(text(),'Track') or contains(text(),'track')]");
    private final By deliveryDateLabel = By.xpath("//span[contains(text(),'Delivery') or contains(@class,'delivery')]");

    public OrdersPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the orders section from account menu.
     */
    public void navigateToOrders() {
        logger.info("Navigating to orders page");
        clickElement(ordersLink);
    }

    /**
     * Opens order details for the first order in the list.
     */
    public void viewFirstOrderDetails() {
        logger.info("Viewing first order details");
        clickElement(orderList);
    }

    /**
     * Returns the current order status text.
     */
    public String getOrderStatus() {
        logger.info("Fetching order status");
        return getText(orderStatus);
    }

    /**
     * Cancels the currently viewed order.
     */
    public void cancelOrder() {
        logger.info("Cancelling order");
        clickElement(cancelOrderBtn);
        handleAlert("accept");
        logger.info("Order cancellation submitted");
    }

    /**
     * Downloads the invoice for the current order.
     */
    public void downloadInvoice() {
        logger.info("Downloading invoice");
        clickElement(downloadInvoiceBtn);
    }

    /**
     * Clicks track order for the current order.
     */
    public void trackOrder() {
        logger.info("Tracking order");
        clickElement(trackOrderLink);
    }

    /**
     * Returns the delivery date text from order details.
     */
    public String getDeliveryDate() {
        logger.info("Fetching delivery date from orders");
        return getText(deliveryDateLabel);
    }

    /**
     * Returns the number of orders displayed.
     */
    public int getOrderCount() {
        logger.info("Counting orders in history");
        return driver.findElements(orderList).size();
    }
}
