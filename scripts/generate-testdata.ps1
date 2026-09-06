$basePath = "c:\Users\dines\OneDrive\Desktop\Automation Project\flipkart-automation-suite\src\test\resources\testdata"
New-Item -ItemType Directory -Force -Path $basePath | Out-Null

Add-Type -AssemblyName System.IO.Compression.FileSystem

function Write-TextFile([string]$Path, [string]$Content) {
    $dir = Split-Path $Path -Parent
    if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    [System.IO.File]::WriteAllText($Path, $Content)
}

function New-ExcelFile {
    param(
        [string]$OutputPath,
        [string]$SheetName,
        [string[][]]$Rows
    )

    $tempDir = Join-Path $env:TEMP ("xlsx_" + [guid]::NewGuid().ToString())
    New-Item -ItemType Directory -Force -Path $tempDir | Out-Null

    $sharedStrings = New-Object System.Collections.Generic.List[string]
    $stringIndex = @{}

    function Get-StringIndex([string]$value) {
        if (-not $stringIndex.ContainsKey($value)) {
            $stringIndex[$value] = $sharedStrings.Count
            [void]$sharedStrings.Add($value)
        }
        return $stringIndex[$value]
    }

    $sheetRows = ""
    for ($r = 0; $r -lt $Rows.Count; $r++) {
        $rowNum = $r + 1
        $cells = ""
        for ($c = 0; $c -lt $Rows[$r].Length; $c++) {
            $colLetter = [char]([int][char]'A' + $c)
            $idx = Get-StringIndex $Rows[$r][$c]
            $cells += "<c r=""$colLetter$rowNum"" t=""s""><v>$idx</v></c>"
        }
        $sheetRows += "<row r=""$rowNum"">$cells</row>"
    }

    $siElements = ($sharedStrings | ForEach-Object { "<si><t>$_</t></si>" }) -join ""

    Write-TextFile "$tempDir\[Content_Types].xml" @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
  <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
  <Override PartName="/xl/sharedStrings.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml"/>
</Types>
"@

    Write-TextFile "$tempDir\_rels\.rels" @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
</Relationships>
"@

    Write-TextFile "$tempDir\xl\workbook.xml" @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
  <sheets><sheet name="$SheetName" sheetId="1" r:id="rId1"/></sheets>
</workbook>
"@

    Write-TextFile "$tempDir\xl\_rels\workbook.xml.rels" @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
  <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings" Target="sharedStrings.xml"/>
</Relationships>
"@

    Write-TextFile "$tempDir\xl\worksheets\sheet1.xml" @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
  <sheetData>$sheetRows</sheetData>
</worksheet>
"@

    Write-TextFile "$tempDir\xl\sharedStrings.xml" @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<sst xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" count="$($sharedStrings.Count)" uniqueCount="$($sharedStrings.Count)">$siElements</sst>
"@

    if (Test-Path $OutputPath) { Remove-Item $OutputPath -Force }
    [System.IO.Compression.ZipFile]::CreateFromDirectory($tempDir, $OutputPath)
    Remove-Item $tempDir -Recurse -Force
}

New-ExcelFile -OutputPath "$basePath\credentials.xlsx" -SheetName "Credentials" -Rows @(
    @("Email", "Password", "ExpectedResult"),
    @("validuser@gmail.com", "ValidPass123", "Success"),
    @("invaliduser@gmail.com", "WrongPass", "Failure"),
    @("blank", "blank", "Empty Fields Error")
)

New-ExcelFile -OutputPath "$basePath\productSearch.xlsx" -SheetName "SearchData" -Rows @(
    @("SearchKeyword", "ExpectedResult", "MinimumResults"),
    @("iPhone", "Apple iPhone", "50"),
    @("shoes", "Footwear products", "100"),
    @("laptop", "Computer devices", "80")
)

New-ExcelFile -OutputPath "$basePath\addressData.xlsx" -SheetName "Addresses" -Rows @(
    @("FullName", "PhoneNumber", "Address", "City", "State", "Pincode"),
    @("Rahul Sharma", "9876543210", "42 MG Road", "Bangalore", "Karnataka", "560001"),
    @("Priya Patel", "9123456780", "15 Park Street", "Mumbai", "Maharashtra", "400001"),
    @("Amit Kumar", "9988776655", "78 Ring Road", "Delhi", "Delhi", "110001")
)

Write-Output "Excel files created at $basePath"
