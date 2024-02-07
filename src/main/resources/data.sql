INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@gmail.com', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE(role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT(name)
VALUES ('Restaurant1'),
       ('Restaurant2'),
       ('Restaurant3'),
       ('Restaurant4');

INSERT INTO MENU(menu_date, restaurant_id)
VALUES (CURRENT_DATE, 1),
       (CURRENT_DATE, 2),
       ('2024-01-08', 1);

INSERT INTO MENU_ITEM(menu_id, price, name)
VALUES (1, 99, 'Dish1'),
       (1, 999, 'Dish2'),
       (1, 1099, 'Dish3'),
       (2, 399, 'Dish4'),
       (2, 499, 'Dish5'),
       (3, 199, 'Dish6');

INSERT INTO VOTE(restaurant_id, user_id, vote_date, vote_time)
VALUES ( 4, 1, CURRENT_DATE, '10:00'),
       ( 3, 2, CURRENT_DATE, '10:00'),
       ( 2, 1, '2024-01-09', '10:00'),
       ( 4, 1, '2024-01-08', '10:00'),
       ( 1, 1, '2024-01-07', '10:00');

