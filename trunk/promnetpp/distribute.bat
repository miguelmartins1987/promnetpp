@echo off
set DISTRIBUTION_DIRECTORY=C:\promnetpp-dist
set SEVEN_ZIP_HOME=C:\Program Files\7-Zip
set PATH=%PATH%;%SEVEN_ZIP_HOME%
REM Setup
if exist %DISTRIBUTION_DIRECTORY% rmdir /s /q %DISTRIBUTION_DIRECTORY%
mkdir %DISTRIBUTION_DIRECTORY%
if exist %temp%\promnetpp rmdir /s /q %temp%\promnetpp
mkdir %temp%\promnetpp
REM Copy JAR file first
copy dist\promnetpp.jar %temp%\promnetpp
REM Copy lib folder
if not exist %temp%\promnetpp\lib mkdir %temp%\promnetpp\lib
copy dist\lib %temp%\promnetpp\lib
REM Now copy the templates
if not exist %temp%\promnetpp\templates mkdir %temp%\promnetpp\templates
xcopy /s templates %temp%\promnetpp\templates
REM Lastly, copy the default-configuration.xml file and the PROMELA models
copy default-configuration.xml %temp%\promnetpp
copy *.pml %temp%\promnetpp
cd %temp%\promnetpp
REM Ask the user for version and compress it all
set /p PROMNETPP_VERSION_NAME=Version name: 
7z a -mx9 promnetpp-%PROMNETPP_VERSION_NAME%.zip * && copy promnetpp-%PROMNETPP_VERSION_NAME%.zip %DISTRIBUTION_DIRECTORY%
explorer %DISTRIBUTION_DIRECTORY%
echo Done!
pause
