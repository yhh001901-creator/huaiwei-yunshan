-- 为菜品表添加库存字段
USE `huaiwei-v2`;
ALTER TABLE `dish` ADD COLUMN `stock` INT DEFAULT 999 COMMENT '库存数量';

-- 更新现有菜品库存为999
UPDATE `dish` SET `stock` = 999 WHERE `stock` IS NULL;
