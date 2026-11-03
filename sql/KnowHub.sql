-- =========================================================
-- KnowHub schema for MySQL 8.0+
-- Charset: utf8mb4
-- =========================================================

DROP DATABASE IF EXISTS knowhub;
CREATE DATABASE knowhub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE knowhub;

-- ---------------- users ----------------
CREATE TABLE users (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(64)  NOT NULL UNIQUE,
    password  VARCHAR(128) NOT NULL,
    nickname  VARCHAR(64)  NOT NULL,
    role      VARCHAR(32)  NOT NULL DEFAULT 'user',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------- categories ----------------
CREATE TABLE categories (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(128) NOT NULL UNIQUE,
    description VARCHAR(512) DEFAULT '',
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------- documents ----------------
CREATE TABLE documents (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(256) NOT NULL,
    file_name     VARCHAR(256) NOT NULL,
    file_path     VARCHAR(512) NOT NULL,
    content_type  VARCHAR(32)  NOT NULL,
    file_size     BIGINT       NOT NULL,
    category_id   INT          DEFAULT NULL,
    content       MEDIUMTEXT,
    upload_time   DATETIME     NOT NULL,
    INDEX idx_category (category_id),
    INDEX idx_upload_time (upload_time),
    CONSTRAINT fk_doc_cat FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------- qa_records ----------------
CREATE TABLE qa_records (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    user_id        INT          DEFAULT NULL,
    question       TEXT         NOT NULL,
    answer         MEDIUMTEXT,
    doc_references VARCHAR(1024) DEFAULT '',
    create_time    DATETIME     NOT NULL,
    INDEX idx_user (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------- seed ----------------
INSERT INTO users (username, password, nickname, role) VALUES
    ('admin', '123456', 'Admin', 'admin'),
    ('user',  '123456', 'User',  'user');

INSERT INTO categories (name, description) VALUES
    ('Tech',    'Technical docs'),
    ('Product', 'Product manuals'),
    ('Policy',  'Internal policies');
