@echo off

setlocal EnableDelayedExpansion

set TICKER=%1
set STRIKE=%2
set INTEREST=%3
set VOLATILITY=%4

cd C:\Program Files\MySQL\MySQL Server 5.7\bin

mysql -uroot -pLunch914 -hlocalhost -e "set @TICKER=!TICKER!;set @STRIKE=!STRIKE!;set @INTEREST=!INTEREST!;set @VOLATILITY=!VOLATILITY!;source C:/Users/apple/OneDrive/Documents/Project/SQL/BidAskFF/InsertOC.sql;"