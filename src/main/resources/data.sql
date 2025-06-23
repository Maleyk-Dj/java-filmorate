-- Начальные жанры
MERGE INTO genre (genre_id, name) KEY (genre_id) VALUES (1, 'Комедия');
MERGE INTO genre (genre_id, name) KEY (genre_id) VALUES (2, 'Драма');
MERGE INTO genre (genre_id, name) KEY (genre_id) VALUES (3, 'Мультфильм');
MERGE INTO genre (genre_id, name) KEY (genre_id) VALUES (4, 'Триллер');
MERGE INTO genre (genre_id, name) KEY (genre_id) VALUES (5, 'Документальный');
MERGE INTO genre (genre_id, name) KEY (genre_id) VALUES (6, 'Боевик');

-- Начальные рейтинги (MPA)
MERGE INTO rating (rating_id, name, description) KEY (rating_id) VALUES (1, 'G', 'Для всех возрастов');
MERGE INTO rating (rating_id, name, description) KEY (rating_id) VALUES (2, 'PG', 'Родительский контроль желателен');
MERGE INTO rating (rating_id, name, description) KEY (rating_id) VALUES (3, 'PG-13', 'Не рекомендуется детям до 13 лет');
MERGE INTO rating (rating_id, name, description) KEY (rating_id) VALUES (4, 'R', 'До 17 лет обязательно сопровождение родителей');
MERGE INTO rating (rating_id, name, description) KEY (rating_id) VALUES (5, 'NC-17', 'Только для взрослых (18+)');
