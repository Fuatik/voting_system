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
VALUES (now(), 1, 99, 'Dish1'),
       (now(), 1, 999, 'Dish2'),
       (now(), 3, 499, 'Dish3'),
       (now(), 4, 199, 'Dish4'),
       ('2024-01-08', 1, 1099, 'Dish5'),
       ('2024-01-09', 2, 399, 'Dish6');

INSERT INTO VOTE(restaurant_id, user_id, vote_date, vote_time)
VALUES ( 1, 1, now(), now()),
       ( 3, 2, now(), now()),
       ( 2, 1, '2024-01-09', now()),
       ( 4, 1, '2024-01-08', now()),
       ( 1, 1, '2024-01-07', now());

