@echo off

setlocal EnableDelayedExpansion

set BIDSIZE=%1
set BIDPRICE=%2
set TICKER=%3
set ASKPRICE=%4
set ASKSIZE=%5

cd C:\Program Files\MySQL\MySQL Server 5.7\bin

mysql -uroot -pLunch914 -hlocalhost -e "set @BIDSIZE=!BIDSIZE!;set @BIDPRICE=!BIDPRICE!;set @TICKER=!TICKER!;set @ASKPRICE=!ASKPRICE!;set @ASKSIZE=!ASKSIZE!;source C:/Users/apple/OneDrive/Documents/Project/SQL/BidAskR/InsertBidAsk.sql;"