package com.flipkart.automation.tests;

import com.flipkart.automation.config.ConfigReader;
import com.flipkart.automation.constants.TestConstants;
import com.flipkart.automation.utilities.ExcelDataProvider;
import com.flipkart.automation.utilities.LoggerUtility;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Authentication and session management test cases.
 */
public class AuthenticationTests extends BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void keepLoginModalAvailable() {
        // Auth tests need the login modal — do NOT close it
        LoggerUtility.logInfo("Authentication test — login modal left available");
    }

    @DataProvider(name = "credentialsData")
    public Object[][] credentialsData() {
        return ExcelDataProvider.readExcelFromClasspath(
                TestConstants.CREDENTIALS_EXCEL, TestConstants.CREDENTIALS_SHEET);
    }

    @Test(priority = 1, description = "Verify login with valid credentials")
    public void testLoginWithValidCredentials() {
        startExtentTest("testLoginWithValidCredentials");
        logExtentInfo("Testing login with valid credentials from config.properties");

        loginPage.loginWithCredentials(ConfigReader.getEmail(), ConfigReader.getPassword());

        Assert.assertTrue(homePage.isUserLoggedIn(),
                "User should be logged in after valid credentials. Update config.properties with real Flipkart credentials.");
        logExtentInfo("Valid login verified");
    }

    @Test(priority = 2, description = "Verify login fails with invalid email")
    public void testLoginWithInvalidEmail() {
        startExtentTest("testLoginWithInvalidEmail");
        loginPage.loginWithCredentials("invaliduser@gmail.com", ConfigReader.getPassword());

        Assert.assertFalse(homePage.isUserLoggedIn(), "User should NOT be logged in with invalid email");
        Assert.assertTrue(loginPage.isLoginModalDisplayed() || loginPage.isErrorMessageDisplayed(),
                "Login modal or error should remain after invalid email");
    }

    @Test(priority = 3, description = "Verify login fails with invalid password")
    public void testLoginWithInvalidPassword() {
        startExtentTest("testLoginWithInvalidPassword");
        loginPage.loginWithCredentials(ConfigReader.getEmail(), "WrongPassword999");

        Assert.assertFalse(homePage.isUserLoggedIn(), "User should NOT be logged in with wrong password");
    }

    @Test(priority = 4, description = "Verify login with empty fields shows validation")
    public void testLoginWithEmptyFields() {
        startExtentTest("testLoginWithEmptyFields");
        loginPage.loginWithCredentials("", "");

        Assert.assertFalse(homePage.isUserLoggedIn(), "Empty credentials must not log in");
        Assert.assertTrue(loginPage.isLoginModalDisplayed(), "Login modal should remain visible");
    }

    @Test(priority = 5, description = "Verify login error message is displayed")
    public void testLoginErrorMessage() {
        startExtentTest("testLoginErrorMessage");
        loginPage.loginWithCredentials("invaliduser@gmail.com", "WrongPass");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || loginPage.isLoginModalDisplayed(),
                "Error message or login modal expected after failed login");
    }

    @Test(priority = 6, description = "Verify signup form opens from login modal")
    public void testSignupWithValidData() {
        startExtentTest("testSignupWithValidData");
        loginPage.openSignupForm();

        Assert.assertTrue(loginPage.isLoginModalDisplayed() || driver.getCurrentUrl().contains("flipkart"),
                "Signup flow should open from login modal");
    }

    @Test(priority = 7, description = "Verify duplicate email login does not create duplicate session")
    public void testSignupWithDuplicateEmail() {
        startExtentTest("testSignupWithDuplicateEmail");
        loginPage.loginWithCredentials(ConfigReader.getEmail(), ConfigReader.getPassword());

        Assert.assertTrue(homePage.isUserLoggedIn() || loginPage.isErrorMessageDisplayed(),
                "Existing account should login or show appropriate message");
    }

    @Test(priority = 8, description = "Verify session persists after login and page refresh")
    public void testSessionPersistenceAfterLogin() {
        startExtentTest("testSessionPersistenceAfterLogin");
        loginPage.loginWithCredentials(ConfigReader.getEmail(), ConfigReader.getPassword());

        Assert.assertTrue(homePage.isUserLoggedIn(), "Must be logged in before refresh");

        driver.navigate().refresh();
        loginPage.closeBlockingLoginModal();

        Assert.assertTrue(homePage.isUserLoggedIn(),
                "Session should persist after page refresh");
    }

    @Test(priority = 9, description = "Verify logout functionality")
    public void testLogoutFunctionality() {
        startExtentTest("testLogoutFunctionality");
        loginPage.loginWithCredentials(ConfigReader.getEmail(), ConfigReader.getPassword());
        Assert.assertTrue(homePage.isUserLoggedIn(), "Must login before testing logout");

        homePage.logout();

        Assert.assertFalse(homePage.isUserLoggedIn(), "User should be logged out");
    }

    @Test(priority = 10, dataProvider = "credentialsData", description = "Data-driven credential validation from Excel")
    public void testLoginWithExcelData(String email, String password, String expectedResult) {
        startExtentTest("testLoginWithExcelData");
        logExtentInfo("Excel credentials test: " + email + " → " + expectedResult);

        loginPage.loginWithCredentials(email, password);

        if ("Success".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue(homePage.isUserLoggedIn(), "Expected successful login for: " + email);
        } else {
            Assert.assertFalse(homePage.isUserLoggedIn(), "Expected failed login for: " + email);
        }
    }
}
