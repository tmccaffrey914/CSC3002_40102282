USE ffBidAskDB;
INSERT INTO options (ticker,strikeprice,interestrate,volatility)
VALUES (@TICKER,@STRIKE,@INTEREST,@VOLATILITY);