# 淮味云膳餐厅服务系统 - 后端说明

## 1. 项目结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── restaurant/
│   │   │           ├── Application.java                 # 应用入口
│   │   │           ├── common/                           # 通用类
│   │   │           │   └── Result.java                  # 统一返回结果类
│   │   │           ├── config/                           # 配置类
│   │   │           │   ├── Knife4jConfig.java           # Knife4j配置
│   │   │           │   ├── MyBatisPlusConfig.java       # MyBatis-Plus配置
│   │   │           │   └── WebMvcConfig.java            # Web MVC配置
│   │   │           ├── controller/                       # 控制器
│   │   │           │   ├── AiController.java            # AI控制器
│   │   │           │   ├── AuthController.java          # 认证控制器
│   │   │           │   ├── CategoryController.java      # 分类控制器
│   │   │           │   ├── CouponController.java        # 优惠券控制器
│   │   │           │   ├── CouponTemplateController.java # 优惠券模板控制器
│   │   │           │   ├── DishController.java          # 菜品控制器
│   │   │           │   ├── DishMemberPriceController.java # 菜品会员价控制器
│   │   │           │   ├── GlobalExceptionHandler.java  # 全局异常处理器
│   │   │           │   ├── MemberController.java        # 会员控制器
│   │   │           │   ├── OrdersController.java        # 订单控制器
│   │   │           │   ├── RestaurantController.java    # 餐厅控制器
│   │   │           │   ├── SystemController.java        # 系统控制器
│   │   │           │   └── SysUserController.java       # 用户控制器
│   │   │           ├── dto/                             # 数据传输对象
│   │   │           │   ├── LoginDTO.java
│   │   │           │   ├── LoginVO.java
│   │   │           │   └── RegisterDTO.java
│   │   │           ├── entity/                           # 实体类
│   │   │           │   ├── Category.java                # 分类实体
│   │   │           │   ├── Coupon.java                  # 优惠券实体
│   │   │           │   ├── CouponTemplate.java          # 优惠券模板实体
│   │   │           │   ├── Dish.java                    # 菜品实体
│   │   │           │   ├── DishMemberPrice.java         # 菜品会员价实体
│   │   │           │   ├── Member.java                  # 会员实体
│   │   │           │   ├── OrderDetail.java             # 订单明细实体
│   │   │           │   ├── Orders.java                  # 订单实体
│   │   │           │   ├── PointLog.java                # 积分日志实体
│   │   │           │   ├── Restaurant.java              # 餐厅实体
│   │   │           │   ├── SystemConfig.java            # 系统配置实体
│   │   │           │   └── SysUser.java                 # 用户实体
│   │   │           ├── enums/                            # 枚举
│   │   │           │   └── UserRole.java                # 用户角色枚举
│   │   │           ├── interceptor/                       # 拦截器
│   │   │           │   └── AuthInterceptor.java         # 认证拦截器
│   │   │           ├── mapper/                            # Mapper接口
│   │   │           │   ├── CategoryMapper.java
│   │   │           │   ├── CouponMapper.java
│   │   │           │   ├── CouponTemplateMapper.java
│   │   │           │   ├── DishMapper.java
│   │   │           │   ├── DishMemberPriceMapper.java
│   │   │           │   ├── MemberMapper.java
│   │   │           │   ├── OrderDetailMapper.java
│   │   │           │   ├── OrdersMapper.java
│   │   │           │   ├── PointLogMapper.java
│   │   │           │   ├── RestaurantMapper.java
│   │   │           │   ├── SystemMapper.java
│   │   │           │   └── SysUserMapper.java
│   │   │           ├── service/                           # 服务层
│   │   │           │   ├── AiService.java
│   │   │           │   ├── CategoryService.java
│   │   │           │   ├── CouponService.java
│   │   │           │   ├── CouponTemplateService.java
│   │   │           │   ├── DishMemberPriceService.java
│   │   │           │   ├── DishService.java
│   │   │           │   ├── MemberService.java
│   │   │           │   ├── OrdersService.java
│   │   │           │   ├── PointLogService.java
│   │   │           │   ├── RestaurantService.java
│   │   │           │   ├── SystemService.java
│   │   │           │   ├── SysUserService.java
│   │   │           │   └── impl/                        # 服务实现
│   │   │           │       ├── AiServiceImpl.java
│   │   │           │       ├── CategoryServiceImpl.java
│   │   │           │       ├── CouponServiceImpl.java
│   │   │           │       ├── CouponTemplateServiceImpl.java
│   │   │           │       ├── DishMemberPriceServiceImpl.java
│   │   │           │       ├── DishServiceImpl.java
│   │   │           │       ├── MemberServiceImpl.java
│   │   │           │       ├── OrdersServiceImpl.java
│   │   │           │       ├── PointLogServiceImpl.java
│   │   │           │       ├── RestaurantServiceImpl.java
│   │   │           │       ├── SystemServiceImpl.java
│   │   │           │       └── SysUserServiceImpl.java
│   │   │           └── utils/                           # 工具类
│   │   │               └── JwtUtil.java                  # JWT工具类
│   │   └── resources/
│   │       ├── application.properties                     # 应用配置
│   │       └── application-prod.properties                # 生产环境配置
│   └── test/
├── sql/                                                 # SQL脚本
│   ├── create_tables.sql                                # 创建表
│   ├── insert_data.sql                                  # 插入数据
│   └── create_member_tables.sql                        # 创建会员表
├── uploads/                                             # 上传文件目录
│   ├── dish image/                                      # 菜品图片
│   └── dishes/                                          # 上传的菜品图片
├── pom.xml                                             # Maven配置
├── start.bat                                           # 启动脚本
├── version.md                                          # 版本信息
└── detail.md                                           # 本文档
```

## 2. 技术栈

- **Spring Boot**: 2.7.15
- **MyBatis-Plus**: 3.5.5
- **MySQL**: 8.0
- **Knife4j**: 2.0.9
- **JWT**: 用于用户认证
- **Maven**: 项目构建工具
- **Lombok**: 简化Java代码

## 3. 核心功能

### 3.1 菜品管理

- 菜品的增删改查
- 菜品分类管理
- 菜品图片上传
- 菜品会员价设置
- 菜品库存管理

### 3.2 订单管理

- 订单创建与提交
- 订单状态管理（待付款、已付款、制作中、已完成、已取消）
- 订单查询与统计
- 订单详情查看
- 优惠券使用

### 3.3 用户管理

- 用户登录与认证
- 用户信息管理
- 密码修改
- 角色权限管理

### 3.4 会员管理

- 会员注册与登录
- 会员信息管理
- 积分管理（签到、消费）
- 会员开通与到期管理
- 会员列表查询
- 积分流水记录

### 3.5 优惠券管理

- 优惠券模板管理
- 优惠券领取
- 优惠券发放
- 优惠券使用
- 优惠券状态管理

### 3.6 系统管理

- 系统配置管理
- 餐厅信息管理
- 用户管理

### 3.7 AI功能

- AI菜品推荐
- AI菜单生成

## 4. 主要API接口

### 4.1 菜品相关

- `POST /api/dish` - 新增菜品
- `PUT /api/dish` - 修改菜品
- `DELETE /api/dish/{id}` - 删除菜品
- `GET /api/dish/list` - 获取所有菜品
- `GET /api/dish/category/{categoryId}` - 根据分类查询菜品
- `POST /api/dish/upload` - 上传菜品图片

### 4.2 订单相关

- `POST /api/orders` - 提交订单
- `PUT /api/orders/status` - 修改订单状态
- `GET /api/orders/list` - 获取所有订单
- `GET /api/orders/{orderNo}` - 获取订单详情

### 4.3 会员相关

- `GET /api/member/info` - 获取会员信息
- `POST /api/member/activate` - 开通会员
- `POST /api/member/checkin` - 会员签到
- `GET /api/member/list` - 获取会员列表
- `GET /api/member/point/list` - 获取积分流水

### 4.4 优惠券相关

- `GET /api/coupon/template/list` - 获取优惠券模板
- `POST /api/coupon/template/receive/{templateId}` - 领取优惠券
- `GET /api/coupon/my/list` - 获取我的优惠券

### 4.5 用户相关

- `POST /api/sysUser/login` - 用户登录
- `GET /api/sysUser/info` - 获取用户信息
- `GET /api/user/list` - 获取用户列表

## 5. 配置说明

### 5.1 后端配置

- **服务端口**: 8083
- **数据库**: huaiwei_yunshan
- **文件上传路径**: uploads/dishes/
- **最大文件大小**: 10MB
- **API文档访问**: http://localhost:8083/doc.html

## 6. 运行说明

### 6.1 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+

### 6.2 运行步骤

1. 启动MySQL数据库
2. 执行 `backend/sql/create_tables.sql` 创建表结构
3. 执行 `backend/sql/insert_data.sql` 插入基础数据
4. 运行 `backend/start.bat` 或执行 `mvn spring-boot:run` 启动后端服务
5. 访问 http://localhost:8083/doc.html 查看API文档

## 7. 数据库表结构

### 7.1 主要数据表

- **sys_user**: 用户表
- **restaurant**: 餐厅表
- **system_config**: 系统配置表
- **category**: 菜品分类表
- **dish**: 菜品表
- **dish_member_price**: 菜品会员价表
- **orders**: 订单表
- **order_detail**: 订单明细表
- **member**: 会员表
- **coupon_template**: 优惠券模板表
- **coupon**: 优惠券表
- **point_log**: 积分日志表

## 8. 文件上传说明

### 8.1 上传接口

- **路径**: `POST /api/dish/upload`
- **参数**: `file` (MultipartFile)
- **存储位置**: `uploads/dishes/`
- **访问路径**: `http://localhost:8083/uploads/{filename}`

### 8.2 上传限制

- 最大文件大小: 10MB
- 支持格式: JPG、PNG、GIF
- 文件命名规则: `dish_{timestamp}_{original_filename}`

## 9. 安全说明

### 9.1 用户认证

- 使用JWT Token进行用户认证
- 登录接口返回Token
- Token过期时间: 7天

### 9.2 接口权限

- 部分接口需要登录才能访问
- 使用拦截器进行权限校验

### 9.3 安全建议

- 生产环境修改默认密码
- 配置HTTPS
- 定期备份数据库
- 设置合理的CORS策略

## 10. 版本历史

### 10.1 v1.1.0 (2026-04-22)

- 新增会员列表接口
- 完善会员管理功能
- 完善优惠券管理功能
- 修复管理员端会员信息更新问题
- 优化前端交互体验

### 10.2 v1.0.0 (2026-03-23)

- 初始版本发布
- 完成所有核心功能
- 优化业务逻辑实现
- 完善API接口文档
