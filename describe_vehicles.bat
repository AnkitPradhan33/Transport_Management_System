@echo off
echo Checking vehicles table structure...

:: Run the SQL command
mysql -u root -pAnkit@123 transport_db -e "DESCRIBE vehicles;"

if %errorlevel% neq 0 (
    echo Error: Failed to describe table
    pause
    exit /b 1
)

pause 