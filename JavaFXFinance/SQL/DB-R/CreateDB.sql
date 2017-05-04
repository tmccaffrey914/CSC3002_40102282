create schema rbidaskdb;
use rbidaskdb;
set foreign_key_checks=0;
CREATE TABLE marketmatch (
    ask INT(11) NOT NULL,
    bid INT(11) NOT NULL,
    timest TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ticker VARCHAR(4) NOT NULL,
    FOREIGN KEY (ask)
        REFERENCES ask (id),
    FOREIGN KEY (bid)
        REFERENCES bid (id),
    FOREIGN KEY (ticker)
        REFERENCES company (ticker),
    PRIMARY KEY (ask , bid)
);
CREATE TABLE company (
    ticker VARCHAR(4) NOT NULL,
    PRIMARY KEY (ticker)
);
CREATE TABLE options (
    id INT(11) NOT NULL AUTO_INCREMENT,
    expirationdate TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ticker VARCHAR(4) NOT NULL,
    strikeprice DOUBLE NOT NULL,
    interestrate DOUBLE NOT NULL,
    volatility DOUBLE NOT NULL,
    FOREIGN KEY (ticker)
        REFERENCES company (ticker),
    PRIMARY KEY (id)
);
CREATE TABLE bid (
    id INT(11) NOT NULL AUTO_INCREMENT,
    size INT NOT NULL,
    price DOUBLE NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE ask (
    id INT(11) NOT NULL AUTO_INCREMENT,
    size INT NOT NULL,
    price DOUBLE NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE trader (
    username VARCHAR(32) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    pword VARCHAR(128) NOT NULL,
    salt INT(11) NOT NULL,
    tradingin VARCHAR(4) NOT NULL,
    FOREIGN KEY (tradingin)
        REFERENCES company (ticker),
    PRIMARY KEY (username)
);
