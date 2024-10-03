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

:: Drop the database, create the database, and load the SQL file
dropdb -U postgres book-management
if %ERRORLEVEL% neq 0 (
    echo Failed to drop the database
    exit /b %ERRORLEVEL%
)

createdb -U postgres book-management
if %ERRORLEVEL% neq 0 (
    echo Failed to create the database
    exit /b %ERRORLEVEL%
)

psql -U postgres -f .\book-management-v7.sql -d book-management
if %ERRORLEVEL% neq 0 (
    echo Failed to execute the SQL file
    exit /b %ERRORLEVEL%
)

echo Database operations completed successfully.

:: Clear password from environment variable
set PGPASSWORD=

endlocal