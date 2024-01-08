INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@gmail.com', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}password'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE(role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);
