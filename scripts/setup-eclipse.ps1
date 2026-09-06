# Automated Eclipse + project setup for Flipkart Automation Suite
# Run: powershell -ExecutionPolicy Bypass -File scripts\setup-eclipse.ps1

$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path $PSScriptRoot -Parent
$EclipseHome = "C:\Users\dines\eclipse\java-2024-033\eclipse"
$EclipseExe = Join-Path $EclipseHome "eclipse.exe"
$Workspace = Join-Path $ProjectRoot ".eclipse-workspace"
$Mvn = Join-Path $ProjectRoot ".tools\apache-maven-3.9.6\bin\mvn.cmd"

Write-Host "=== Flipkart Automation Suite - Eclipse Setup ===" -ForegroundColor Cyan

# 1. Generate Excel test data
Write-Host "[1/4] Generating Excel test data..." -ForegroundColor Yellow
& (Join-Path $PSScriptRoot "generate-testdata.ps1")

# 2. Install TestNG Eclipse plugin (if not already installed)
Write-Host "[2/4] Installing TestNG plugin into Eclipse..." -ForegroundColor Yellow
if (Test-Path $EclipseExe) {
    cmd /c "`"$EclipseExe`" -nosplash -application org.eclipse.equinox.p2.director -repository https://testng.org/testng-eclipse-update -installIU org.testng.eclipse.feature.group -destination `"$EclipseHome`" -profileProperties org.eclipse.update.install.features=true 2>nul"
    Write-Host "      TestNG plugin install attempted (restart Eclipse if prompted)." -ForegroundColor Green
} else {
    Write-Host "      Eclipse not found at $EclipseExe — install TestNG manually from Marketplace." -ForegroundColor Red
}

# 3. Import project into Eclipse workspace
Write-Host "[3/4] Importing Maven project into Eclipse workspace..." -ForegroundColor Yellow
if (Test-Path $EclipseExe) {
    if (-not (Test-Path $Workspace)) { New-Item -ItemType Directory -Path $Workspace | Out-Null }
    cmd /c "`"$EclipseExe`" -nosplash -data `"$Workspace`" -import `"$ProjectRoot`" -addWorkspace 2>nul"
    Write-Host "      Project imported to workspace: $Workspace" -ForegroundColor Green
} else {
    Write-Host "      Skipped — Eclipse not found." -ForegroundColor Red
}

# 4. Remind about credentials
Write-Host "[4/4] Credentials check..." -ForegroundColor Yellow
$configFile = Join-Path $ProjectRoot "src\test\resources\properties\config.properties"
$config = Get-Content $configFile -Raw
if ($config -match "test\.user@gmail\.com") {
    Write-Host "      WARNING: Placeholder credentials still in config.properties" -ForegroundColor Red
    Write-Host "      Edit $configFile with your real Flipkart email/password for login tests." -ForegroundColor Red
} else {
    Write-Host "      Credentials appear customized." -ForegroundColor Green
}

Write-Host ""
Write-Host "=== Setup Complete ===" -ForegroundColor Cyan
Write-Host "Open Eclipse with workspace: $Workspace"
Write-Host "Run search tests: powershell -ExecutionPolicy Bypass -File scripts\run-search-tests.ps1"
Write-Host "Run login tests:  powershell -ExecutionPolicy Bypass -File scripts\run-login-tests.ps1"

# 5. Download Maven if missing (for CLI test runs)
if (-not (Test-Path $Mvn)) {
    Write-Host "[5/5] Downloading Maven for command-line test runs..." -ForegroundColor Yellow
    $toolsDir = Join-Path $ProjectRoot ".tools"
    $zip = Join-Path $toolsDir "maven.zip"
    Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip" -OutFile $zip
    Expand-Archive -Path $zip -DestinationPath $toolsDir -Force
    Write-Host "      Maven installed to .tools\" -ForegroundColor Green
}
