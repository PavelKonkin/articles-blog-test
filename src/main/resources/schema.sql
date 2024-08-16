CREATE TABLE IF NOT EXISTS articles
(
    article_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title        VARCHAR(100)                            NOT NULL,
    content      VARCHAR                                 NOT NULL,
    author       VARCHAR(100),
    publish_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_category PRIMARY KEY (article_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username     VARCHAR(255)                            NOT NULL,
    password VARCHAR(255)                            NOT NULL,
    role     VARCHAR(10)                             NOT NULL,
    CONSTRAINT uq_email UNIQUE (username),
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);