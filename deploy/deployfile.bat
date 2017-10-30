@echo off

setlocal

SET FOLDERNAME=%1

IF  "%FOLDERNAME%" == "" (
	echo
	echo Please provide deploy file name without extension as the first argument. Exiting.
	EXIT /B
)

cd ../hybris/temp
mkdir "%FOLDERNAME%"
cd "%FOLDERNAME%"

echo(
echo Creating metadata.properties...
echo package_version = 2.3 > metadata.properties
echo done
echo(

echo(
echo Creating folder structure...
mkdir hybris
cd hybris
mkdir bin
mkdir config
cd config
mkdir dev
mkdir prod
mkdir stag
echo done
echo(

cd ../../../../bin/platform/
CALL setantenv.bat
CALL ant production

cd ../../temp/"%FOLDERNAME%"/hybris/bin
echo f | xcopy "..\..\..\hybris\hybrisServer\hybrisServer-AllExtensions.zip" "hybrisServer-AllExtensions.zip"
echo f | xcopy "..\..\..\hybris\hybrisServer\hybrisServer-Platform.zip" "hybrisServer-Platform.zip"

cd ..\config\dev

copy ..\..\..\..\..\config\env\dev\customer.properties "customer.properties"
copy ..\..\..\..\..\config\localextensions.xml "localextensions.xml"
robocopy "..\..\..\..\..\config\customize" "customize" /e
robocopy " ..\..\..\..\..\..\..\config\languages" "languages" /e

cd ..\stag
copy ..\..\..\..\..\config\env\stag\customer.adm.properties "customer.adm.properties"
copy ..\..\..\..\..\config\env\stag\customer.app.properties "customer.app.properties"
copy ..\..\..\..\..\config\localextensions.xml "localextensions.adm.xml"
copy ..\..\..\..\..\config\localextensions.xml "localextensions.app.xml"
robocopy "..\..\..\..\..\config\customize" "customize" /e
robocopy " ..\..\..\..\..\..\..\config\languages" "languages" /e

cd ..\prod
copy ..\..\..\..\..\config\env\prod\customer.adm.properties "customer.adm.properties"
copy ..\..\..\..\..\config\env\prod\customer.app.properties "customer.app.properties"
copy ..\..\..\..\..\config\localextensions.xml "localextensions.adm.xml"
copy ..\..\..\..\..\config\localextensions.xml "localextensions.app.xml"
robocopy "..\..\..\..\..\config\customize" "customize" /e
robocopy " ..\..\..\..\..\..\..\config\languages" "languages" /e

echo(
echo creating the zip temp\%FOLDERNAME%.zip ...

cd ..\..\..\..\


..\..\deploy\arch\7z a %FOLDERNAME%.zip %FOLDERNAME%

echo(
echo hybris\temp\%FOLDERNAME%.zip DEPLOYFILE HAS BEEN CREATED

endlocal

	
