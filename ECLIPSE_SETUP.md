# Flipkart Automation Suite — Eclipse Setup

## Step 1 — Install TestNG Plugin

1. **Help** → **Eclipse Marketplace** → search **TestNG** → Install → Restart

## Step 2 — Import Project

1. **File** → **Import** → **Maven** → **Existing Maven Projects**
2. Browse to: `flipkart-automation-suite` folder (where `pom.xml` lives)
3. Click **Finish** → wait for Maven dependencies to download

## Step 3 — Generate Test Data (required once)

Open PowerShell in the project folder and run:
```powershell
powershell -ExecutionPolicy Bypass -File scripts\generate-testdata.ps1
```

This creates Excel files in `src/test/resources/testdata/`.

## Step 4 — Set Your Flipkart Credentials (required for login/order tests)

Edit `src/test/resources/properties/config.properties`:
```properties
email=your.real.flipkart.email@gmail.com
password=YourFlipkartPassword
```

> Login tests **will fail** with placeholder credentials. Search/cart tests work as guest.

## Step 5 — Run Tests in Eclipse

| What to run | How |
|-------------|-----|
| Full suite (73 tests) | Right-click `testng.xml` → **Run As** → **TestNG Suite** |
| Search tests only | Right-click `ProductSearchTests.java` → **Run As** → **TestNG Test** |
| Login tests only | Right-click `AuthenticationTests.java` → **Run As** → **TestNG Test** |

## Step 6 — View Reports

- **ExtentReport:** `test-output/ExtentReport.html`
- **Logs:** `logs/automation.log`
- **Screenshots on failure:** `test-output/screenshots/`

## Troubleshooting

| Problem | Fix |
|---------|-----|
| Maven dependencies red | Right-click project → **Maven** → **Update Project** (Force Update) |
| Excel FileNotFoundException | Run `scripts\generate-testdata.ps1` |
| Login tests fail | Add real Flipkart credentials to `config.properties` |
| OTP popup during login | Flipkart may require OTP — use an account with password login enabled |
| Search tests fail | Flipkart locators may have changed — update page classes |
| TestNG missing | Install TestNG plugin and restart Eclipse |
