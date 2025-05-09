@echo off
echo Checking Java installation...

:: Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install JDK and add it to your PATH
    pause
    exit /b 1
)

echo Checking project structure...

:: Create bin directory if it doesn't exist
if not exist bin mkdir bin

:: Check if MySQL JDBC driver exists
if not exist lib\mysql-connector-j-8.0.33.jar (
    echo Error: MySQL JDBC driver not found
    echo Please download mysql-connector-j-8.0.33.jar and place it in the lib folder
    pause
    exit /b 1
)

echo Compiling Transport Management System...

:: Clean bin directory
del /Q bin\*.class

:: Compile the Java files
javac -cp "lib/*" -d bin *.java
if %errorlevel% neq 0 (
    echo Error: Compilation failed
    pause
    exit /b 1
)

:: Run the application with detailed error reporting
echo Running Transport Management System...
java -cp "bin;lib/*" -Djava.util.logging.config.file=logging.properties TransportManagementSystem
if %errorlevel% neq 0 (
    echo Error: Application failed to start
    pause
    exit /b 1
)

pause 