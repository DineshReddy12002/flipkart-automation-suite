package com.flipkart.automation.pages;

import com.flipkart.automation.config.ConfigReader;
import com.flipkart.automation.utilities.WaitUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Flipkart homepage interactions.
 */
public class HomePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    private final By searchBar = By.name("q");
    private final By searchSuggestions = By.xpath(
            "//div[contains(@class,'suggestion') or contains(@class,'_1crY5')]//li | //ul[contains(@class,'suggestion')]//li");
    private final By accountMenuTrigger = By.xpath(
            "//div[contains(@class,'exehdJ') or .//span[normalize-space()='Login'] or contains(text(),'Hello')]");
    private final By cartLink = By.xpath("//a[contains(@href,'/viewcart')]");
    private final By cartCountBadge = By.xpath("//a[contains(@href,'/viewcart')]//span[contains(@class,'_2KoIT') or string-length(normalize-space())<=2]");
    private final By logoutLink = By.xpath(
            "//div[normalize-space()='Logout' or normalize-space()='Sign Out' or normalize-space()='Log Out']");
    private final By myAccountLink = By.xpath(
            "//div[normalize-space()='My Profile' or normalize-space()='Orders' or normalize-space()='Account']");
    private final By loginLinkInHeader = By.xpath("//span[normalize-space()='Login']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Searches for a product and submits the query.
     */
    public void searchProduct(String productName) {
        logger.info("Searching for product: {}", productName);
        WaitUtility.waitForElementVisibility(driver, searchBar, ConfigReader.getTimeout());
        WebElement searchField = driver.findElement(searchBar);
        searchField.clear();
        searchField.sendKeys(productName);
        searchField.sendKeys(Keys.ENTER);
        logger.info("Search submitted for: {}", productName);
    }

    /**
     * Types in search bar without submitting — used to trigger auto-suggestions.
     */
    public void typeInSearchBar(String productName) {
        logger.info("Typing in search bar: {}", productName);
        WebElement searchField = WaitUtility.waitForElementVisibility(driver, searchBar, ConfigReader.getTimeout());
        searchField.clear();
        searchField.sendKeys(productName);
    }

    /**
     * Returns the number of visible search suggestions.
     */
    public int getSearchSuggestionCount() {
        logger.info("Counting search suggestions");
        List<WebElement> suggestions = driver.findElements(searchSuggestions);
        logger.info("Suggestion count: {}", suggestions.size());
        return suggestions.size();
    }

    /**
     * Returns true if search results page loaded (URL contains search query param).
     */
    public boolean isSearchResultsPageDisplayed() {
        String url = driver.getCurrentUrl();
        return url.contains("q=") || url.contains("sid=");
    }

    /**
     * Selects a category from the navigation menu by visible name.
     */
    public void selectCategory(String categoryName) {
        logger.info("Selecting category: {}", categoryName);
        By categoryLocator = By.xpath("//a[normalize-space()='" + categoryName + "' or contains(text(),'" + categoryName + "')]");
        clickElement(categoryLocator);
    }

    /**
     * Navigates to the shopping cart page.
     */
    public void navigateToCart() {
        logger.info("Navigating to cart");
        clickElement(cartLink);
    }

    /**
     * Returns true if cart page is displayed.
     */
    public boolean isCartPageDisplayed() {
        return driver.getCurrentUrl().contains("viewcart") || driver.getCurrentUrl().contains("cart");
    }

    /**
     * Returns the number of items shown on the cart icon badge.
     */
    public int getCartItemCount() {
        logger.info("Reading cart item count");
        try {
            List<WebElement> badges = driver.findElements(cartCountBadge);
            for (WebElement badge : badges) {
                String text = badge.getText().trim();
                if (!text.isEmpty() && text.matches("\\d+")) {
                    return Integer.parseInt(text);
                }
            }
        } catch (Exception e) {
            logger.warn("Cart count badge not found, returning 0");
        }
        return 0;
    }

    /**
     * Hovers over the account/login menu in the header.
     */
    public void hoverOnAccount() {
        logger.info("Hovering on account menu");
        hoverOverElement(accountMenuTrigger);
    }

    /**
     * Returns true if user appears logged in (Login link absent, account menu present).
     */
    public boolean isUserLoggedIn() {
        logger.info("Checking logged-in state");
        try {
            boolean loginVisible = !driver.findElements(loginLinkInHeader).isEmpty()
                    && driver.findElement(loginLinkInHeader).isDisplayed();
            if (loginVisible) {
                return false;
            }
            hoverOnAccount();
            return isElementDisplayed(myAccountLink) || !driver.getPageSource().contains(">Login<");
        } catch (Exception e) {
            logger.warn("Could not determine login state: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Logs out the current user via the account dropdown menu.
     */
    public void logout() {
        logger.info("Logging out user");
        hoverOnAccount();
        clickElement(logoutLink);
        WaitUtility.waitForElementVisibility(driver, loginLinkInHeader, ConfigReader.getTimeout());
        logger.info("Logout completed");
    }

    /**
     * Navigates to Orders from the account dropdown (requires logged-in session).
     */
    public void navigateToOrders() {
        logger.info("Navigating to Orders via account menu");
        hoverOnAccount();
        By ordersLink = By.xpath("//div[normalize-space()='Orders' or normalize-space()='My Orders']");
        clickElement(ordersLink);
    }
}
