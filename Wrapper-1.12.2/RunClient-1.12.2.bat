@echo off
echo Make Dirs
rmdir /s/q ..\bin\
mkdir ..\bin\
mkdir ..\bin\oaknetlink-api\
mkdir ..\bin\updater\
echo Find Source
dir /s /B ..\OakNet-Link-Api\*.java > ..\OakNet-Link-Api\sources.txt
dir /s /B ..\Updater\*.java > ..\Updater\sources.txt
echo Build source
javac -d ..\bin\oaknetlink-api\ @..\OakNet-Link-Api\sources.txt
javac -d ..\bin\updater\ @..\Updater\sources.txt
echo Build Jar
jar cvf .\run\mods\OakNetLink-Api.jar -C ..\bin\oaknetlink-api\ .
jar cvf .\run\mods\OakNetLink-Updater.jar -C ..\bin\updater\ .
copy .\run\mods\OakNetLink-Api.jar .\libs\OakNetLink-Api.jar
copy .\run\mods\OakNetLink-Updater.jar .\libs\OakNetLink-Updater.jar
echo "Run Client"
.\gradlew.bat runClient
pause
