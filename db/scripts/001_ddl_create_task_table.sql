CREATE TABLE tasks
(
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description TEXT,
    created     TIMESTAMP,
    done        BOOLEAN
);