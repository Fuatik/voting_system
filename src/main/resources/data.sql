INSERT INTO PUBLIC.USERS (ID, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES (1, 'user@gmail.com', 'User_First', 'User_Last', 'password'),
       (2, 'admin@gmail.com', 'Admin_First', 'Admin_Last', 'password');

INSERT INTO USER_ROLE(ROLE, USER_ID)
VALUES ('ROLE_USER', 1),
       ('ROLE_ADMIN', 2),
       ('ROLE_USER', 2);
