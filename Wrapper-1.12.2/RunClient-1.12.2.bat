@echo off
echo Make Dirs
rmdir /s/q ..\bin\
mkdir \libs\
mkdir \run\
mkdir \run\mods
mkdir ..\bin\
mkdir ..\bin\oaknetlink-api\
mkdir ..\bin\updater\
echo Find Source
dir /s /B ..\OakNet-Link-Api\*.java > ..\OakNet-Link-Api\sources.txt
dir /s /B ..\Updater\*.java > ..\Updater\sources.txt
echo Build source
javac -cp ..\OakNet-Link-Api\libs\slick-util.jar;..\OakNet-Link-Api\libs\log4j-core-2.8.jar;..\OakNet-Link-Api\libs\log4j-api-2.8.jar -d ..\bin\oaknetlink-api\ @..\OakNet-Link-Api\sources.txt
javac -d ..\bin\updater\ @..\Updater\sources.txt
echo Build Jar
jar cvf .\libs\OakNetLink-Api.jar -C ..\bin\oaknetlink-api\ .
jar cvf .\libs\OakNetLink-Updater.jar -C ..\bin\updater\ .
echo Copy some libs
copy .\libs\OakNetLink-Api.jar .\run\mods\OakNetLink-Api.jar
copy .\libs\OakNetLink-Updater.jar .\run\mods\OakNetLink-Updater.jar
copy ..\OakNet-Link-Api\libs\slick-util.jar .\run\mods\slick-util.jar
copy ..\OakNet-Link-Api\libs\slick-util.jar .\libs\slick-util.jar
echo "Run Client"
.\gradlew.bat runClient
pause
