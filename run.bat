@echo off
REM Script de compilation et d'exécution - PharmacieApp
REM =====================================================

setlocal enabledelayedexpansion

echo.
echo ╔═════════════════════════════════════════════════╗
echo ║      PHARMACIE APP - Script Compilation         ║
echo ╚═════════════════════════════════════════════════╝
echo.

REM Créer le dossier bin s'il n'existe pas
if not exist bin mkdir bin

REM Compiler tous les fichiers Java
echo ➜ Compilation en cours...
javac -cp lib/mysql-connector-j-8.0.33.jar -d bin src/*.java

if %errorlevel% neq 0 (
    echo.
    echo ✗ ERREUR lors de la compilation!
    echo   Vérifiez que :
    echo   - Tous les fichiers .java sont dans src/
    echo   - mysql-connector-j.jar est dans lib/
    pause
    exit /b 1
)

echo ✓ Compilation réussie!
echo.
echo ╔═════════════════════════════════════════════════╗
echo ║          LANCEMENT DE L'APPLICATION             ║
echo ╚═════════════════════════════════════════════════╝
echo.

REM Lancer l'application
java -cp bin;lib/mysql-connector-j-8.0.33.jar LoginGUI

pause
