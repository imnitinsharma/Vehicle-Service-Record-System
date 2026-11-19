#!/bin/bash
echo "====================================================="
echo "  Vehicle Service Record System - Nitin Kumar Sharma"
echo "====================================================="
echo ""

mkdir -p out

find src -name "*.java" > sources.txt

echo "[1/2] Compiling..."
javac -cp "lib/mysql-connector-j-8.0.33.jar" -d out @sources.txt

if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed."
    rm sources.txt
    exit 1
fi

echo "[2/2] Running..."
java -cp "out:lib/mysql-connector-j-8.0.33.jar" com.vehicle.ui.MainFrame

rm sources.txt
