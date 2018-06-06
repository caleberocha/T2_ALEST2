@echo off
pushd "%~dp0"
set javac=
for %%a in (javac.exe) do set javac=%%~$PATH:a
if "%javac%"=="" goto _FindJDK
set jar=jar
set java=java

:_Build
"%javac%" -d bin src\pucrs\calebe\alest2\t2\*.java
if not exist build mkdir build
"%jar%" cfe build\custo.jar pucrs.calebe.alest2.t2.App -C bin pucrs

:_Run
for /r %%a in (data\caso*.txt) do (
	java -jar build\custo.jar "%%~a"
	echo:
)
goto _End

:_FindJDK
set jdkHome=
for /f "tokens=3 skip=2" %%a in ('"reg query "HKLM\SOFTWARE\JavaSoft\Java Development Kit" /v CurrentVersion"') do for /f "tokens=2* skip=2" %%b in ('"reg query "HKLM\SOFTWARE\JavaSoft\Java Development Kit\%%a" /v JavaHome"') do set jdkHome=%%c
if "%jdkHome%"=="" (
	echo JDK n∆o encontrado.
	goto _End
)
set javac=%jdkHome%\bin\javac.exe
if not exist "%javac%" (
	echo javac n∆o encontrado.
	goto _End
)
set jar=%jdkHome%\bin\jar.exe
set java=%jdkHome%\bin\java.exe
goto _Build

:_End
popd