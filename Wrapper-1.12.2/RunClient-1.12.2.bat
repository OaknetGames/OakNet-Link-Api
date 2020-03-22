@echo off
echo Make Dirs
rmdir /s/q ..\bin\
mkdir \libs\
mkdir \run\
mkdir \run\mods
mkdir ..\bin\
mkdir ..\bin\oaknetlink-api\
mkdir ..\bin\oaknetlink-api\assets\
mkdir ..\bin\updater\
echo Find Source
dir /s /B ..\OakNet-Link-Api\*.java > ..\OakNet-Link-Api\sources.txt
dir /s /B ..\Updater\*.java > ..\Updater\sources.txt
echo Build source
javac -cp ..\OakNet-Link-Api\libs\log4j-core-2.8.jar;..\OakNet-Link-Api\libs\log4j-api-2.8.jar -d ..\bin\oaknetlink-api\ @..\OakNet-Link-Api\sources.txt -Xlint:unchecked
javac -d ..\bin\updater\ @..\Updater\sources.txt
echo Copy Assets
xcopy ..\OakNet-Link-Api\assets ..\bin\oaknetlink-api\assets /s /e
echo Build Jar
jar cvf .\libs\OakNetLink-Api.jar -C ..\bin\oaknetlink-api\ .
jar cvf .\libs\OakNetLink-Updater.jar -C ..\bin\updater\ .
echo Copy some libs
copy .\libs\OakNetLink-Api.jar .\run\mods\OakNetLink-Api.jar
copy .\libs\OakNetLink-Updater.jar .\run\mods\OakNetLink-Updater.jar
echo "Run Client"
.\gradlew.bat runClient
pause
