@echo off
@title [%date% %time%] Build
:compile
mkdir bin
echo [%date% %time%]: Compiling source code...
java -jar libs/javac++.jar src libs bin
echo [%date% %time%]: Done!
pause