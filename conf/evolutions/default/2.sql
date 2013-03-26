# --- !Ups

ALTER TABLE players
DROP COLUMN bisId;

ALTER TABLE players
ADD COLUMN bisId varchar(50) not null DEFAULT '1';

# --- !Downs

ALTER TABLE players
DROP COLUMN bisId;

ALTER TABLE players
ADD COLUMN bisId integer not null DEFAULT 1;