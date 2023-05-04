INSERT INTO state (id, name)
VALUES
    (1, 'OPEN'),
    (2, 'IN PROGRESS'),
    (3, 'RESOLVED'),
    (4, 'CLOSED'),
    (5, 'REOPENED');

INSERT INTO role (id, name)
VALUES
    (1, 'ADMIN'),
    (2, 'EXPERT'),
    (3, 'MANAGER');

INSERT INTO priority (id, name)
VALUES
    (1, 'LOW'),
    (2, 'MEDIUM'),
    (3, 'HIGH');

INSERT INTO employee (id, email, name, surname, role_id)
VALUES
    (1, 'abc@gmail.com', 'John', 'Smith', 1),
    (2, 'aaa@fafa.a', 'Jack', 'Black', 2),
    (3, 'dsa@cas.it', 'Bob', 'Marley', 2);

insert into profiles (email, name, phone_number, surname, username )
values
    ('baba@gmail.com', 'John', '123456789', 'Smith', 'johnsmith');

insert into products (id, brand, name)
values
    (1, 'Apple', 'iPhone 12'),
    (2, 'Samsung', 'Galaxy S21'),
    (3, 'Xiaomi', 'Mi 11');

insert into purchase (id, expiring_date, purchase_date, warranty_code, customer_email, product_id)
values
    (1, '2021-12-31', '2021-01-01', '123456789', 'baba@gmail.com', 1)