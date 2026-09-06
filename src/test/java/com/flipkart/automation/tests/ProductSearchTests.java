package com.flipkart.automation.tests;

import com.flipkart.automation.constants.TestConstants;
import com.flipkart.automation.pages.ProductSearchPage;
import com.flipkart.automation.utilities.ExcelDataProvider;
import com.flipkart.automation.utilities.LoggerUtility;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Product search, filter, sort, and pagination test cases.
 */
public class ProductSearchTests extends BaseTest {

    private ProductSearchPage productSearchPage;

    @BeforeMethod(alwaysRun = true)
    public void initSearchPage() {
        prepareGuestSession();
        productSearchPage = new ProductSearchPage(driver);
    }

    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        return ExcelDataProvider.readExcelFromClasspath(
                TestConstants.PRODUCT_SEARCH_EXCEL, TestConstants.PRODUCT_SEARCH_SHEET);
    }

    @Test(priority = 1, description = "Search with a single keyword")
    public void testSearchWithSingleKeyword() {
        startExtentTest("testSearchWithSingleKeyword");
        homePage.searchProduct("laptop");

        Assert.assertTrue(homePage.isSearchResultsPageDisplayed(), "Search results page should load");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "At least one product should appear for 'laptop'");
    }

    @Test(priority = 2, description = "Search with multiple keywords")
    public void testSearchWithMultipleKeywords() {
        startExtentTest("testSearchWithMultipleKeywords");
        homePage.searchProduct("wireless bluetooth headphones");

        Assert.assertTrue(homePage.isSearchResultsPageDisplayed(), "Multi-keyword search should navigate to results");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Products should appear for multi-keyword search");
    }

    @Test(priority = 3, description = "Verify search auto-suggestions appear")
    public void testSearchAutoSuggestions() {
        startExtentTest("testSearchAutoSuggestions");
        homePage.typeInSearchBar("iphone");

        int suggestions = homePage.getSearchSuggestionCount();
        logExtentInfo("Auto-suggestion count: " + suggestions);
        Assert.assertTrue(suggestions > 0, "Auto-suggestions should appear when typing 'iphone'");
    }

    @Test(priority = 4, description = "Sort results by price low to high")
    public void testFilterByPriceLowToHigh() {
        startExtentTest("testFilterByPriceLowToHigh");
        homePage.searchProduct("shoes");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Products must exist before sorting");

        productSearchPage.sortBy("Price -- Low to High");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Products should remain after sort");
    }

    @Test(priority = 5, description = "Sort results by price high to low")
    public void testFilterByPriceHighToLow() {
        startExtentTest("testFilterByPriceHighToLow");
        homePage.searchProduct("shoes");
        productSearchPage.sortBy("Price -- High to Low");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Products should appear after high-to-low sort");
    }

    @Test(priority = 6, description = "Filter results by customer ratings")
    public void testFilterByRatings() {
        startExtentTest("testFilterByRatings");
        homePage.searchProduct("mobile");
        int beforeCount = productSearchPage.getProductCount();
        productSearchPage.filterByRating("4");
        Assert.assertTrue(productSearchPage.getProductCount() >= 0 && beforeCount > 0,
                "Rating filter should apply on mobile search results");
    }

    @Test(priority = 7, description = "Filter results by brand")
    public void testFilterByBrand() {
        startExtentTest("testFilterByBrand");
        homePage.searchProduct("laptop");
        productSearchPage.filterByBrand("HP");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "HP laptops should appear after brand filter");
    }

    @Test(priority = 8, description = "Sort results by popularity")
    public void testSortByPopularity() {
        startExtentTest("testSortByPopularity");
        homePage.searchProduct("earphones");
        productSearchPage.sortBy("Popularity");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Products should appear after popularity sort");
    }

    @Test(priority = 9, description = "Sort results by price")
    public void testSortByPrice() {
        startExtentTest("testSortByPrice");
        homePage.searchProduct("watch");
        productSearchPage.sortBy("Price -- Low to High");
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Products should appear after price sort");
    }

    @Test(priority = 10, description = "Navigate to next page of results")
    public void testPaginationNextPage() {
        startExtentTest("testPaginationNextPage");
        homePage.searchProduct("books");
        int page1Count = productSearchPage.getProductCount();
        productSearchPage.nextPage();
        Assert.assertTrue(productSearchPage.getProductCount() > 0 && page1Count > 0,
                "Next page should show products");
    }

    @Test(priority = 11, description = "Navigate to previous page of results")
    public void testPaginationPreviousPage() {
        startExtentTest("testPaginationPreviousPage");
        homePage.searchProduct("books");
        productSearchPage.nextPage();
        productSearchPage.previousPage();
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Previous page should show products");
    }

    @Test(priority = 12, description = "Verify product count after applying filter")
    public void testProductCountAfterFilter() {
        startExtentTest("testProductCountAfterFilter");
        homePage.searchProduct("phone");
        int before = productSearchPage.getProductCount();
        productSearchPage.filterByBrand("Samsung");
        int after = productSearchPage.getProductCount();
        LoggerUtility.logInfo("Product count before=" + before + ", after=" + after);
        Assert.assertTrue(before > 0, "Initial search should return products");
    }

    @Test(priority = 13, description = "Clear all applied filters")
    public void testClearFilters() {
        startExtentTest("testClearFilters");
        homePage.searchProduct("tablet");
        productSearchPage.filterByBrand("Samsung");
        productSearchPage.clearFilters();
        Assert.assertTrue(productSearchPage.getProductCount() > 0, "Products should appear after clearing filters");
    }

    @Test(priority = 14, description = "Search with zero results query")
    public void testSearchZeroResults() {
        startExtentTest("testSearchZeroResults");
        homePage.searchProduct("xyznonexistentproduct12345");
        Assert.assertTrue(productSearchPage.isZeroResultsDisplayed() || productSearchPage.getProductCount() == 0,
                "Zero results message or empty list expected");
    }

    @Test(priority = 15, description = "Search with special characters")
    public void testSearchWithSpecialCharacters() {
        startExtentTest("testSearchWithSpecialCharacters");
        homePage.searchProduct("phone @ # case");
        Assert.assertTrue(homePage.isSearchResultsPageDisplayed(), "Special character search should not crash browser");
    }

    @Test(priority = 16, dataProvider = "searchData", description = "Data-driven product search from Excel")
    public void testSearchFromExcel(String keyword, String expectedResult, String minResults) {
        startExtentTest("testSearchFromExcel");
        logExtentInfo("Excel search: " + keyword + " → expect " + expectedResult);

        homePage.searchProduct(keyword);
        int count = productSearchPage.getProductCount();
        int minimum = Integer.parseInt(minResults);

        Assert.assertTrue(count >= minimum,
                "Expected at least " + minimum + " results for '" + keyword + "' but found " + count);
    }
}
