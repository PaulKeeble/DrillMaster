# --- !Ups

CREATE TABLE player_trainings (
    player varchar(255) NOT NULL,
    training varchar(255) NOT NULL,
    date Date NOT NULL
);


# --- !Downs
 
DROP TABLE player_trainings;



