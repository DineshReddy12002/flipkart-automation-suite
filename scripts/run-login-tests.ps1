# Run AuthenticationTests — requires real credentials in config.properties
$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path $PSScriptRoot -Parent
$Mvn = Join-Path $ProjectRoot ".tools\apache-maven-3.9.6\bin\mvn.cmd"
$ConfigFile = Join-Path $ProjectRoot "src\test\resources\properties\config.properties"

if (-not (Test-Path $Mvn)) {
    Write-Host "Maven not found. Run setup first." -ForegroundColor Red
    exit 1
}

$config = Get-Content $ConfigFile -Raw
if ($config -match "test\.user@gmail\.com") {
    Write-Host "ERROR: Update config.properties with your real Flipkart credentials before running login tests." -ForegroundColor Red
    exit 1
}

Write-Host "Running AuthenticationTests..." -ForegroundColor Cyan
Set-Location $ProjectRoot
& $Mvn clean test -Dtest=AuthenticationTests -Dbrowser=chrome
