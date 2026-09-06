package com.flipkart.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for individual product detail page interactions.
 */
public class ProductDetailPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProductDetailPage.class);

    private final By productTitle = By.xpath("//span[contains(@class,'B_NuCI')] | //h1[contains(@class,'title')]");
    private final By price = By.xpath("//div[contains(@class,'_30jeq3')] | //div[contains(@class,'price')]");
    private final By rating = By.xpath("//div[contains(@class,'_3LWZlK')] | //span[contains(@class,'rating')]");
    private final By addToCartBtn = By.xpath("//button[contains(text(),'ADD TO CART') or contains(text(),'Add to cart')]");
    private final By addToWishlistBtn = By.xpath("//button[contains(text(),'WISHLIST') or contains(text(),'Wishlist')]");
    private final By pincodeInput = By.xpath("//input[contains(@placeholder,'Enter Delivery Pincode') or @name='pincode']");
    private final By deliveryInfo = By.xpath("//div[contains(text(),'Delivery') or contains(@class,'delivery')]");
    private final By productImages = By.xpath("//img[contains(@class,'product') or contains(@class,'_396cs4')]");
    private final By productDescription = By.xpath("//div[contains(@class,'description') or contains(text(),'Description')]");
    private final By checkPincodeBtn = By.xpath("//span[contains(text(),'Check') or contains(text(),'check')]");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns the product title text.
     */
    public String getProductTitle() {
        logger.info("Fetching product title");
        return getText(productTitle);
    }

    /**
     * Returns the displayed product price.
     */
    public String getProductPrice() {
        logger.info("Fetching product price");
        return getText(price);
    }

    /**
     * Returns the product rating value.
     */
    public String getProductRating() {
        logger.info("Fetching product rating");
        return getText(rating);
    }

    /**
     * Adds the current product to the shopping cart.
     */
    public void addToCart() {
        logger.info("Adding product to cart");
        clickElement(addToCartBtn);
        logger.info("Product added to cart");
    }

    /**
     * Adds the current product to the wishlist.
     */
    public void addToWishlist() {
        logger.info("Adding product to wishlist");
        clickElement(addToWishlistBtn);
        logger.info("Product added to wishlist");
    }

    /**
     * Checks delivery availability for the given pincode.
     */
    public boolean checkPincodeAvailability(String pincode) {
        logger.info("Checking pincode availability: {}", pincode);
        sendKeys(pincodeInput, pincode);
        clickElement(checkPincodeBtn);
        boolean available = isElementDisplayed(deliveryInfo);
        logger.info("Pincode {} availability: {}", pincode, available);
        return available;
    }

    /**
     * Returns the estimated delivery date text.
     */
    public String getDeliveryDate() {
        logger.info("Fetching delivery date");
        return getText(deliveryInfo);
    }

    /**
     * Clicks through product image thumbnails.
     */
    public void viewImages() {
        logger.info("Viewing product images");
        List<WebElement> images = driver.findElements(productImages);
        for (int i = 0; i < Math.min(images.size(), 3); i++) {
            images.get(i).click();
            logger.info("Viewed image index: {}", i);
        }
    }

    /**
     * Returns the product description text.
     */
    public String getProductDescription() {
        logger.info("Fetching product description");
        scrollToElement(productDescription);
        return getText(productDescription);
    }

    /**
     * Checks whether the add-to-cart button is enabled (in stock).
     */
    public boolean isProductInStock() {
        logger.info("Checking product stock status");
        try {
            WebElement cartButton = driver.findElement(addToCartBtn);
            return cartButton.isEnabled() && cartButton.isDisplayed();
        } catch (Exception e) {
            logger.warn("Add to cart button not available");
            return false;
        }
    }
}
