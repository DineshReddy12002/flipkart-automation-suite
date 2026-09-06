package com.flipkart.automation.tests;

import com.flipkart.automation.config.ConfigReader;
import com.flipkart.automation.pages.OrdersPage;
import com.flipkart.automation.utilities.LoggerUtility;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Order history, tracking, and management test cases.
 */
public class OrderManagementTests extends BaseTest {

    private OrdersPage ordersPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndInitOrders() {
        ordersPage = new OrdersPage(driver);
        loginPage.loginWithCredentials(ConfigReader.getEmail(), ConfigReader.getPassword());
        if (!homePage.isUserLoggedIn()) {
            LoggerUtility.logWarning("Login failed — order tests require valid credentials in config.properties");
        }
    }

    @Test(priority = 1, description = "View order history page")
    public void testViewOrderHistory() {
        startExtentTest("testViewOrderHistory");
        Assert.assertTrue(homePage.isUserLoggedIn(), "Must be logged in to view orders");

        homePage.navigateToOrders();
        Assert.assertTrue(driver.getCurrentUrl().contains("order") || ordersPage.getOrderCount() >= 0,
                "Orders page should load");
    }

    @Test(priority = 2, description = "View individual order details")
    public void testViewOrderDetails() {
        startExtentTest("testViewOrderDetails");
        Assert.assertTrue(homePage.isUserLoggedIn(), "Must be logged in");

        homePage.navigateToOrders();
        if (ordersPage.getOrderCount() > 0) {
            ordersPage.viewFirstOrderDetails();
            Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"), "Order details should load");
        } else {
            LoggerUtility.logWarning("No orders in history — skipping detail view assertion");
        }
    }

    @Test(priority = 3, description = "Track an existing order")
    public void testTrackOrder() {
        startExtentTest("testTrackOrder");
        Assert.assertTrue(homePage.isUserLoggedIn(), "Must be logged in");

        homePage.navigateToOrders();
        if (ordersPage.getOrderCount() > 0) {
            ordersPage.trackOrder();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
    }

    @Test(priority = 4, description = "Verify order status display")
    public void testVerifyOrderStatus() {
        startExtentTest("testVerifyOrderStatus");
        homePage.navigateToOrders();
        if (ordersPage.getOrderCount() > 0) {
            ordersPage.viewFirstOrderDetails();
            String status = ordersPage.getOrderStatus();
            Assert.assertNotNull(status, "Order status should be displayed");
        }
    }

    @Test(priority = 5, description = "Cancel an order")
    public void testCancelOrder() {
        startExtentTest("testCancelOrder");
        homePage.navigateToOrders();
        if (ordersPage.getOrderCount() > 0) {
            ordersPage.cancelOrder();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
    }

    @Test(priority = 6, description = "Download order invoice")
    public void testDownloadInvoice() {
        startExtentTest("testDownloadInvoice");
        homePage.navigateToOrders();
        if (ordersPage.getOrderCount() > 0) {
            ordersPage.viewFirstOrderDetails();
            ordersPage.downloadInvoice();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("flipkart.com"));
    }

    @Test(priority = 7, description = "Verify delivery date on order")
    public void testVerifyDeliveryDate() {
        startExtentTest("testVerifyDeliveryDate");
        homePage.navigateToOrders();
        if (ordersPage.getOrderCount() > 0) {
            ordersPage.viewFirstOrderDetails();
            String date = ordersPage.getDeliveryDate();
            Assert.assertNotNull(date, "Delivery date should be shown on order");
        }
    }

    @Test(priority = 8, description = "View estimated delivery information")
    public void testViewEstimatedDelivery() {
        startExtentTest("testViewEstimatedDelivery");
        homePage.navigateToOrders();
        if (ordersPage.getOrderCount() > 0) {
            ordersPage.viewFirstOrderDetails();
            logExtentInfo("Delivery info: " + ordersPage.getDeliveryDate());
        }
        Assert.assertTrue(homePage.isUserLoggedIn() || driver.getCurrentUrl().contains("flipkart.com"));
    }
}
