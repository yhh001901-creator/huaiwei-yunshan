@echo off

rem Stop server on port 8083
echo Stopping server on port 8083...
echo.

rem Find processes on port 8083
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8083') do (
    echo Found process PID: %%a
    taskkill /F /PID %%a
    if %%errorlevel%% equ 0 (
        echo Successfully stopped PID %%a
    ) else (
        echo Failed to stop PID %%a
    )
)

echo.
echo Verifying port 8083 is released...
ping 127.0.0.1 -n 3 >nul

netstat -ano | findstr :8083
if %%errorlevel%% equ 0 (
    echo Warning: Port 8083 is still in use
) else (
    echo Success: Port 8083 is released
)

echo.
echo Operation completed. Press any key to exit...
pause >nul