drop table if exists messages;
create table if not exists messages (
    id VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    text VARCHAR NOT NULL
);