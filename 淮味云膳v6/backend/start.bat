@echo off
chcp 65001 >nul

echo 淮味云膳餐馆服务系统启动脚本
echo ==============================
echo.

rem 检查Maven是否安装
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven，请确保Maven已安装并添加到环境变量PATH中
    pause
    exit /b 1
)

rem 检查Java是否安装
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo 错误: 未找到Java，请确保Java已安装并添加到环境变量PATH中
    pause
    exit /b 1
)

echo 正在启动淮味云膳餐馆服务系统...
echo 服务器将运行在 http://localhost:8082
echo.

rem 启动项目
mvn spring-boot:run

pause