CREATE DATABASE IF NOT EXISTS `huaiwei-v2` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `huaiwei-v2`;
SET GLOBAL time_zone = '+8:00';

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    role TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS category (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    sort INT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS dish (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    category_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    description TEXT,
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    table_no VARCHAR(20) DEFAULT NULL COMMENT '桌号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS order_detail (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL,
    dish_id BIGINT UNSIGNED NOT NULL,
    dish_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_order_no (order_no),
    KEY idx_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS restaurant (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    address VARCHAR(255) NOT NULL,
    open_time VARCHAR(10) NOT NULL DEFAULT '09:00',
    close_time VARCHAR(10) NOT NULL DEFAULT '21:00',
    description TEXT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    system_name VARCHAR(100) NOT NULL DEFAULT 'Huaiwei Restaurant',
    version VARCHAR(20) NOT NULL DEFAULT '1.0.0',
    database_url VARCHAR(255) NOT NULL,
    port INT NOT NULL DEFAULT 8080,
    ai_api_key VARCHAR(255) COMMENT 'DeepSeek R1 API Key',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO restaurant (name, phone, address, open_time, close_time, description) VALUES
('Huaiwei Restaurant', '13800138000', 'Jiangsu Province, Huaian City, Qingjiangpu District, Jiankang Road 123', '09:00', '21:00', 'Huaiwei Restaurant is a characteristic restaurant focusing on Huaiyang cuisine, providing authentic local Huaian food.');

INSERT INTO system_config (system_name, version, database_url, port) VALUES
('Huaiwei Restaurant', '1.0.0', 'jdbc:mysql://localhost:3306/huaiwei-v2', 8080);

INSERT INTO sys_user (username, password, phone, role) VALUES
('admin', '123456', '13800138000', 1),
('user1', '123456', '13900139001', 0);