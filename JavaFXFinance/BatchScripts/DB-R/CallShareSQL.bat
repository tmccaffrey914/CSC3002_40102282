@echo off

setlocal EnableDelayedExpansion

set LINE=%*
set COUNT=0
set TICKER=""
set BIDSIZE=""
set BID=""
set ASK=""
set ASKSIZE=""

set /A DELAY=%RANDOM% * 3 / 32768 + 1

for %%j in (%LINE%) do (
	set /A COUNT += 1

	if !COUNT!==3 (
		set TICKER=%%j
	)
	
	if !COUNT!==4 (
		set BIDSIZE=%%j
	)

	if !COUNT!==5 (
		set BID=%%j
	)

	if !COUNT!==6 (
		set ASK=%%j
	)

	if !COUNT!==7 (
		set ASKSIZE=%%j
		timeout /t %DELAY% /nobreak
		call %~dp0\RunInsertShareQry.bat !BIDSIZE! !BID! '!TICKER!' !ASK! !ASKSIZE!
	)
)