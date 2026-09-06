package com.flipkart.automation.pages;

import com.flipkart.automation.utilities.WaitUtility;
import com.flipkart.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Flipkart login and authentication flows.
 * Models Flipkart's multi-step login: email/mobile → Continue → password → Login.
 */
public class LoginPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    private final By loginModalCloseBtn = By.xpath(
            "//button[normalize-space()='✕'] | //span[normalize-space()='✕'] | //button[contains(@class,'close')]");
    private final By headerLoginLink = By.xpath(
            "//a[contains(@href,'login') or .//span[normalize-space()='Login']]");
    private final By emailOrPhoneInput = By.xpath(
            "//form//input[@type='text' or @type='email' or contains(@class,'r4vIwl')]");
    private final By continueButton = By.xpath(
            "//button[.//span[contains(text(),'Continue')] or contains(normalize-space(),'Continue')]");
    private final By useEmailPasswordLink = By.xpath(
            "//span[contains(text(),'Use Email and Password') or contains(text(),'Use Email & Password')]");
    private final By passwordInput = By.xpath("//input[@type='password']");
    private final By loginSubmitButton = By.xpath(
            "//button[.//span[normalize-space()='Login'] or contains(normalize-space(),'Login')]");
    private final By errorMessage = By.xpath(
            "//span[contains(@class,'error') or contains(@class,'Error') or contains(text(),'incorrect') "
                    + "or contains(text(),'valid') or contains(text(),'Please enter')]");
    private final By forgotPasswordLink = By.xpath(
            "//a[contains(text(),'Forgot') or contains(text(),'forgot')]");
    private final By signupLink = By.xpath(
            "//span[contains(text(),'New to Flipkart')]/following::a[1] | //a[contains(text(),'Sign Up')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Closes the login modal so guest users can browse/search without obstruction.
     */
    public void closeBlockingLoginModal() {
        logger.info("Closing blocking login modal if present");
        try {
            List<WebElement> closeButtons = driver.findElements(loginModalCloseBtn);
            for (WebElement btn : closeButtons) {
                if (btn.isDisplayed()) {
                    btn.click();
                    logger.info("Closed login modal");
                    WaitUtility.waitForElementInvisibility(driver, emailOrPhoneInput, 5);
                    return;
                }
            }
        } catch (Exception e) {
            logger.debug("No blocking login modal to close");
        }
    }

    /**
     * Opens the login modal from the header when it is not already visible.
     */
    public void openLoginModal() {
        logger.info("Opening login modal");
        if (!isLoginModalDisplayed()) {
            clickElement(headerLoginLink);
        }
        WaitUtility.waitForElementVisibility(driver, emailOrPhoneInput, ConfigReader.getTimeout());
        logger.info("Login modal is open");
    }

    /**
     * Enters email or mobile number on the first login step.
     */
    public void enterEmailOrPhone(String emailOrPhone) {
        logger.info("Entering email/phone: {}", emailOrPhone);
        sendKeys(emailOrPhoneInput, emailOrPhone);
    }

    /**
     * Clicks Continue after entering email/phone.
     */
    public void clickContinue() {
        logger.info("Clicking Continue on login form");
        clickElement(continueButton);
    }

    /**
     * Selects password-based login when Flipkart shows OTP screen.
     */
    public void selectPasswordLoginOption() {
        logger.info("Selecting 'Use Email and Password' option");
        try {
            if (isElementDisplayed(useEmailPasswordLink)) {
                clickElement(useEmailPasswordLink);
            }
        } catch (Exception e) {
            logger.debug("Password login option not shown — password field may already be visible");
        }
    }

    /**
     * Enters password on the login form.
     */
    public void enterPassword(String password) {
        logger.info("Entering password");
        WaitUtility.waitForElementVisibility(driver, passwordInput, ConfigReader.getTimeout());
        sendKeys(passwordInput, password);
    }

    /**
     * Submits the login form.
     */
    public void submitLogin() {
        logger.info("Submitting login form");
        clickElement(loginSubmitButton);
    }

    /**
     * Performs the full Flipkart login flow with email and password.
     */
    public void loginWithCredentials(String email, String password) {
        logger.info("Starting full login flow for: {}", email);
        openLoginModal();

        if (email != null && !email.isBlank()) {
            enterEmailOrPhone(email);
            clickContinue();
        }

        selectPasswordLoginOption();

        if (password != null && !password.isBlank()) {
            enterPassword(password);
            submitLogin();
        } else if (email == null || email.isBlank()) {
            clickContinue();
        }

        logger.info("Login flow completed for: {}", email);
    }

    /**
     * Opens signup form from the login modal.
     */
    public void openSignupForm() {
        logger.info("Opening signup form");
        openLoginModal();
        clickElement(signupLink);
    }

    /**
     * Returns visible login error message text.
     */
    public String getErrorMessage() {
        logger.info("Fetching login error message");
        return getText(errorMessage);
    }

    /**
     * Returns true if a login error message is visible.
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return isElementDisplayed(errorMessage);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clicks the forgot password link.
     */
    public void clickForgotPassword() {
        logger.info("Clicking forgot password link");
        openLoginModal();
        clickElement(forgotPasswordLink);
    }

    /**
     * Checks whether the login modal/form is displayed.
     */
    public boolean isLoginModalDisplayed() {
        try {
            return driver.findElement(emailOrPhoneInput).isDisplayed()
                    || driver.findElement(headerLoginLink).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @deprecated Use {@link #closeBlockingLoginModal()} or {@link #isLoginModalDisplayed()}
     */
    @Deprecated
    public void dismissLoginPopupIfPresent() {
        closeBlockingLoginModal();
    }

    /**
     * @deprecated Use {@link #isLoginModalDisplayed()}
     */
    @Deprecated
    public boolean isLoginPageDisplayed() {
        return isLoginModalDisplayed();
    }
}
