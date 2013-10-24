# --- !Ups

CREATE TABLE archived_players (
    bisId varchar(50) NOT NULL,
    name varchar(255) NOT NULL,
    remark varchar(255),
    rank varchar(20) NOT NULL,
    joined DATE not null DEFAULT TODAY
);


# --- !Downs
 
DROP TABLE archived_players;
