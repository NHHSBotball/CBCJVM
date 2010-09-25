#!/bin/bash
echo "[COMPILER] ------------------"
echo "[COMPILER] Building CBCJVM Classes"
cd cbc
#mvn clean install
sh Build.sh
cd ..

echo "[COMPILER] Building Installer"
cd installer
sh make_latest_installer

cd ..

#echo "[COMPILER] Building Download Tool"
#cd consoleDownload
#ant
