# --- !Ups

CREATE TABLE players (
    bisId integer NOT NULL,
    name varchar(255) NOT NULL,
    remark varchar(255),
    rank varchar(20) NOT NULL
);


# --- !Downs
 
DROP TABLE players;
