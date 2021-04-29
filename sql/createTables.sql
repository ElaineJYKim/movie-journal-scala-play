CREATE TABLE journal (
            id SERIAL PRIMARY KEY,
            title VARCHAR(30) UNIQUE NOT NULL,
            year VARCHAR(5) NOT NULL,
            director VARCHAR(30) UNIQUE NOT NULL,
);

CREATE TABLE entries (
            id SERIAL PRIMARY KEY,
            entry VARCHAR(100) NOT NULL,
            sentiment INT NOT NULL,
            journal_id int4 REFERENCES journal(id),
);