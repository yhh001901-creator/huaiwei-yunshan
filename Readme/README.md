# 淮味云膳餐厅服务系统

## 项目简介

淮味云膳是一个完整的餐厅服务系统，采用前后端分离架构，支持多端应用。系统包含菜品管理、订单管理、会员管理、优惠券管理、AI推荐等多种功能，为餐厅提供全方位的数字化服务支持。

## 项目结构

```
淮味云膳 v6/
├── backend/                          # 后端服务（Spring Boot）
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── restaurant/
│   │   │   │           ├── Application.java        # 应用入口
│   │   │   │           ├── common/                  # 通用类
│   │   │   │           │   └── Result.java          # 统一返回结果类
│   │   │   │           ├── config/                  # 配置类
│   │   │   │           │   ├── Knife4jConfig.java    # Knife4j 配置
│   │   │   │           │   ├── MyBatisPlusConfig.java # MyBatis-Plus配置
│   │   │   │           │   └── WebMvcConfig.java     # Web MVC配置
│   │   │   │           ├── controller/              # 控制器
│   │   │   │           │   ├── AiController.java      # AI控制器
│   │   │   │           │   ├── AuthController.java    # 认证控制器
│   │   │   │           │   ├── CategoryController.java # 分类控制器
│   │   │   │           │   ├── CouponController.java  # 优惠券控制器
│   │   │   │           │   ├── CouponTemplateController.java # 优惠券模板控制器
│   │   │   │           │   ├── DishController.java    # 菜品控制器
│   │   │   │           │   ├── DishMemberPriceController.java # 菜品会员价控制器
│   │   │   │           │   ├── GlobalExceptionHandler.java # 全局异常处理器
│   │   │   │           │   ├── MemberController.java  # 会员控制器
│   │   │   │           │   ├── OrdersController.java  # 订单控制器
│   │   │   │           │   ├── RestaurantController.java # 餐厅控制器
│   │   │   │           │   ├── SystemController.java  # 系统控制器
│   │   │   │           │   └── SysUserController.java # 用户控制器
│   │   │   │           ├── dto/                     # 数据传输对象
│   │   │   │           │   ├── LoginDTO.java
│   │   │   │           │   ├── LoginVO.java
│   │   │   │           │   └── RegisterDTO.java
│   │   │   │           ├── entity/                  # 实体类
│   │   │   │           │   ├── Category.java        # 分类实体
│   │   │   │           │   ├── Coupon.java          # 优惠券实体
│   │   │   │           │   ├── CouponTemplate.java  # 优惠券模板实体
│   │   │   │           │   ├── Dish.java            # 菜品实体
│   │   │   │           │   ├── DishMemberPrice.java # 菜品会员价实体
│   │   │   │           │   ├── Member.java          # 会员实体
│   │   │   │           │   ├── OrderDetail.java     # 订单明细实体
│   │   │   │           │   ├── Orders.java          # 订单实体
│   │   │   │           │   ├── PointLog.java        # 积分日志实体
│   │   │   │           │   ├── Restaurant.java      # 餐厅实体
│   │   │   │           │   ├── SystemConfig.java    # 系统配置实体
│   │   │   │           │   └── SysUser.java         # 用户实体
│   │   │   │           ├── enums/                   # 枚举
│   │   │   │           │   └── UserRole.java        # 用户角色枚举
│   │   │   │           ├── interceptor/             # 拦截器
│   │   │   │           │   └── AuthInterceptor.java # 认证拦截器
│   │   │   │           ├── mapper/                  # Mapper接口
│   │   │   │           │   ├── CategoryMapper.java
│   │   │   │           │   ├── CouponMapper.java
│   │   │   │           │   ├── CouponTemplateMapper.java
│   │   │   │           │   ├── DishMapper.java
│   │   │   │           │   ├── DishMemberPriceMapper.java
│   │   │   │           │   ├── MemberMapper.java
│   │   │   │           │   ├── OrderDetailMapper.java
│   │   │   │           │   ├── OrdersMapper.java
│   │   │   │           │   ├── PointLogMapper.java
│   │   │   │           │   ├── RestaurantMapper.java
│   │   │   │           │   ├── SystemMapper.java
│   │   │   │           │   └── SysUserMapper.java
│   │   │   │           ├── service/                 # 服务层
│   │   │   │           │   ├── AiService.java
│   │   │   │           │   ├── CategoryService.java
│   │   │   │           │   ├── CouponService.java
│   │   │   │           │   ├── CouponTemplateService.java
│   │   │   │           │   ├── DishMemberPriceService.java
│   │   │   │           │   ├── DishService.java
│   │   │   │           │   ├── MemberService.java
│   │   │   │           │   ├── OrdersService.java
│   │   │   │           │   ├── PointLogService.java
│   │   │   │           │   ├── RestaurantService.java
│   │   │   │           │   ├── SystemService.java
│   │   │   │           │   ├── SysUserService.java
│   │   │   │           │   └── impl/                # 服务实现
│   │   │   │           │       ├── AiServiceImpl.java
│   │   │   │           │       ├── CategoryServiceImpl.java
│   │   │   │           │       ├── CouponServiceImpl.java
│   │   │   │           │       ├── CouponTemplateServiceImpl.java
│   │   │   │           │       ├── DishMemberPriceServiceImpl.java
│   │   │   │           │       ├── DishServiceImpl.java
│   │   │   │           │       ├── MemberServiceImpl.java
│   │   │   │           │       ├── OrdersServiceImpl.java
│   │   │   │           │       ├── PointLogServiceImpl.java
│   │   │   │           │       ├── RestaurantServiceImpl.java
│   │   │   │           │       ├── SystemServiceImpl.java
│   │   │   │           │       └── SysUserServiceImpl.java
│   │   │   │           └── utils/                   # 工具类
│   │   │   │               └── JwtUtil.java         # JWT工具类
│   │   │   └── resources/
│   │   │       ├── application.properties            # 应用配置
│   │   │       ├── application-prod.properties       # 生产环境配置
│   │   │       ├── test_users.sql                    # 测试用户SQL
│   │   │       └── static/                           # 静态资源
│   │   │           ├── index.html
│   │   │           └── assets/                       # 前端构建资源
│   │   └── test/
│   ├── sql/                            # SQL脚本
│   │   ├── add_dish_stock.sql          # 添加菜品库存
│   │   ├── alter_table.sql             # 表结构修改
│   │   ├── create_member_tables.sql    # 创建会员表
│   │   ├── create_tables.sql           # 创建表
│   │   ├── fix_image_paths.sql         # 修复图片路径
│   │   ├── insert_data.sql             # 插入数据
│   │   ├── rename_images.ps1           # 重命名图片
│   │   ├── test_insert.sql             # 测试数据插入
│   │   ├── update.sql                  # 更新SQL
│   │   └── update_orders.sql           # 更新订单
│   ├── uploads/                        # 上传文件目录
│   │   ├── dish image/                 # 菜品图片（分类）
│   │   │   ├── 主食/
│   │   │   ├── 淮扬菜/
│   │   │   ├── 甜品/
│   │   │   └── 饮料/
│   │   └── dishes/                     # 上传的菜品图片
│   ├── target/                         # 构建输出目录
│   ├── detail.md                       # 后端详细说明
│   ├── version.md                      # 版本信息
│   ├── pom.xml                         # Maven配置
│   ├── start.bat                       # 启动脚本
│   ├── stop_server.bat                 # 停止脚本（批处理）
│   ├── stop_server.ps1                 # 停止脚本（PowerShell）
│   └── 后端测试.json                   # 后端测试文件
│
├── frontend/                           # 前端Web端（Vue 3 + Vite）
│   ├── public/                         # 公共文件
│   │   └── favicon.ico                 # 网站图标
│   ├── src/
│   │   ├── api/                        # API接口
│   │   ├── assets/                     # 静态资源
│   │   ├── components/                 # 组件
│   │   ├── router/                     # 路由配置
│   │   ├── stores/                     # 状态管理（Pinia）
│   │   ├── utils/                      # 工具类
│   │   ├── views/                      # 页面视图
│   │   ├── App.vue                     # 根组件
│   │   └── main.js                     # 入口文件
│   ├── index.html                      # HTML模板
│   ├── package.json                    # NPM配置
│   ├── vite.config.js                  # Vite配置
│   ├── detail.md                       # 前端详细说明
│   ├── version.md                      # 前端版本信息
│   └── README.md                       # 前端说明
│
└── Readme/                             # 文档目录
    ├── README.md                       # 主说明文档
    ├── backend/                        # 后端文档
    │   └── detail.md                   # 后端详细说明
    ├── frontend/                       # 前端文档
    │   └── README.md                   # 前端说明
    └── mobile-app/                     # 移动端文档
        └── README.md                   # 移动端说明
```

## 技术栈

### 后端
- **框架**：Spring Boot 2.7.15
- **ORM**：MyBatis-Plus 3.5.5
- **数据库**：MySQL 8.0
- **API文档**：Knife4j 2.0.9
- **认证**：JWT
- **构建工具**：Maven
- **服务端口**：8083

### 前端Web端
- **框架**：Vue 3
- **构建工具**：Vite
- **状态管理**：Pinia
- **路由**：Vue Router
- **UI框架**：Element Plus
- **HTTP客户端**：Axios
- **开发端口**：5173

### 移动端
- **框架**：uni-app
- **基础框架**：Vue 3
- **状态管理**：Pinia

## 核心功能模块

### 1. 菜品管理
- 菜品CRUD操作
- 菜品分类管理
- 菜品图片上传
- 菜品会员价设置
- 菜品库存管理

### 2. 订单管理
- 订单创建与提交
- 订单状态管理（待付款、已付款、制作中、已完成、已取消）
- 订单查询与统计
- 订单详情查看
- 优惠券使用

### 3. 用户管理
- 用户登录与认证
- 用户信息管理
- 密码修改
- 角色权限管理（管理员、厨房、收银员等）

### 4. 会员管理
- 会员注册与登录
- 会员信息管理
- 积分管理（签到、消费）
- 会员开通与到期管理
- 会员列表查询
- 会员积分流水

### 5. 优惠券管理
- 优惠券模板管理
- 优惠券领取
- 优惠券发放
- 优惠券使用
- 优惠券状态管理

### 6. 系统管理
- 系统配置管理
- 餐厅信息管理
- 用户管理

### 7. AI功能
- AI菜品推荐
- AI菜单生成

### 8. 多端支持
- **客户端（顾客端）**：点餐、查看订单、会员中心、优惠券
- **管理端**：菜品管理、订单管理、会员管理、系统设置
- **收银端**：订单结账、支付处理
- **厨房端**：订单制作、状态更新
- **POS端**：点餐下单、快速开单
- **移动端小程序**：顾客移动点餐

## 快速开始

### 后端启动

1. 启动MySQL数据库
2. 执行 `backend/sql/create_tables.sql` 创建表结构
3. 执行 `backend/sql/insert_data.sql` 插入基础数据
4. 运行 `backend/start.bat` 或直接运行编译后的JAR文件启动后端服务
5. 访问 http://localhost:8083/doc.html 查看API文档

### 前端启动

1. 进入 `frontend` 目录
2. 执行 `npm install` 安装依赖
3. 执行 `npm run dev` 启动开发服务器
4. 访问 http://localhost:5173 查看应用
5. 执行 `npm run build` 构建生产版本

### 移动端启动

1. 进入 `mobile-app` 目录
2. 使用HBuilderX打开项目
3. 运行到浏览器或微信开发者工具

## 配置说明

### 后端配置
- **开发环境**：`backend/src/main/resources/application.properties`
- **生产环境**：`backend/src/main/resources/application-prod.properties`
- **服务端口**：8083
- **数据库**：huaiwei_yunshan
- **文件上传路径**：uploads/dishes/
- **最大文件大小**：10MB

### 前端配置
- **Vite配置**：`frontend/vite.config.js`
- **API地址**：`frontend/src/utils/request.js`
- **后端API**：http://localhost:8083/api

## 主要API接口

### 菜品相关
- `POST /api/dish` - 新增菜品
- `PUT /api/dish` - 修改菜品
- `DELETE /api/dish/{id}` - 删除菜品
- `GET /api/dish/list` - 获取所有菜品
- `GET /api/dish/category/{categoryId}` - 根据分类查询菜品
- `POST /api/dish/upload` - 上传菜品图片

### 订单相关
- `POST /api/orders` - 提交订单
- `PUT /api/orders/status` - 修改订单状态
- `GET /api/orders/list` - 获取所有订单
- `GET /api/orders/{orderNo}` - 获取订单详情

### 会员相关
- `GET /api/member/info` - 获取会员信息
- `POST /api/member/activate` - 开通会员
- `POST /api/member/checkin` - 会员签到
- `GET /api/member/list` - 获取会员列表
- `GET /api/member/point/list` - 获取积分流水

### 优惠券相关
- `GET /api/coupon/template/list` - 获取优惠券模板
- `POST /api/coupon/template/receive/{templateId}` - 领取优惠券
- `GET /api/coupon/my/list` - 获取我的优惠券

### 用户相关
- `POST /api/sysUser/login` - 用户登录
- `GET /api/sysUser/info` - 获取用户信息
- `GET /api/user/list` - 获取用户列表

## 项目特点

1. **完整的多端支持**：客户端、管理端、收银端、厨房端、POS端、移动端
2. **完善的会员体系**：积分、优惠券、会员等级
3. **AI智能推荐**：菜品推荐、菜单生成
4. **易于部署**：前后端分离，构建简单
5. **可扩展架构**：模块化设计，便于功能扩展

## 更新日志

### 2026-04-22
- 新增会员列表接口 `/member/list`
- 完善会员管理功能，包括开通、签到、积分流水
- 完善优惠券管理功能，包括领取、使用
- 修复管理员端会员信息更新问题
- 优化前端交互体验，添加签到成功反馈

### 2026-04-15
- 完成菜品图片上传功能
- 配置静态资源访问
- 优化文件上传处理
- 增强系统稳定性

### 2026-03-23
- 完成所有核心功能开发
- 优化业务逻辑实现
- 完善API接口

### 2026-03-22
- 初始项目搭建
- 完成核心功能模块
- 集成Knife4j API文档

## 相关文档

- [后端详细说明](../backend/detail.md)
- [后端版本信息](../backend/version.md)
- [前端说明](../frontend/README.md)
- [API接口文档](../api.md)
- [功能说明](../function.md)
