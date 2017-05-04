USE ffBidAskDB;
INSERT INTO company (ticker) 
    SELECT @TICKER
        FROM dual
        WHERE NOT EXISTS (SELECT * FROM company
                             WHERE ticker = @TICKER);

USE ffBidAskDB;
INSERT INTO bidaskmatch (ticker,size,price,bidask)
VALUES (@TICKER,@BIDSIZE,@BIDPRICE,'BID');

USE ffBidAskDB;
INSERT INTO bidaskmatch (ticker,size,price,bidask)
VALUES (@TICKER,@ASKSIZE,@ASKPRICE,'ASK');