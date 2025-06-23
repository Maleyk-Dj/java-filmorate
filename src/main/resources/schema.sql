-- таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    login VARCHAR(255) NOT NULL,
    birthday DATE
    );

-- таблица друзей
CREATE TABLE IF NOT EXISTS friendships (
    user_id BIGINT,
    friend_id BIGINT,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE
    );

-- таблица рейтингов
CREATE TABLE IF NOT EXISTS rating (
    rating_id INT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR NOT NULL
    );

-- таблица жанров
CREATE TABLE IF NOT EXISTS genre (
    genre_id INT PRIMARY KEY,
    name VARCHAR(255)
    );

-- таблица фильмов
CREATE TABLE IF NOT EXISTS films (
    film_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    release_date DATE,
    duration INT,
    rating_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rating_id) REFERENCES rating(rating_id)
    );

-- таблица жанров фильма
CREATE TABLE IF NOT EXISTS film_genre (
    film_id INT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre(genre_id) ON DELETE CASCADE
    );

-- таблица лайков
CREATE TABLE IF NOT EXISTS film_likes (
    film_id INT,
    user_id BIGINT,
    liked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
    );
