package com.flipkart.automation.pages;

import com.flipkart.automation.config.ConfigReader;
import com.flipkart.automation.utilities.WaitUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for product search results, filters, and sorting.
 */
public class ProductSearchPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProductSearchPage.class);

    private final By sortByDropdown = By.xpath("//div[contains(text(),'Sort By') or contains(text(),'Sort by')]");
    private final By productCards = By.xpath(
            "//div[contains(@class,'_1AtVbE')]//a[contains(@href,'/p/') or contains(@href,'/product/')]"
                    + " | //a[contains(@class,'_1fQZEK') and contains(@href,'/p/')]"
                    + " | //a[contains(@href,'/p/') and @title]");
    private final By noResultsMessage = By.xpath(
            "//div[contains(text(),'Sorry') or contains(text(),'no products') or contains(text(),'No results')]");
    private final By clearAllFilters = By.xpath(
            "//span[normalize-space()='Clear all' or normalize-space()='Clear All' or contains(text(),'Clear')]");

    public ProductSearchPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Applies a minimum and maximum price filter using Flipkart's price range inputs.
     */
    public void filterByPrice(String minPrice, String maxPrice) {
        logger.info("Filtering by price range: {} - {}", minPrice, maxPrice);
        By minInput = By.xpath("//input[contains(@placeholder,'Min') or @name='minPrice']");
        By maxInput = By.xpath("//input[contains(@placeholder,'Max') or @name='maxPrice']");
        sendKeys(minInput, minPrice);
        sendKeys(maxInput, maxPrice);
        logger.info("Price filter applied");
    }

    /**
     * Applies a customer rating filter by star rating label.
     */
    public void filterByRating(String rating) {
        logger.info("Filtering by rating: {}★ & above", rating);
        By ratingLocator = By.xpath(
                "//div[contains(text(),'Customer Ratings')]/following::label[contains(text(),'" + rating + "')]"
                        + " | //label[contains(text(),'" + rating + "★')]");
        scrollToElement(ratingLocator);
        clickElement(ratingLocator);
    }

    /**
     * Applies a brand filter checkbox by brand name.
     */
    public void filterByBrand(String brand) {
        logger.info("Filtering by brand: {}", brand);
        By brandSection = By.xpath("//div[contains(text(),'Brand')]");
        scrollToElement(brandSection);
        By brandCheckbox = By.xpath("//label[contains(text(),'" + brand + "')]");
        clickElement(brandCheckbox);
    }

    /**
     * Sorts search results using Flipkart's custom dropdown (not a native select).
     */
    public void sortBy(String option) {
        logger.info("Sorting results by: {}", option);
        clickElement(sortByDropdown);
        By sortOption = By.xpath(
                "//div[contains(@class,'sort') or contains(@class,'option')][normalize-space()='" + option + "']"
                        + " | //li[normalize-space()='" + option + "']"
                        + " | //div[normalize-space()='" + option + "']");
        clickElement(sortOption);
        logger.info("Sort applied: {}", option);
    }

    /**
     * Returns the number of product cards on the current results page.
     */
    public int getProductCount() {
        logger.info("Counting products on search results page");
        WaitUtility.waitForElementPresence(driver, productCards, ConfigReader.getTimeout());
        List<WebElement> products = driver.findElements(productCards);
        logger.info("Product count: {}", products.size());
        return products.size();
    }

    /**
     * Returns true when zero-results message is shown.
     */
    public boolean isZeroResultsDisplayed() {
        return isElementDisplayed(noResultsMessage);
    }

    /**
     * Clicks the first product card on the results page.
     */
    public void selectFirstProduct() {
        logger.info("Selecting first product from results");
        List<WebElement> products = driver.findElements(productCards);
        if (products.isEmpty()) {
            throw new RuntimeException("No products found on search results page");
        }
        products.get(0).click();
    }

    /**
     * Clicks a product from search results by partial name match in title attribute.
     */
    public void selectProduct(String productName) {
        logger.info("Selecting product matching: {}", productName);
        By productLocator = By.xpath(
                "//a[contains(@title,'" + productName + "') or contains(@href,'" + productName.toLowerCase() + "')]");
        scrollToElement(productLocator);
        clickElement(productLocator);
    }

    /**
     * Navigates to the next page of search results.
     */
    public void nextPage() {
        logger.info("Navigating to next page");
        By nextBtn = By.xpath("//a[contains(@class,'next') or .//span[normalize-space()='Next']]");
        scrollToElement(nextBtn);
        clickElement(nextBtn);
    }

    /**
     * Navigates to the previous page of search results.
     */
    public void previousPage() {
        logger.info("Navigating to previous page");
        By prevBtn = By.xpath("//a[contains(@class,'previous') or normalize-space()='Previous']");
        clickElement(prevBtn);
    }

    /**
     * Clears all applied filters on the search results page.
     */
    public void clearFilters() {
        logger.info("Clearing all filters");
        if (isElementDisplayed(clearAllFilters)) {
            clickElement(clearAllFilters);
        }
    }
}
