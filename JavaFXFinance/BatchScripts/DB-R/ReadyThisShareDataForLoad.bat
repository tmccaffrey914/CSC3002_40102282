@echo off
setlocal EnableDelayedExpansion

set FILE=%1
set DIR=%~dp0

findstr /V "INFO" %FILE% > %DIR%\cleanData.txt

set FORSTUFF="tokens=* delims=*"

for /f %FORSTUFF% %%i in (%DIR%\cleanData.txt) do (
	set LINE=%%i
	set MYLINE=!LINE!
	set MYLINE=!MYLINE:[=%!
	set MYLINE=!MYLINE:]=%!
	set MYLINE=!MYLINE: =,!
	set MYLINE=!MYLINE:x=,!
	echo !MYLINE! >> %DIR%\temp.txt
)

type %DIR%\temp.txt > %DIR%\DataReadyForLoad.txt

del "%DIR%\temp.txt"
del "%DIR%\cleanData.txt"

call %DIR%\LoadThisShareDataToThisRDB.bat %DIR%\DataReadyForLoad.txt