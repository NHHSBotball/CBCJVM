@echo on
echo "[COMPILER] ------------------"
echo "[COMPILER] Building CBCWrapper"
cd cbc
Build.bat
cd ..

echo "[COMPILER] Building Installer"
cd installer
make_latest_installer.bat

echo "[COMPILER] Building Download Tool"
cd consoleDownload
ant
