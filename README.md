# Filmorate — сервис для оценки фильмов

Filmorate — это REST-сервис на Java + Spring Boot, с возможностью добавления фильмов, пользователей, лайков, друзей и 
фильтрации самых популярных фильмов. Сервис использует H2-базу данных и поддерживает сохранение состояния.

---

## Схема базы данных

[Схема БД](docs/database-schema.png)

### Основные таблицы:

- `user` — хранит информацию о пользователях
- `film` — хранит данные о фильмах
- `genre` — содержит жанры
- `rating` — возрастной рейтинг (MPA)
- `film_genre` — связь "многие ко многим" между фильмами и жанрами
- `likes` — хранит лайки пользователей к фильмам
- `friendships` — связи между пользователями с возможным статусом (неподтверждённая/подтверждённая)

##  Примеры SQL-запросов

### Получение всех фильмов:
SELECT * FROM film;
 
### Получение всех пользователей:
SELECT * FROM user;

### Добавление нового друга (неподтверждённая заявка):
INSERT INTO friendships (user_id, friend_id, status)
VALUES (1, 2, 'PENDING');

### Подтверждение дружбы:
UPDATE friendships
SET status = 'CONFIRMED'
WHERE user_id = 2 AND friend_id = 1;

### Топ-5 самых популярных фильмов:
SELECT film.*, COUNT(likes.user_id) AS like_count
FROM film
LEFT JOIN likes ON film.film_id = likes.film_id
GROUP BY film.film_id
ORDER BY like_count DESC
LIMIT 5;

### Общие друзья двух пользователей:
SELECT u.*
FROM user u
JOIN friendships f1 ON u.user_id = f1.friend_id AND f1.user_id = 1 AND f1.status = 'CONFIRMED'
JOIN friendships f2 ON u.user_id = f2.friend_id AND f2.user_id = 2 AND f2.status = 'CONFIRMED';
