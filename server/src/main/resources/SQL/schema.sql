--create database project_db;

create table products
(
    id VARCHAR(23) primary key,
    name varchar(255) NOT NULL,
    brand varchar(255)
);

CREATE TABLE profiles (
    email VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL
);