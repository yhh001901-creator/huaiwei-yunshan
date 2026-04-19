@echo off
setlocal enabledelayedexpansion

echo [1/4] Building Frontend...
cd frontend
call npm run build
if %errorlevel% neq 0 (
    echo Frontend build failed!
    exit /b 1
)

echo [2/4] Preparing Backend Static Resources...
set STATIC_DIR=..\backend\src\main\resources\static
if exist "%STATIC_DIR%" (
    echo Cleaning old static files...
    rd /s /q "%STATIC_DIR%"
)
mkdir "%STATIC_DIR%"
xcopy /e /y dist\* "%STATIC_DIR%"

echo [3/4] Packaging Backend JAR...
cd ..\backend
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo Backend packaging failed!
    exit /b 1
)

echo [4/4] Done!
echo.
echo Your deployable JAR is located at: backend\target\huaiwei-yunshan-1.0.0.jar
echo You can upload this single file to Alibaba Cloud and run it with:
echo java -jar huaiwei-yunshan-1.0.0.jar
echo.
pause
