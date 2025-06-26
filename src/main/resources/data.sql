-- Начальные жанры
MERGE INTO genre (id, name) KEY (id) VALUES (1, 'Комедия');
MERGE INTO genre (id, name) KEY (id) VALUES (2, 'Драма');
MERGE INTO genre (id, name) KEY (id) VALUES (3, 'Мультфильм');
MERGE INTO genre (id, name) KEY (id) VALUES (4, 'Триллер');
MERGE INTO genre (id, name) KEY (id) VALUES (5, 'Документальный');
MERGE INTO genre (id, name) KEY (id) VALUES (6, 'Боевик');

-- Начальные рейтинги (MPA)
MERGE INTO rating (id, name) KEY (id) VALUES (1, 'G');
MERGE INTO rating (id, name) KEY (id) VALUES (2, 'PG');
MERGE INTO rating (id, name) KEY (id) VALUES (3, 'PG-13');
MERGE INTO rating (id, name) KEY (id) VALUES (4, 'R');
MERGE INTO rating (id, name) KEY (id) VALUES (5, 'NC-17');
