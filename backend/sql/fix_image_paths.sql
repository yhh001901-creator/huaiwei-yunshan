-- 批量更新数据库中的图片路径，将中文文件名改为时间戳格式
-- 注意：此脚本需要根据实际数据库中的路径来修改

-- 查看所有包含中文的图片路径
SELECT id, name, image_url FROM dish WHERE image_url LIKE '%红烧肉%' OR image_url LIKE '%宫保鸡丁%' OR image_url LIKE '%鱼香肉丝%';

-- 示例：将特定路径更新为新格式
-- UPDATE dish SET image_url = '/uploads/dishes/dish_1776612034170_红烧肉.png' WHERE id = xxx;
