USE rBidAskDB;
INSERT INTO bid (size,price)
VALUES (@BIDSIZE,@BIDPRICE);

USE rBidAskDB;
INSERT INTO ask (size,price)
VALUES (@ASKSIZE,@ASKPRICE);

USE rBidAskDB;
SELECT LAST_INSERT_ID()
INTO @BIDID
FROM
 bid
LIMIT 1;

USE rBidAskDB;
SELECT LAST_INSERT_ID()
INTO @ASKID
FROM
 ask
LIMIT 1;

USE rBidAskDB;
INSERT INTO company (ticker) 
    SELECT @TICKER
        FROM dual
        WHERE NOT EXISTS (SELECT * FROM company
                             WHERE ticker = @TICKER);

USE rBidAskDB;
INSERT INTO marketmatch (ask,bid,ticker)
VALUES (@ASKID,@BIDID,@TICKER);