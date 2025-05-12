CREATE TABLE IF NOT EXISTS posts
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    text        TEXT         NOT NULL,
    image_path  VARCHAR(512),
    likes_count INT       DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);