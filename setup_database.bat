@echo off
echo Setting up Transport Management System Database...

:: Check if MySQL is installed
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: MySQL is not installed or not in PATH
    echo Please install MySQL and add it to your PATH
    pause
    exit /b 1
)

:: Run the SQL script
echo Running database setup script...
mysql -u root -pAnkit@123 < setup_database.sql

if %errorlevel% neq 0 (
    echo Error: Database setup failed
    pause
    exit /b 1
)

echo Database setup completed successfully!
pause 