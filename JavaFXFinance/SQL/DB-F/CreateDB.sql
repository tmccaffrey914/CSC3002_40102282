create schema ffbidaskdb;
use ffbidaskdb;
set foreign_key_checks=0;
CREATE TABLE bidaskmatch (
    id INT(11) NOT NULL AUTO_INCREMENT,
    timest TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ticker VARCHAR(4) NOT NULL,
    size INT NOT NULL,
    price FLOAT NOT NULL,
    bidask VARCHAR(3),
    FOREIGN KEY (ticker)
        REFERENCES company (ticker),
    PRIMARY KEY (id)
);
CREATE TABLE company (
    ticker VARCHAR(4) NOT NULL,
    PRIMARY KEY (ticker)
);
CREATE TABLE options (
    id INT(11) NOT NULL AUTO_INCREMENT,
    expirationdate TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ticker VARCHAR(4) NOT NULL,
    strikeprice FLOAT(4) NOT NULL,
    interestrate FLOAT NOT NULL,
    volatility FLOAT NOT NULL,
    FOREIGN KEY (ticker)
        REFERENCES company (ticker),
    PRIMARY KEY (id)
);
