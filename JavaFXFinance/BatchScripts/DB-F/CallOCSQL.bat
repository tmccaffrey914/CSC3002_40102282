@echo off

setlocal EnableDelayedExpansion

set LINE=%*
set COUNT=0
set TICKER=""
set STRIKE=""
set INTEREST=""
set VOLATILITY=""

set /A DELAY=%RANDOM% * 15 / 32768 + 1

for %%j in (%LINE%) do (
	set /A COUNT += 1

	if !COUNT!==2 (
		set TICKER=%%j
	)
	
	if !COUNT!==3 (
		set STRIKE=%%j
	)

	if !COUNT!==4 (
		set INTEREST=%%j
	)

	if !COUNT!==5 (
		set VOLATILITY=%%j
	)

	if !COUNT!==6 (
		timeout /t %DELAY% /nobreak
		call %~dp0\RunInsertOCQry.bat '!TICKER!' !STRIKE! !INTEREST! !VOLATILITY!
	)
)