CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    text       TEXT   NOT NULL,
    post_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id)
);