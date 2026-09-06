package com.flipkart.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * Page Object for checkout flow including address and payment selection.
 */
public class CheckoutPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CheckoutPage.class);

    private final By addressDropdown = By.xpath("//select[contains(@name,'address')] | //div[contains(@class,'address')]//select");
    private final By paymentMethodRadio = By.xpath("//input[@type='radio' and contains(@name,'payment')] | //label[contains(@class,'payment')]");
    private final By placeOrderBtn = By.xpath("//button[contains(text(),'Pay') or contains(text(),'Place Order') or contains(text(),'Continue')]");
    private final By orderSummary = By.xpath("//div[contains(@class,'order-summary') or contains(text(),'Order Summary')]");
    private final By deliveryDate = By.xpath("//span[contains(text(),'Delivery') or contains(@class,'delivery-date')]");
    private final By addAddressBtn = By.xpath("//button[contains(text(),'Add') and contains(text(),'Address')] | //span[contains(text(),'Add a new address')]");
    private final By fullNameInput = By.xpath("//input[@name='name' or contains(@placeholder,'Name')]");
    private final By phoneInput = By.xpath("//input[@name='phone' or contains(@placeholder,'Phone')]");
    private final By addressInput = By.xpath("//textarea[@name='address' or contains(@placeholder,'Address')]");
    private final By cityInput = By.xpath("//input[@name='city' or contains(@placeholder,'City')]");
    private final By stateInput = By.xpath("//input[@name='state' or contains(@placeholder,'State')]");
    private final By pincodeInput = By.xpath("//input[@name='pincode' or contains(@placeholder,'Pincode')]");
    private final By saveAddressBtn = By.xpath("//button[contains(text(),'Save') or contains(text(),'Deliver Here')]");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Selects an existing delivery address by label or partial text.
     */
    public void selectAddress(String address) {
        logger.info("Selecting address: {}", address);
        By addressOption = By.xpath("//label[contains(text(),'" + address + "')] | //option[contains(text(),'" + address + "')]");
        clickElement(addressOption);
        logger.info("Address selected: {}", address);
    }

    /**
     * Adds a new delivery address using the provided details map.
     */
    public void addNewAddress(Map<String, String> addressDetails) {
        logger.info("Adding new address for: {}", addressDetails.get("FullName"));
        clickElement(addAddressBtn);
        sendKeys(fullNameInput, addressDetails.getOrDefault("FullName", ""));
        sendKeys(phoneInput, addressDetails.getOrDefault("PhoneNumber", ""));
        sendKeys(addressInput, addressDetails.getOrDefault("Address", ""));
        sendKeys(cityInput, addressDetails.getOrDefault("City", ""));
        sendKeys(stateInput, addressDetails.getOrDefault("State", ""));
        sendKeys(pincodeInput, addressDetails.getOrDefault("Pincode", ""));
        clickElement(saveAddressBtn);
        logger.info("New address saved");
    }

    /**
     * Selects a payment method (COD, Card, UPI, etc.).
     */
    public void selectPaymentMethod(String method) {
        logger.info("Selecting payment method: {}", method);
        By paymentOption = By.xpath("//label[contains(text(),'" + method + "')] | //input[@value='" + method + "']");
        clickElement(paymentOption);
        logger.info("Payment method selected: {}", method);
    }

    /**
     * Returns the order summary section text.
     */
    public String getOrderSummary() {
        logger.info("Fetching order summary");
        return getText(orderSummary);
    }

    /**
     * Places the order by clicking the final submit button.
     */
    public void placeOrder() {
        logger.info("Placing order");
        clickElement(placeOrderBtn);
        logger.info("Order placed");
    }

    /**
     * Verifies and returns the delivery date shown at checkout.
     */
    public String verifyDeliveryDate() {
        logger.info("Verifying delivery date at checkout");
        return getText(deliveryDate);
    }
}
