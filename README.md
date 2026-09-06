# Flipkart Automation Suite

A comprehensive Selenium WebDriver automation framework for the [Flipkart](https://www.flipkart.com) e-commerce platform, built with Java, TestNG, and the Page Object Model (POM) design pattern.

## Features

- **70+ automated test cases** covering authentication, search, product details, cart, checkout, and order management
- **Page Object Model** architecture for maintainable and reusable page classes
- **Data-driven testing** with Apache POI Excel integration
- **Parallel execution** via TestNG (3 threads)
- **HTML reporting** with ExtentReports
- **Logging** with Log4j2 (console + file)
- **Automatic driver management** with WebDriverManager
- **Screenshot capture** on test failure
- **Multi-browser support** — Chrome, Firefox, Edge

## Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 11 or higher |
| Apache Maven | 3.6 or higher |
| Git | Latest |
| Internet connection | Required for WebDriverManager and Flipkart access |

## Installation

1. **Clone or download the project**
   ```bash
   git clone <repository-url>
   cd flipkart-automation-suite
   ```

2. **Verify Java and Maven**
   ```bash
   java -version
   mvn -version
   ```

3. **Install dependencies**
   ```bash
   mvn clean install -DskipTests
   ```

4. **Configure test settings** (optional)

   Edit `src/test/resources/properties/config.properties`:
   ```properties
   baseURL=https://www.flipkart.com
   browser=chrome
   email=your.test.email@gmail.com
   password=YourTestPassword
   ```

## Running Tests

### Run the full suite
```bash
mvn clean test
```

### Run a specific test class
```bash
mvn test -Dtest=AuthenticationTests
mvn test -Dtest=ProductSearchTests
mvn test -Dtest=ShoppingCartTests
```

### Run with a different browser
```bash
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

### Run with TestNG XML directly
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/suites/testng.xml
```

## Project Structure

```
flipkart-automation-suite/
├── src/main/java/com/flipkart/automation/
│   ├── pages/              # Page Object classes
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   ├── HomePage.java
│   │   ├── ProductSearchPage.java
│   │   ├── ProductDetailPage.java
│   │   ├── CartPage.java
│   │   ├── CheckoutPage.java
│   │   ├── OrderConfirmationPage.java
│   │   └── OrdersPage.java
│   ├── utilities/          # Helper classes
│   │   ├── DriverManager.java
│   │   ├── WaitUtility.java
│   │   ├── LoggerUtility.java
│   │   ├── ScreenshotUtility.java
│   │   ├── ExcelDataProvider.java
│   │   ├── CommonMethods.java
│   │   └── ExtentReportManager.java
│   ├── config/             # Configuration
│   │   └── ConfigReader.java
│   └── constants/          # Enums & Constants
│       ├── BrowserType.java
│       └── TestConstants.java
├── src/test/java/com/flipkart/automation/tests/
│   ├── BaseTest.java
│   ├── TestListener.java
│   ├── AuthenticationTests.java       (10 tests)
│   ├── ProductSearchTests.java        (16 tests)
│   ├── ProductDetailTests.java        (10 tests)
│   ├── ShoppingCartTests.java         (15 tests)
│   ├── CheckoutTests.java             (14 tests)
│   └── OrderManagementTests.java      (8 tests)
├── src/test/resources/
│   ├── testdata/           # Excel test data
│   │   ├── credentials.xlsx
│   │   ├── productSearch.xlsx
│   │   └── addressData.xlsx
│   ├── properties/
│   │   └── config.properties
│   ├── suites/
│   │   └── testng.xml
│   └── log4j2.xml
├── pom.xml
└── README.md
```

## Test Coverage Summary

| Test Class | Test Count | Coverage Area |
|------------|-----------|---------------|
| AuthenticationTests | 10 | Login, signup, session, logout, Excel credentials |
| ProductSearchTests | 16 | Search, filters, sort, pagination, Excel data |
| ProductDetailTests | 10 | Price, rating, pincode, images, wishlist, stock |
| ShoppingCartTests | 15 | Add/remove items, quantity, coupons, totals |
| CheckoutTests | 14 | Address, payment methods, order placement |
| OrderManagementTests | 8 | Order history, tracking, cancel, invoice |
| **Total** | **73** | End-to-end e-commerce flows |

## Reports & Logs

After test execution, find outputs at:

| Output | Location |
|--------|----------|
| ExtentReports HTML | `test-output/ExtentReport.html` |
| TestNG report | `target/surefire-reports/` |
| Application logs | `logs/automation.log` |
| Failure screenshots | `test-output/screenshots/` |

## Screenshots of Test Execution

> Run `mvn clean test` and open `test-output/ExtentReport.html` in a browser to view the interactive HTML report with pass/fail status, logs, and embedded screenshots.

Example execution:
```
[INFO] Tests run: 73, Failures: 0, Errors: 0, Skipped: 0
```

## Known Issues

1. **Dynamic locators** — Flipkart frequently updates its DOM structure. Locators in page classes may need periodic maintenance.
2. **Login popup** — Flipkart shows a login modal on first visit; tests attempt to dismiss it automatically.
3. **CAPTCHA / OTP** — Real login and checkout flows may require manual OTP verification; use test accounts where possible.
4. **Rate limiting** — Running all 73 tests in parallel against the live site may trigger bot detection; consider reducing `thread-count` in `testng.xml`.
5. **ExtentReports 6.x** — Version 6.x is not available on Maven Central; this project uses **5.1.2** (latest stable).

## Contributing Guidelines

1. Fork the repository and create a feature branch.
2. Follow existing naming conventions (PascalCase for classes, camelCase for methods).
3. Add Javadoc to all new public methods.
4. Use explicit waits — never use `Thread.sleep()`.
5. Keep tests independent; each test must run in isolation.
6. Update locators in page classes, not in test classes.
7. Run `mvn clean test` before submitting a pull request.
8. Add logging for every major test step.

## Author

**[Your Name]**

---

Built with Selenium WebDriver 4.x | TestNG 7.x | Maven | Page Object Model
