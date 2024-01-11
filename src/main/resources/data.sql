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

INSERT INTO DISH(dish_date, restaurant_id, price, name)
VALUES (CURRENT_DATE, 1, 99, 'Dish1'),
       (CURRENT_DATE, 1, 999, 'Dish2'),
       (CURRENT_DATE, 3, 499, 'Dish3'),
       (CURRENT_DATE, 4, 199, 'Dish4'),
       ('2024-01-08', 1, 1099, 'Dish5'),
       ('2024-01-09', 2, 399, 'Dish6');

INSERT INTO VOTE(restaurant_id, user_id, vote_date, vote_time)
VALUES ( 1, 1, CURRENT_DATE, CURRENT_TIME()),
       ( 3, 2, CURRENT_DATE, CURRENT_TIME()),
       ( 2, 1, '2024-01-09', CURRENT_TIME()),
       ( 4, 1, '2024-01-08', CURRENT_TIME()),
       ( 1, 1, '2024-01-07', CURRENT_TIME());

