package com.flipkart.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for shopping cart page interactions.
 */
public class CartPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CartPage.class);

    private final By cartItems = By.xpath("//div[contains(@class,'cart')]//a[contains(@class,'title')] | //div[contains(@class,'_2-uG6')]//a");
    private final By quantityInput = By.xpath("//input[contains(@class,'quantity') or @type='number']");
    private final By removeBtn = By.xpath("//button[contains(text(),'Remove') or contains(text(),'REMOVE')]");
    private final By totalPrice = By.xpath("//span[contains(text(),'Total')]/following::span[1] | //div[contains(@class,'total')]//span");
    private final By subtotal = By.xpath("//span[contains(text(),'Subtotal') or contains(text(),'Price')]/following::span[1]");
    private final By tax = By.xpath("//span[contains(text(),'Tax') or contains(text(),'GST')]/following::span[1]");
    private final By proceedCheckoutBtn = By.xpath("//button[contains(text(),'Place Order') or contains(text(),'PLACE ORDER')]");
    private final By couponInput = By.xpath("//input[contains(@placeholder,'coupon') or contains(@placeholder,'Coupon')]");
    private final By applyCouponBtn = By.xpath("//button[contains(text(),'Apply') or contains(text(),'APPLY')]");
    private final By removeCouponBtn = By.xpath("//button[contains(text(),'Remove Coupon') or contains(text(),'remove')]");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns the number of distinct items in the cart.
     */
    public int getCartItemCount() {
        logger.info("Counting cart items");
        List<WebElement> items = driver.findElements(cartItems);
        logger.info("Cart item count: {}", items.size());
        return items.size();
    }

    /**
     * Updates quantity for a product identified by name.
     */
    public void updateQuantity(String productName, int quantity) {
        logger.info("Updating quantity for '{}' to {}", productName, quantity);
        By productQty = By.xpath("//a[contains(text(),'" + productName + "')]/ancestor::div[contains(@class,'cart') or contains(@class,'item')]//input");
        sendKeys(productQty, String.valueOf(quantity));
        logger.info("Quantity updated for: {}", productName);
    }

    /**
     * Removes a product from the cart by name.
     */
    public void removeItem(String productName) {
        logger.info("Removing item from cart: {}", productName);
        By removeLocator = By.xpath("//a[contains(text(),'" + productName + "')]/ancestor::div//button[contains(text(),'Remove')]");
        clickElement(removeLocator);
        logger.info("Item removed: {}", productName);
    }

    /**
     * Returns the cart total price text.
     */
    public String getCartTotal() {
        logger.info("Fetching cart total");
        return getText(totalPrice);
    }

    /**
     * Returns the cart subtotal text.
     */
    public String getSubtotal() {
        logger.info("Fetching cart subtotal");
        return getText(subtotal);
    }

    /**
     * Returns the tax amount text.
     */
    public String getTaxAmount() {
        logger.info("Fetching tax amount");
        try {
            return getText(tax);
        } catch (Exception e) {
            logger.warn("Tax element not found");
            return "0";
        }
    }

    /**
     * Proceeds from cart to checkout.
     */
    public void proceedToCheckout() {
        logger.info("Proceeding to checkout");
        clickElement(proceedCheckoutBtn);
        logger.info("Navigated to checkout");
    }

    /**
     * Applies a coupon code to the cart.
     */
    public void applyCoupon(String couponCode) {
        logger.info("Applying coupon: {}", couponCode);
        sendKeys(couponInput, couponCode);
        clickElement(applyCouponBtn);
        logger.info("Coupon applied: {}", couponCode);
    }

    /**
     * Removes an applied coupon from the cart.
     */
    public void removeCoupon() {
        logger.info("Removing applied coupon");
        if (isElementDisplayed(removeCouponBtn)) {
            clickElement(removeCouponBtn);
        }
        logger.info("Coupon removed");
    }

    /**
     * Removes all items from the cart.
     */
    public void clearEntireCart() {
        logger.info("Clearing entire cart");
        List<WebElement> removeButtons = driver.findElements(removeBtn);
        for (WebElement btn : removeButtons) {
            btn.click();
        }
        logger.info("Cart cleared");
    }
}
