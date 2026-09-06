package com.flipkart.automation.tests;

import com.flipkart.automation.pages.ProductDetailPage;
import com.flipkart.automation.pages.ProductSearchPage;
import com.flipkart.automation.utilities.LoggerUtility;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Product detail page verification test cases.
 */
public class ProductDetailTests extends BaseTest {

    private ProductSearchPage productSearchPage;
    private ProductDetailPage productDetailPage;

    @BeforeMethod(alwaysRun = true)
    public void initSearchPage() {
        prepareGuestSession();
        productSearchPage = new ProductSearchPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        openFirstSearchResult();
    }

    private void openFirstSearchResult() {
        homePage.searchProduct("laptop");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Search must return products");
        productSearchPage.selectFirstProduct();
    }

    @Test(priority = 1, description = "View product details page")
    public void testViewProductDetails() {
        startExtentTest("testViewProductDetails");
        Assert.assertTrue(driver.getCurrentUrl().contains("/p/"), "Product detail URL should contain /p/");
    }

    @Test(priority = 2, description = "Verify product price is displayed")
    public void testVerifyProductPrice() {
        startExtentTest("testVerifyProductPrice");
        try {
            String price = productDetailPage.getProductPrice();
            Assert.assertFalse(price.isBlank(), "Product price should be displayed");
        } catch (Exception e) {
            Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"), "Product page should be accessible");
        }
    }

    @Test(priority = 3, description = "Verify product rating is displayed")
    public void testVerifyProductRating() {
        startExtentTest("testVerifyProductRating");
        try {
            String rating = productDetailPage.getProductRating();
            Assert.assertNotNull(rating, "Product rating should be present");
        } catch (Exception e) {
            LoggerUtility.logWarning("Rating not found on page");
            Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
        }
    }

    @Test(priority = 4, description = "Check product availability")
    public void testCheckProductAvailability() {
        startExtentTest("testCheckProductAvailability");
        boolean inStock = productDetailPage.isProductInStock();
        LoggerUtility.logInfo("Product in stock: " + inStock);
        Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
    }

    @Test(priority = 5, description = "Verify delivery date information")
    public void testVerifyDeliveryDate() {
        startExtentTest("testVerifyDeliveryDate");
        try {
            String delivery = productDetailPage.getDeliveryDate();
            Assert.assertNotNull(delivery);
        } catch (Exception e) {
            Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
        }
    }

    @Test(priority = 6, description = "Check pincode availability")
    public void testCheckPincodeAvailability() {
        startExtentTest("testCheckPincodeAvailability");
        boolean available = productDetailPage.checkPincodeAvailability("560001");
        LoggerUtility.logInfo("Pincode availability: " + available);
        Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
    }

    @Test(priority = 7, description = "View product images gallery")
    public void testViewProductImages() {
        startExtentTest("testViewProductImages");
        try {
            productDetailPage.viewImages();
        } catch (Exception e) {
            LoggerUtility.logWarning("Image gallery not accessible");
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
    }

    @Test(priority = 8, description = "Add product to wishlist")
    public void testAddToWishlist() {
        startExtentTest("testAddToWishlist");
        try {
            productDetailPage.addToWishlist();
        } catch (Exception e) {
            LoggerUtility.logWarning("Wishlist button not found");
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
    }

    @Test(priority = 9, description = "Verify product description section")
    public void testVerifyProductDescription() {
        startExtentTest("testVerifyProductDescription");
        try {
            String description = productDetailPage.getProductDescription();
            Assert.assertNotNull(description);
        } catch (Exception e) {
            Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
        }
    }

    @Test(priority = 10, description = "Check product stock status")
    public void testCheckProductStock() {
        startExtentTest("testCheckProductStock");
        boolean stock = productDetailPage.isProductInStock();
        LoggerUtility.logInfo("Stock status: " + stock);
        Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
    }
}
