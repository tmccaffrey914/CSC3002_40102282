@echo off

setlocal EnableDelayedExpansion

set FILE=%1

set FORSTUFF="tokens=* delims=,"

for /f %FORSTUFF% %%i in (%FILE%) do (
	set LINE=%%i
	set MYLINE=!LINE!
	call %~dp0\CallOCSQL.bat !MYLINE!
)