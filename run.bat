@echo off
title Vehicle Service Record System
setlocal enabledelayedexpansion

set JARFILE=
for %%f in (lib\mysql-connector*.jar) do set JARFILE=%%f

if "%JARFILE%"=="" (
    echo ERROR: No mysql-connector jar found in lib folder!
    pause
    exit /b 1
)

echo Using JAR: %JARFILE%

if not exist "out" mkdir out

if exist sources.txt del sources.txt
for /r src %%f in (*.java) do (
    set "p=%%f"
    set "p=!p:\=/!"
    echo "!p!" >> sources.txt
)

echo Compiling...
javac -cp "%JARFILE%" -d out @sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo COMPILE FAILED
    del sources.txt
    pause
    exit /b 1
)

echo Compiled successfully. Launching app...
java -cp "out;%JARFILE%" com.vehicle.ui.MainFrame

del sources.txt
pause
