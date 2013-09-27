# --- !Ups

ALTER TABLE players
ADD COLUMN joined DATE not null DEFAULT TODAY;

# --- !Downs

ALTER TABLE players
DROP COLUMN joined;
