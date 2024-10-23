@echo off
setlocal

:: Dr (Drop) Cr (Create) Im (Import)

:: Check if a password argument is provided
if "%~1"=="" (
    echo Usage: %~nx0 [password]
    exit /b 1
)

:: Set the password from the argument
set PGPASSWORD=%~1

:: Check if the database exists
psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'book-management'" | findstr /b "1" >nul
if %ERRORLEVEL% neq 0 (
    echo Database 'book-management' does not exist, skipping drop.
) else (
    echo Dropping existing database 'book-management'...
    dropdb -U postgres book-management
    if %ERRORLEVEL% neq 0 (
        echo Failed to drop the database
        exit /b %ERRORLEVEL%
    )
)

:: Create the database
echo Creating database 'book-management'...
createdb -U postgres book-management
if %ERRORLEVEL% neq 0 (
    echo Failed to create the database
    exit /b %ERRORLEVEL%
)

:: Load the SQL file into the database
echo Importing SQL file into 'book-management'...
psql -U postgres -f .\book-management-v12.sql -d book-management
if %ERRORLEVEL% neq 0 (
    echo Failed to execute the SQL file
    exit /b %ERRORLEVEL%
)

echo Database operations completed successfully.

:: Clear password from environment variable
set PGPASSWORD=

endlocal
