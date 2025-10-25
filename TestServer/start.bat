@echo on
cd /d "%~dp0"
echo === FOLDER %cd% ===

REM Явно используем твою JDK, чтобы не зависеть от PATH
set JAVA_EXE="C:\Users\Professional\.jdks\openjdk-24.0.1\bin\java.exe"

echo Проверка Java версии:
%JAVA_EXE% -version

echo Запускаю server.jar ...
%JAVA_EXE% -jar server.jar

echo Код завершения: %ERRORLEVEL%
echo === SERVER EXITED ===
pause