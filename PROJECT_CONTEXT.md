# 淮味云膳 - AI Agent 极速理解项目上下文

> **定位**: 餐厅点餐管理系统 | 多角色(Web/小程序) | **Token优化版**

---

## 项目结构

```
淮味云膳v6/
├── backend/          # Spring Boot 2.7 + MyBatis-Plus (port 8083)
├── frontend/         # Vue 3 + Vite 5 + Pinia + Element Plus + TailwindCSS + Capacitor (port 5173)
├── miniprogram/      # uni-app + Vue 3 + Pinia (微信小程序)
└── backend/sql/      # 数据库脚本(create + insert + alter)
```

**生产地址**: http://47.116.30.247:8083 | **数据库**: huaiwei-v2 | **账号**: root/123456

---

## 数据模型 (MySQL: huaiwei-v2, 7表)

### sys_user

| 字段                 | 类型           | 约束                    |
| ------------------ | ------------ | --------------------- |
| id                 | BIGINT       | PK, 自增                |
| username           | VARCHAR(50)  | NOT NULL              |
| password           | VARCHAR(100) | NOT NULL (明文)         |
| phone              | VARCHAR(11)  | UNIQUE                |
| role               | TINYINT      | **0顾客/1管理员/2收银员/3后厨** |
| create/update_time | DATETIME     |                       |

### category

| id | name(VARCHAR50) | sort(INT) | create/update_time |

### dish

| 字段          | 说明                                                    |
| ----------- | ----------------------------------------------------- |
| id          | BIGINT PK                                             |
| category_id | FK→category.id                                        |
| name        | VARCHAR(100)                                          |
| price       | DECIMAL(10,2)                                         |
| image_url   | VARCHAR(255) (例: /uploads/dish_1774258010373_xxx.png) |
| description | TEXT                                                  |
| status      | **0停售/1起售**                                           |
| stock       | INT (库存)                                              |

### orders

| 字段                 | 说明                                                |
| ------------------ | ------------------------------------------------- |
| id                 | BIGINT PK                                         |
| order_no           | VARCHAR(32) UNIQUE (格式: yyyyMMddHHmmss+UUID前8位大写) |
| user_id            | FK→sys_user.id                                    |
| total_amount       | DECIMAL(10,2)                                     |
| status             | **1待接单/2制作中/3已完成/4已取消**                           |
| table_no           | VARCHAR(20)                                       |
| create/update_time |                                                   |

### order_detail

| id | order_no(FK→orders) | dish_id | dish_name(快照) | unit_price | quantity | create/update_time |

### restaurant

| id | name | phone | address | open_time | close_time | description |

### system_config

| id | system_name | version | database_url | port | ai_api_key(DeepSeek) |

---

## 完整API清单 (base: /api)

**响应格式**: `{ code: 200, msg: "成功", data: {} }` | 失败500 | 未登录401
**Header**: `Authorization: Bearer <token>` (JWT, 24h有效期)

### Auth (免认证)

| 方法   | 路径             | 请求体                         | 返回                                               |
| ---- | -------------- | --------------------------- | ------------------------------------------------ |
| POST | /auth/login    | {username, password}        | {userId, username, phone, role, roleName, token} |
| POST | /auth/register | {username, password, phone} | 同上(默认role=0)                                     |
| GET  | /auth/info     | -                           | {userId, username, phone, role, roleName}        |
| POST | /auth/logout   | -                           | null                                             |

### Dish (需认证, 仅list免认证)

| 方法     | 路径                         | 参数                                                              | 返回         |
| ------ | -------------------------- | --------------------------------------------------------------- | ---------- |
| POST   | /dish                      | {categoryId, name, price, imageUrl, description, status, stock} | Dish       |
| PUT    | /dish                      | {id, ...}                                                       | Dish       |
| DELETE | /dish/:id                  |                                                                 | null       |
| GET    | /dish/page?page=&size=     |                                                                 | Page<Dish> |
| GET    | /dish/category/:categoryId |                                                                 | Dish[]     |
| GET    | /dish/list                 |                                                                 | Dish[]     |
| POST   | /dish/upload               | multipart file                                                  | URL字符串     |

### Category

| 方法     | 路径             | 返回         |
| ------ | -------------- | ---------- |
| POST   | /category      | Category   |
| PUT    | /category      | Category   |
| DELETE | /category/:id  | null       |
| GET    | /category/list | Category[] |

### Orders

| 方法   | 路径                                    | 参数                                                                                              | 返回                    |
| ---- | ------------------------------------- | ----------------------------------------------------------------------------------------------- | --------------------- |
| POST | /orders                               | {userId, totalAmount, tableNo, status?, orderDetails:[{dishId, dishName, quantity, unitPrice}]} | Orders(含orderNo)      |
| PUT  | /orders/status                        | query: orderNo, status                                                                          | null                  |
| GET  | /orders/page?page=&size=              |                                                                                                 | Page<Orders>          |
| GET  | /orders/list                          |                                                                                                 | Orders[] (当前用户)       |
| GET  | /orders/:orderNo                      |                                                                                                 | Orders(含orderDetails) |
| GET  | /orders/daily-details?date=yyyy-MM-dd |                                                                                                 | Orders[] (含明细)        |

### User (需认证)

| 方法     | 路径                     | 参数                                | 返回            |
| ------ | ---------------------- | --------------------------------- | ------------- |
| POST   | /user                  | {username, password, phone, role} | SysUser       |
| PUT    | /user                  | {id, username, phone, role}       | SysUser       |
| DELETE | /user/:id              |                                   | null          |
| GET    | /user/page?page=&size= |                                   | Page<SysUser> |
| GET    | /user/list             |                                   | SysUser[]     |
| GET    | /user/profile          |                                   | SysUser(无密码)  |
| PUT    | /user/profile          | {username, phone}                 | SysUser       |
| PUT    | /user/password         | {oldPassword, newPassword}        | null          |

### Restaurant & System & AI

| 方法   | 路径                 | 返回                   |
| ---- | ------------------ | -------------------- |
| GET  | /restaurant/info   | Restaurant           |
| POST | /restaurant/save   | Restaurant           |
| GET  | /system/config     | SystemConfig         |
| POST | /system/save       | SystemConfig         |
| GET  | /ai/analyze-orders | String(Markdown分析报告) |

---

## 认证与权限

**拦截器**: AuthInterceptor 拦截 `/api/**`，排除: `/api/auth/login`, `/api/auth/logout`, `/api/auth/register`, `/api/dish/list`, `/api/category/list`, `/api/restaurant/info`

**JWT**: payload含{userId, username, role} | 密钥随机生成(重启失效)

**角色路由**:
| 角色 | code | 前端路由 | API权限 |
|------|------|----------|---------|
| 顾客 | 0 | /customer | list+category+submitOrder+自己订单 |
| 管理员 | 1 | /admin | 全部 |
| 收银员 | 2 | /cashier, /pos | dish+category+orders+restaurant |
| 后厨 | 3 | /kitchen | orders |

---

## 后端核心逻辑

### 订单提交 (OrdersServiceImpl.submitOrder)

1. 校验菜品存在 & 库存充足
2. 生成orderNo = `yyyyMMddHHmmss` + UUID前8位大写
3. INSERT orders (status默认1)
4. INSERT order_details (批量)
5. 扣减库存 → stock≤0时 UPDATE dish SET status=0
6. @Transactional(rollbackFor=Exception.class)

### AI分析 (AiServiceImpl.analyzeOrders)

1. 读system_config.ai_api_key
2. 获取最近10订单(含明细)
3. POST https://api.deepseek.com/chat/completions
4. model: deepseek-reasoner
5. 返回Markdown分析报告

### 图片上传

- 保存路径: `./uploads/dishes/` (相对backend目录)
- WebMvcConfig映射: `/uploads/**` → `file:./uploads/dishes/` + `file:./uploads/dish image/`
- URL格式: `/uploads/dish_时间戳_原名.png`
- 前端拼接baseURL: 开发=`http://localhost:8083` | 生产=`http://47.116.30.247/`

### 跨域: allowedOrigins=* | methods=GET,POST,PUT,DELETE,OPTIONS | maxAge=3600

---

## 前端 (Web管理端)

**技术**: Vue3 + Vite5 + Pinia + VueRouter + TailwindCSS + ElementPlus + ECharts
**开发**: port 5173 | 代理: /api→localhost:8083, /uploads→localhost:8083
**生产API**: http://47.116.30.247:8083/api
**打包**: `npm run build` → dist/ | **APK**: Capacitor 8 → android/

### Pinia (stores/user.js)

```js
token: localStorage
userInfo: {userId, username, phone, role, roleName}
方法: login({username,password}), register({username,password,phone}), logout(), fetchUserInfo()
登录成功后自动fetchUserInfo()填充userInfo
```

### HTTP封装 (utils/request.js)

- baseURL: 开发='/api' | 生产='http://47.116.30.247:8083/api'
- imageBaseURL: 开发='http://localhost:8083' | 生产='http://47.116.30.247/'
- 自动附加Authorization header
- 401→跳转/login | 超时120s | 响应200直接返回data

### 页面组件

**LoginView.vue**: 登录/注册切换 | 测试账号快捷登录(admin/cashier/kitchen/customer / 123456) | 登录成功按role跳转

**CustomerMain.vue (role 0,1)**:

- 左侧分类栏 + 右侧菜品网格(status=1过滤)
- 底部购物车浮条(数量+总价)
- 桌号选择 A01-A10 | 提交订单到/api/orders

**AdminMain.vue (role 1)**:

- 仪表盘: 今日订单数/营业额 | 热销菜品Top5
- 菜品管理CRUD + 图片上传 + 一键补货(stock=999)
- 用户管理CRUD | 餐厅信息设置 | 个人设置

**CashierMain.vue (role 1,2)**:

- 分类浏览 + 菜品选择 + 购物车
- 桌号 A01-A10, B01-B05, V01-V03
- 结账提交订单

**KitchenMain.vue (role 1,3)**:

- 三列看板: 待接单(1) / 制作中(2) / 已完成(3)
- 接单(1→2) / 完成(2→3) 操作
- 30秒自动刷新

**OrdersView.vue (role 1,2,3)**:

- 订单列表分页 | 查看详情(含orderDetails)
- 管理员: 订单概览ECharts(选日期→选维度dish/category→饼图/柱图)
- 状态码: 0待付款/1待接单/2制作中/3已完成/4已取消

**SettingsView.vue (role 1)**:

- Tab: 基本设置/菜品管理/用户管理/个人设置/系统配置
- 餐厅信息CRUD | 菜品CRUD+图片上传 | 用户CRUD
- 个人信息修改 | 修改密码 | 系统配置
- AI分析按钮→调用/api/ai/analyze-orders→Markdown渲染

**PosMain.vue (role 1,2)**:

- 点餐收银界面 | 分类浏览 + 购物车
- AI智能分析按钮

---

## 小程序端 (miniprogram/)

**框架**: uni-app + Vue3 + Pinia + Vite
**页面**: index(首页) | login(登录) | orders(订单) | mine(个人中心)

### stores/cart.js

```js
items: [{id, name, price, imageUrl, quantity, stock}]
tableNo: string
方法: addItem(dish), removeItem(idx), increase/decreaseQuantity, clearCart, setTableNo, getOrderDetails()
持久化: uni.setStorageSync
库存检查: quantity<stock才允许加
```

### index.vue

- 分类横向滚动 + 菜品2列网格
- 库存显示: ≤0已售罄, ≤5库存紧张, 正常显示数量
- 底部购物车浮条 | 模态框购物车+桌号选择(A01-A10)
- 提交订单: POST /api/orders

### login.vue

- 登录/注册切换 | 4个测试账号快捷登录
- 登录成功→switchTab到index

### request.js (小程序)

- API_BASE: `http://47.116.30.247:8083` (生产)
- imageBaseURL: `http://47.116.30.247/`
- 自动附加Authorization header
- 401→clearUserAndRedirect→login页

---

## 启动方式

### 后端

```bash
cd backend
start.bat  # 或 mvn spring-boot:run
```

### 前端Web

```bash
cd frontend
npm run dev      # http://localhost:5173
npm run build    # → dist/
```

### 数据库

```sql
-- 执行顺序:
1. sql/create_tables.sql    -- 建库建表+初始用户/分类/餐厅
2. sql/insert_data.sql      -- 插入约80条菜品数据
3. sql/add_dish_stock.sql   -- ALTER TABLE dish ADD stock INT DEFAULT 999
```

---

## 关键业务规则

1. **订单状态流转**: 1(待接单) → 2(制作中) → 3(已完成) | 4(已取消)
2. **库存管理**: 下单自动扣减, stock≤0自动status=0(停售), 一键补货=999
3. **订单号**: yyyyMMddHHmmss + UUID前8位大写
4. **图片URL**: 以/uploads/开头, 前端自动拼接baseURL
5. **密码**: 明文存储(未加密)
6. **JWT密钥**: 随机UUID(重启后所有token失效)
7. **桌号**: 顾客端A01-A10 | 收银端A01-A10/B01-B05/V01-V03

## 测试账号 (密码均为123456)

| username | role | 说明  |
| -------- | ---- | --- |
| admin    | 1    | 管理员 |
| cashier  | 2    | 收银员 |
| kitchen  | 3    | 后厨  |
| customer | 0    | 顾客  |

## 分类数据

| id  | name  | sort |
| --- | ----- | ---- |
| 1   | 精品淮扬菜 | 1    |
| 2   | 主食    | 2    |
| 3   | 饮品    | 3    |
| 4   | 甜点    | 4    |

## 会员体系

### member - 会员表

| 字段            | 类型           | 说明             |
| ------------- | ------------ | -------------- |
| id            | BIGINT PK    | 自增             |
| user_id       | BIGINT       | FK→sys_user.id |
| phone         | VARCHAR(11)  | 手机号            |
| nickname      | VARCHAR(50)  | 昵称             |
| avatar        | VARCHAR(255) | 头像             |
| birthday      | DATE         | 生日             |
| gender        | TINYINT      | 0未知/1男/2女      |
| point         | INT          | 当前积分           |
| is_member     | TINYINT      | 0否/1是          |
| member_expire | DATE         | 到期日(NULL=永久)   |

### point_log - 积分流水

| id | member_id | point(正=获/负=耗) | type(1消费/2签到/3兑换/4调整/5生日) | source | create_time |

### coupon_template - 优惠券模板

| id | name | type(1满减/2折扣/3兑换) | discount_value | min_amount | dish_id | total_count | remain_count | valid_days | expire_date | status |

### coupon - 用户持有的优惠券

| id | member_id | template_id | status(0未用/1已用/2过期) | use_time | order_no | expire_date | create_time |

### dish_member_price - 菜品会员价

| id | dish_id | member_price | create_time |

### 会员API

| 方法   | 路径                               | 说明        |
| ---- | -------------------------------- | --------- |
| GET  | /api/member/info                 | 获取会员信息    |
| POST | /api/member/activate             | 开通会员      |
| PUT  | /api/member/profile              | 更新资料      |
| POST | /api/member/checkin              | 签到(+10积分) |
| GET  | /api/member/point/list           | 积分流水      |
| GET  | /api/coupon/template/list        | 可领取券列表    |
| POST | /api/coupon/template/receive/:id | 领取券       |
| GET  | /api/coupon/my/list              | 我的优惠券     |
| GET  | /api/dish/member-price/list      | 会员价列表     |
| POST | /api/dish/member-price/save      | 保存会员价     |

### 会员规则

- 签到: 每天+10积分
- 消费: 订单完成后按金额发放积分(可配置)
- 会员价: 有配置的菜品,会员购买时使用会员价替代原价
- 优惠券: 下单时可选使用,满减/折扣/兑换三种类型

---

## 依赖版本

| 后端           | 版本                                             |
| ------------ | ---------------------------------------------- |
| Spring Boot  | 2.7.18                                         |
| MyBatis-Plus | 3.5.5                                          |
| MySQL        | 8.0.33                                         |
| JWT          | 0.9.1                                          |
| Hutool       | 5.8.25                                         |
| FastJSON2    | 2.0.45                                         |
| Knife4j      | 4.3.0                                          |
| 前端           | Vue 3.4+Vite 5+Pinia 3+ECharts 5+ElementPlus 2 |
| 小程序          | uni-app(Vue3)+Pinia                            |
