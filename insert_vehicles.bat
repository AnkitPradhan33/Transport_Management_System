@echo off
echo Creating vehicles table and inserting data...

:: Run the SQL script
mysql -u root -pAnkit@123 < insert_vehicles.sql

if %errorlevel% neq 0 (
    echo Error: Failed to create table or insert data
    pause
    exit /b 1
)

echo Vehicles table created and data inserted successfully!
pause 