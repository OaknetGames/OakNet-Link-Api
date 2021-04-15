@echo off
echo Clean Old Stuff
rmdir /s/q .\bin\
del .\Wrapper-1.12.2\build.gradle
del .\Wrapper-1.16.5\build.gradle

echo Make Dirs
mkdir .\bin\
mkdir .\bin\oaknetlink-api\
mkdir .\bin\oaknetlink-api\assets\
mkdir .\bin\updater\
set /p username="Enter Minecraft-Username (E-Mail): "
set /p pass="Enter Minecraft-Password: "

echo Copy Configs
copy .\Wrapper-1.12.2\build.gradle.example .\Wrapper-1.12.2\build.gradle
copy .\Wrapper-1.16.5\build.gradle.example .\Wrapper-1.16.5\build.gradle

echo Apply Minecraft Credentials
set command1="(gc .\Wrapper-1.12.2\build.gradle) -replace '{username}', '%username%' | Out-File -encoding ASCII .\Wrapper-1.12.2\build.gradle"
set command2="(gc .\Wrapper-1.12.2\build.gradle) -replace '{password}', '%pass%' | Out-File -encoding ASCII .\Wrapper-1.12.2\build.gradle"
powershell -Command %command1%
powershell -Command %command2%
set command3="(gc .\Wrapper-1.16.5\build.gradle) -replace '{username}', '%username%' | Out-File -encoding ASCII .\Wrapper-1.16.5\build.gradle"
set command4="(gc .\Wrapper-1.16.5\build.gradle) -replace '{password}', '%pass%' | Out-File -encoding ASCII .\Wrapper-1.16.5\build.gradle"
powershell -Command %command3%
powershell -Command %command4%

echo Building Workspace Wrapper-1.12.2
mkdir .\Wrapper-1.12.2\libs\
mkdir .\Wrapper-1.12.2\run\
mkdir .\Wrapper-1.12.2\run\mods
echo Find Source
dir /s /B .\OakNet-Link-Api\*.java > .\OakNet-Link-Api\sources.txt
dir /s /B .\Updater\*.java > .\Updater\sources.txt
echo Build source
javac -cp .\OakNet-Link-Api\libs\log4j-core-2.8.jar;.\OakNet-Link-Api\libs\log4j-api-2.8.jar -d .\bin\oaknetlink-api\ @.\OakNet-Link-Api\sources.txt -Xlint:unchecked
javac -d .\bin\updater\ @.\Updater\sources.txt
echo Copy Assets
xcopy .\OakNet-Link-Api\assets .\bin\oaknetlink-api\assets /s /e
echo Build Jar
jar cvf .\Wrapper-1.12.2\libs\OakNetLink-Api.jar -C .\bin\oaknetlink-api\ .
jar cvf .\Wrapper-1.12.2\libs\OakNetLink-Updater.jar -C .\bin\updater\ .
echo Copy some libs
copy .\Wrapper-1.12.2\libs\OakNetLink-Api.jar .\Wrapper-1.12.2\run\mods\OakNetLink-Api.jar
copy .\Wrapper-1.12.2\libs\OakNetLink-Updater.jar .\Wrapper-1.12.2\run\mods\OakNetLink-Updater.jar
echo "Running ForgeGradle"
cd .\Wrapper-1.12.2\
.\gradlew.bat setupDecompWorkspace
.\gradlew.bat eclipse

echo Building Workspace Wrapper-1.16.5
cd ..
mkdir .\Wrapper-1.16.5\libs\
mkdir .\Wrapper-1.16.5\run\
mkdir .\Wrapper-1.16.5\run\mods
echo Find Source
dir /s /B .\OakNet-Link-Api\*.java > .\OakNet-Link-Api\sources.txt
dir /s /B .\Updater\*.java > .\Updater\sources.txt
echo Build source
javac -cp .\OakNet-Link-Api\libs\log4j-core-2.8.jar;.\OakNet-Link-Api\libs\log4j-api-2.8.jar -d .\bin\oaknetlink-api\ @.\OakNet-Link-Api\sources.txt -Xlint:unchecked
javac -d .\bin\updater\ @.\Updater\sources.txt
echo Copy Assets
xcopy .\OakNet-Link-Api\assets .\bin\oaknetlink-api\assets /s /e
echo Build Jar
jar cvf .\Wrapper-1.16.5\libs\OakNetLink-Api.jar -C .\bin\oaknetlink-api\ .
jar cvf .\Wrapper-1.16.5\libs\OakNetLink-Updater.jar -C .\bin\updater\ .
echo Copy some libs
copy .\Wrapper-1.16.5\libs\OakNetLink-Api.jar .\Wrapper-1.16.5\run\mods\OakNetLink-Api.jar
copy .\Wrapper-1.16.5\libs\OakNetLink-Updater.jar .\Wrapper-1.16.5\run\mods\OakNetLink-Updater.jar
echo "Running ForgeGradle"
cd .\Wrapper-1.16.5\
.\gradlew.bat setupDecompWorkspace
.\gradlew.bat eclipse

pause