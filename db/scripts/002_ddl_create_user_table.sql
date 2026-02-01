CREATE TABLE users
(
    id       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     varchar        not null,
    login    varchar unique not null,
    password varchar        not null
);