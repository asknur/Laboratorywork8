insert into authorities(authority)
values ('full'),
       ('read_only');

insert into roles(role_name, authority_id)
values ('admin', (select id from authorities where authority = 'full')),
       ('user', (select id from authorities where authority = 'read_only'));

INSERT INTO users (username, email, password, enabled, role_id)
VALUES ('Bakyt', 'bakyt@example.com', '$2a$10$QLjdf1RZCjBYwCG5DahE8OJft93BftkQZmd1WGL9MQZghceRZPQ4i', true,
        (SELECT id FROM roles WHERE role_name = 'user')),
       ('Aizada', 'aizada@example.com', '$2a$10$QLjdf1RZCjBYwCG5DahE8OJft93BftkQZmd1WGL9MQZghceRZPQ4i', true,
        (SELECT id FROM roles WHERE role_name = 'user')),
       ('Nursultan', 'nursultan@example.com', '$2a$10$QLjdf1RZCjBYwCG5DahE8OJft93BftkQZmd1WGL9MQZghceRZPQ4i', true,
        (SELECT id FROM roles WHERE role_name = 'user'));

INSERT INTO quizzes (title, description, creator_id)
VALUES ('Города Кыргызстана', 'Проверь знания о городах Кыргызстана',
        (select id from users where email = 'bakyt@example.com')),
       ('Общие знания', 'Викторина по базовым знаниям', (select id from users where email = 'aizada@example.com'));

INSERT INTO questions (question_text, quiz_id)
VALUES ('Какой город является столицей Кыргызстана?', 1),
       ('Город на юге Кыргызстана?', 1),
       ('Сколько континентов на Земле?', 2),
       ('Столица Франции?', 2);

INSERT INTO options (option_text, is_correct, question_id)
VALUES ('Бишкек', true, 1),
       ('Ош', false, 1),
       ('Нарын', false, 1),
       ('Каракол', false, 1),
       ('Ош', true, 2),
       ('Бишкек', false, 2),
       ('Баткен', false, 2),
       ('Чолпон-Ата', false, 2),
       ('Калифорния', true, 3),
       ('Вашингтон', false, 3),
       ('Чикаго', false, 3),
       ('Теннеси', false, 3),
       ('Париж', true, 4),
       ('Берлин', false, 4),
       ('Лион', false, 4),
       ('Марсель', false, 4);

INSERT INTO quiz_results (score, user_id, quiz_id)
VALUES (2.0, 1, 1),
       (1.0, 2, 2),
       (0.0, 3, 1);