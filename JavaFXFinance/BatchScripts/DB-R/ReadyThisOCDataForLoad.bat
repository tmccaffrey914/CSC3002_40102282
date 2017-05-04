@echo off
setlocal EnableDelayedExpansion

set FILE=%1
set DIR=%~dp0

findstr /V "INFO" %FILE% > %DIR%\OCcleanData.txt

set FORSTUFF="tokens=* delims=*"

for /f %FORSTUFF% %%i in (%DIR%\OCcleanData.txt) do (
	set LINE=%%i
	set MYLINE=!LINE!
	set MYLINE=!MYLINE:[=%!
	set MYLINE=!MYLINE:]=%!
	set MYLINE=!MYLINE: =,!
	set MYLINE=!MYLINE:x=,!
	echo !MYLINE! >> %DIR%\OCtemp.txt
)

type %DIR%\OCtemp.txt > %DIR%\OCDataReadyForLoad.txt

del "%DIR%\OCtemp.txt"
del "%DIR%\OCcleanData.txt"

call %DIR%\LoadThisOCDataToThisRDB.bat %DIR%\OCDataReadyForLoad.txt