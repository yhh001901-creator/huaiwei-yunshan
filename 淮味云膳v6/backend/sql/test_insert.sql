-- 使用数据库
USE `huaiwei-v2`;

-- 插入热销菜品
INSERT INTO dish (category_id, name, price, image_url, description, status) VALUES
(1, '红烧肉', 68.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=braised%20pork%20belly%20chinese%20cuisine&size=512x512', '传统红烧肉，肥而不腻', 1);
