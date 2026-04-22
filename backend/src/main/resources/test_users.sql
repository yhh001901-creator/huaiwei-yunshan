-- 权限系统测试用户数据
-- 角色说明: 0-顾客, 1-管理员, 2-收银员, 3-后厨

-- 清空现有用户数据（可选，谨慎使用）
-- TRUNCATE TABLE sys_user;

-- 插入测试用户
INSERT INTO sys_user (username, password, phone, role, create_time, update_time) VALUES
('admin', '123456', '13800000001', 1, NOW(), NOW()),
('cashier', '123456', '13800000002', 2, NOW(), NOW()),
('kitchen', '123456', '13800000003', 3, NOW(), NOW()),
('customer', '123456', '13800000004', 0, NOW(), NOW());

-- 查看用户数据
SELECT * FROM sys_user;
