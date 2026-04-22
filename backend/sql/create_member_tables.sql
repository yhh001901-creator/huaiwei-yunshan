-- 会员表
USE `huaiwei-v2`;
CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '关联sys_user.id',
    phone VARCHAR(11) NOT NULL COMMENT '手机号',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    birthday DATE COMMENT '生日',
    gender TINYINT DEFAULT 0 COMMENT '0未知/1男/2女',
    point INT DEFAULT 0 COMMENT '当前积分',
    is_member TINYINT DEFAULT 0 COMMENT '0否/1是',
    member_expire DATE COMMENT '会员到期日(NULL=永久)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_id (user_id),
    UNIQUE KEY uk_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 积分流水表
CREATE TABLE IF NOT EXISTS point_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL COMMENT 'FK→member.id',
    point INT NOT NULL COMMENT '正数=获得,负数=消耗',
    type TINYINT NOT NULL COMMENT '1消费得/2签到/3兑换扣/4管理员调整/5生日',
    source VARCHAR(100) COMMENT '来源描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_member_id (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分流水表';

-- 优惠券模板表
CREATE TABLE IF NOT EXISTS coupon_template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '券名',
    type TINYINT NOT NULL COMMENT '1满减/2折扣/3兑换',
    discount_value DECIMAL(10,2) COMMENT '满减金额/折扣比例(0.8)/兑换菜价',
    min_amount DECIMAL(10,2) DEFAULT 0 COMMENT '最低消费金额',
    dish_id BIGINT COMMENT '兑换菜品ID(兑换券用)',
    total_count INT DEFAULT -1 COMMENT '发行总量(-1=无限)',
    remain_count INT DEFAULT -1 COMMENT '剩余数量',
    valid_days INT DEFAULT 30 COMMENT '领取后有效天数',
    expire_date DATE COMMENT '固定到期日(NULL=按valid_days算)',
    status TINYINT DEFAULT 1 COMMENT '0停用/1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券模板表';

-- 用户持有的优惠券表
CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL COMMENT 'FK→member.id',
    template_id BIGINT NOT NULL COMMENT 'FK→coupon_template.id',
    status TINYINT DEFAULT 0 COMMENT '0未使用/1已使用/2已过期',
    use_time DATETIME COMMENT '使用时间',
    order_no VARCHAR(32) COMMENT '使用的订单号',
    expire_date DATE NOT NULL COMMENT '过期日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    KEY idx_member_id (member_id),
    KEY idx_template_id (template_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户持有的优惠券';

-- 菜品会员价表
CREATE TABLE IF NOT EXISTS dish_member_price (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dish_id BIGINT NOT NULL COMMENT 'FK→dish.id',
    member_price DECIMAL(10,2) NOT NULL COMMENT '会员价',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品会员价表';

-- 插入测试优惠券模板数据
INSERT INTO coupon_template (name, type, discount_value, min_amount, valid_days, status, remain_count) VALUES
('满50减10', 1, 10.00, 50.00, 30, 1, 999),
('满100减20', 1, 20.00, 100.00, 30, 1, 999),
('8折券', 2, 0.80, 0.00, 30, 1, 500),
('新会员注册券', 1, 15.00, 80.00, 7, 1, 9999);
