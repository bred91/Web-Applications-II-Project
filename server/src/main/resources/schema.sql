create table products
(
    id VARCHAR(23) primary key,
    name varchar(255) NOT NULL,
    brand varchar(255)
);

CREATE TABLE users (
    email VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL
);