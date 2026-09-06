# Run ProductSearchTests from command line (no Eclipse GUI needed)
$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path $PSScriptRoot -Parent
$Mvn = Join-Path $ProjectRoot ".tools\apache-maven-3.9.6\bin\mvn.cmd"

if (-not (Test-Path $Mvn)) {
    Write-Host "Maven not found. Run scripts\setup-eclipse.ps1 first." -ForegroundColor Red
    exit 1
}

Write-Host "Running ProductSearchTests (guest, no login required)..." -ForegroundColor Cyan
Set-Location $ProjectRoot
& $Mvn clean test -Dtest=ProductSearchTests -Dbrowser=chrome
