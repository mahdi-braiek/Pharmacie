@echo off
chcp 65001 >nul
echo ===================================
echo    Pharmacie App - Interface GUI
echo ===================================
echo.

cd /d "%~dp0"

:: Compiler les fichiers Java
echo [*] Compilation en cours...
javac -encoding UTF-8 -cp "lib/*" -d bin src/*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERREUR] La compilation a echoue!
    pause
    exit /b 1
)

echo [OK] Compilation reussie!
echo.
echo [*] Lancement de l'interface graphique...
echo.

:: Lancer l'application GUI
java -cp "bin;lib/*" LoginGUI

pause
