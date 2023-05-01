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

INSERT INTO employee (id, email, name, surname, role_id)
VALUES
    (1, 'abc@gmail.com', 'John', 'Smith', 1),
    (2, 'aaa@fafa.a', 'Jack', 'Black', 2),
    (3, 'dsa@cas.it', 'Bob', 'Marley', 2);