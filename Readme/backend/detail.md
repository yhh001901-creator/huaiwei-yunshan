# 淮味云膳餐馆服务系统 - 后端说明

## 1. 项目结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       ├── huaiwei/
│   │   │       │   └── yunshan/
│   │   │       │       └── entity/
│   │   │       └── restaurant/
│   │   │           ├── Application.java              # 应用入口
│   │   │           ├── common/                       # 通用类
│   │   │           │   └── Result.java               # 统一返回结果类
│   │   │           ├── config/                       # 配置类
│   │   │           │   ├── Knife4jConfig.java        # Knife4j 配置
│   │   │           │   ├── MyBatisPlusConfig.java    # MyBatis-Plus 配置
│   │   │           │   └── WebMvcConfig.java         # Web MVC 配置
│   │   │           ├── controller/                   # 控制器
│   │   │           │   ├── AiController.java         # AI 控制器
│   │   │           │   ├── AuthController.java       # 认证控制器
│   │   │           │   ├── CategoryController.java   # 分类控制器
│   │   │           │   ├── CouponController.java     # 优惠券控制器
│   │   │           │   ├── CouponTemplateController.java # 优惠券模板控制器
│   │   │           │   ├── DishController.java       # 菜品控制器
│   │   │           │   ├── DishMemberPriceController.java # 菜品会员价控制器
│   │   │           │   ├── GlobalExceptionHandler.java # 全局异常处理器
│   │   │           │   ├── MemberController.java     # 会员控制器
│   │   │           │   ├── OrdersController.java     # 订单控制器
│   │   │           │   ├── RestaurantController.java # 餐厅控制器
│   │   │           │   ├── SystemController.java     # 系统控制器
│   │   │           │   └── SysUserController.java    # 系统用户控制器
│   │   │           ├── dto/                          # 数据传输对象
│   │   │           │   ├── LoginDTO.java             # 登录 DTO
│   │   │           │   ├── LoginVO.java              # 登录 VO
│   │   │           │   └── RegisterDTO.java          # 注册 DTO
│   │   │           ├── entity/                       # 实体类
│   │   │           │   ├── Category.java             # 分类实体
│   │   │           │   ├── Coupon.java               # 优惠券实体
│   │   │           │   ├── CouponTemplate.java       # 优惠券模板实体
│   │   │           │   ├── Dish.java                 # 菜品实体
│   │   │           │   ├── DishMemberPrice.java      # 菜品会员价实体
│   │   │           │   ├── Member.java               # 会员实体
│   │   │           │   ├── OrderDetail.java          # 订单明细实体
│   │   │           │   ├── Orders.java               # 订单实体
│   │   │           │   ├── PointLog.java             # 积分日志实体
│   │   │           │   ├── Restaurant.java           # 餐厅实体
│   │   │           │   ├── SystemConfig.java         # 系统配置实体
│   │   │           │   └── SysUser.java              # 系统用户实体
│   │   │           ├── enums/                        # 枚举
│   │   │           │   └── UserRole.java             # 用户角色枚举
│   │   │           ├── interceptor/                  # 拦截器
│   │   │           │   └── AuthInterceptor.java      # 认证拦截器
│   │   │           ├── mapper/                       # Mapper 接口
│   │   │           │   ├── CategoryMapper.java       # 分类 Mapper
│   │   │           │   ├── CouponMapper.java         # 优惠券 Mapper
│   │   │           │   ├── CouponTemplateMapper.java # 优惠券模板 Mapper
│   │   │           │   ├── DishMapper.java           # 菜品 Mapper
│   │   │           │   ├── DishMemberPriceMapper.java # 菜品会员价 Mapper
│   │   │           │   ├── MemberMapper.java         # 会员 Mapper
│   │   │           │   ├── OrderDetailMapper.java    # 订单明细 Mapper
│   │   │           │   ├── OrdersMapper.java         # 订单 Mapper
│   │   │           │   ├── PointLogMapper.java       # 积分日志 Mapper
│   │   │           │   ├── RestaurantMapper.java     # 餐厅 Mapper
│   │   │           │   ├── SystemMapper.java         # 系统 Mapper
│   │   │           │   └── SysUserMapper.java        # 系统用户 Mapper
│   │   │           ├── service/                      # 服务层
│   │   │           │   ├── AiService.java            # AI 服务接口
│   │   │           │   ├── CategoryService.java      # 分类服务接口
│   │   │           │   ├── CouponService.java        # 优惠券服务接口
│   │   │           │   ├── CouponTemplateService.java # 优惠券模板服务接口
│   │   │           │   ├── DishMemberPriceService.java # 菜品会员价服务接口
│   │   │           │   ├── DishService.java          # 菜品服务接口
│   │   │           │   ├── MemberService.java        # 会员服务接口
│   │   │           │   ├── OrdersService.java        # 订单服务接口
│   │   │           │   ├── PointLogService.java      # 积分日志服务接口
│   │   │           │   ├── RestaurantService.java    # 餐厅服务接口
│   │   │           │   ├── SystemService.java        # 系统服务接口
│   │   │           │   ├── SysUserService.java       # 系统用户服务接口
│   │   │           │   └── impl/                     # 服务实现
│   │   │           │       ├── AiServiceImpl.java    # AI 服务实现
│   │   │           │       ├── CategoryServiceImpl.java # 分类服务实现
│   │   │           │       ├── CouponServiceImpl.java # 优惠券服务实现
│   │   │           │       ├── CouponTemplateServiceImpl.java # 优惠券模板服务实现
│   │   │           │       ├── DishMemberPriceServiceImpl.java # 菜品会员价服务实现
│   │   │           │       ├── DishServiceImpl.java  # 菜品服务实现
│   │   │           │       ├── MemberServiceImpl.java # 会员服务实现
│   │   │           │       ├── OrdersServiceImpl.java # 订单服务实现
│   │   │           │       ├── PointLogServiceImpl.java # 积分日志服务实现
│   │   │           │       ├── RestaurantServiceImpl.java # 餐厅服务实现
│   │   │           │       ├── SystemServiceImpl.java # 系统服务实现
│   │   │           │       └── SysUserServiceImpl.java # 系统用户服务实现
│   │   │           └── utils/                        # 工具类
│   │   │               └── JwtUtil.java              # JWT 工具类
│   │   └── resources/
│   │       ├── application.properties                # 应用配置
│   │       ├── application-prod.properties           # 生产环境配置
│   │       ├── test_users.sql                        # 测试用户 SQL
│   │       └── static/                               # 静态资源
│   │           ├── index.html                        # 首页
│   │           └── assets/                           # 前端构建资源
│   │               ├── AdminMain-*.js                # 管理端 JS
│   │               ├── CashierMain-*.js              # 收银端 JS
│   │               ├── CustomerMain-*.js             # 客户端 JS
│   │               ├── CustomerMain-*.css            # 客户端 CSS
│   │               ├── index-*.js                    # 首页 JS
│   │               ├── index-*.css                   # 首页 CSS
│   │               ├── KitchenMain-*.js              # 厨房端 JS
│   │               ├── LoginView-*.js                # 登录页 JS
│   │               ├── LoginView-*.css               # 登录页 CSS
│   │               ├── OrdersView-*.js               # 订单页 JS
│   │               ├── OrdersView-*.css              # 订单页 CSS
│   │               ├── PosMain-*.js                  # POS 端 JS
│   │               ├── PosMain-*.css                 # POS 端 CSS
│   │               ├── SettingsView-*.js             # 设置页 JS
│   │               └── SettingsView-*.css            # 设置页 CSS
│   └── test/                                         # 测试代码
├── sql/                                              # SQL 脚本
│   ├── add_dish_stock.sql                            # 添加菜品库存
│   ├── alter_table.sql                               # 表结构修改
│   ├── create_member_tables.sql                      # 创建会员表
│   ├── create_tables.sql                             # 创建表
│   ├── fix_image_paths.sql                           # 修复图片路径
│   ├── insert_data.sql                               # 插入数据
│   ├── rename_images.ps1                             # 重命名图片
│   ├── test_insert.sql                               # 测试数据插入
│   ├── update.sql                                    # 更新 SQL
│   └── update_orders.sql                             # 更新订单
├── uploads/                                          # 上传文件目录
│   ├── dish image/                                   # 菜品图片（分类）
│   │   ├── 主食/                                     # 主食类图片
│   │   │   ├── 刀削面.png
│   │   │   ├── 小米粥.png
│   │   │   ├── 扬州炒饭.png
│   │   │   ├── 担担面.png
│   │   │   ├── 汤包.png
│   │   │   ├── 油条.png
│   │   │   ├── 灌汤包.png
│   │   │   ├── 炒面.png
│   │   │   ├── 炸酱面.png
│   │   │   ├── 生煎包.png
│   │   │   ├── 米饭.png
│   │   │   ├── 红烧牛肉面.png
│   │   │   ├── 肉夹馍.png
│   │   │   ├── 腊八粥.png
│   │   │   ├── 葱油拌面.png
│   │   │   ├── 蛋炒饭.png
│   │   │   ├── 西红柿鸡蛋面.png
│   │   │   ├── 饺子.png
│   │   │   ├── 馄饨.png
│   │   │   └── 馒头.png
│   │   ├── 淮扬菜/                                   # 淮扬菜图片
│   │   │   ├── 可乐鸡翅.png
│   │   │   ├── 回锅肉.png
│   │   │   ├── 宫保鸡丁.png
│   │   │   ├── 梅菜扣肉.png
│   │   │   ├── 椒盐虾.png
│   │   │   ├── 水煮鱼.png
│   │   │   ├── 清炒时蔬.png
│   │   │   ├── 清蒸鲈鱼.png
│   │   │   ├── 白切鸡.png
│   │   │   ├── 糖醋排骨.png
│   │   │   ├── 红烧狮子头.png
│   │   │   ├── 红烧肉.png
│   │   │   ├── 蒜蓉扇贝.png
│   │   │   ├── 蒜蓉西兰花.png
│   │   │   ├── 西红柿炒蛋.png
│   │   │   ├── 酸辣土豆丝.png
│   │   │   ├── 酸辣汤.png
│   │   │   ├── 青椒肉丝.png
│   │   │   ├── 鱼香肉丝.png
│   │   │   └── 麻婆豆腐.png
│   │   ├── 甜品/                                     # 甜品类图片
│   │   │   ├── 双皮奶.png
│   │   │   ├── 巧克力蛋糕.png
│   │   │   ├── 提拉米苏.png
│   │   │   ├── 杨枝甘露.png
│   │   │   ├── 糯米糍.png
│   │   │   ├── 绿豆糕.png
│   │   │   ├── 芒果冰淇淋.png
│   │   │   ├── 芝士蛋糕.png
│   │   │   ├── 草莓蛋糕.png
│   │   │   └── 银耳汤.png
│   │   └── 饮料/                                     # 饮料类图片
│   │       ├── 可口可乐.png
│   │       ├── 啤酒.png
│   │       ├── 奶茶.png
│   │       ├── 果汁.png
│   │       ├── 矿泉水.png
│   │       ├── 红茶.png
│   │       ├── 绿茶.png
│   │       ├── 芬达.png
│   │       ├── 茉莉花茶.png
│   │       └── 雪碧.png
│   └── dishes/                                       # 上传的菜品图片
│       ├── dish_*_*.png                              # 动态上传的菜品图片
│       └── ...
├── target/                                           # 构建输出目录
│   ├── classes/                                      # 编译后的类文件
│   │   ├── com/restaurant/                           # 业务类
│   │   └── static/                                   # 静态资源
│   ├── generated-sources/                            # 生成的源代码
│   ├── generated-test-sources/                       # 生成的测试源代码
│   ├── maven-status/                                 # Maven 状态
│   └── test-classes/                                 # 测试类
├── .apt_generated_tests/                             # APT 生成的测试代码
├── detail.md                                         # 后端详细说明
├── version.md                                        # 版本信息
├── Dockerfile                                        # Docker 配置
├── pom.xml                                           # Maven 配置
├── start.bat                                         # 启动脚本
├── stop_server.bat                                   # 停止脚本（批处理）
├── stop_server.ps1                                   # 停止脚本（PowerShell）
└── 后端测试.json                                       # 后端测试文件
```

## 2. 技术栈

- **框架**: Spring Boot 2.7.15
- **ORM**: MyBatis-Plus 3.5.5
- **数据库**: MySQL 8.0
- **API 文档**: Knife4j 2.0.9
- **认证**: JWT
- **构建工具**: Maven

## 3. 核心功能

### 3.1 菜品管理

- 新增菜品
- 修改菜品
- 删除菜品
- 分页查询菜品列表
- 根据分类 ID 查询菜品列表
- 获取所有菜品列表
- 菜品图片上传
- 菜品会员价管理

### 3.2 订单管理

- 提交订单
- 修改订单状态
- 分页查询订单列表
- 获取所有订单列表
- 订单详情查询

### 3.3 分类管理

- 新增分类
- 修改分类
- 删除分类
- 获取所有分类列表

### 3.4 用户管理

- 用户登录
- 用户信息管理
- 密码修改
- 用户权限管理

### 3.5 会员管理

- 会员注册
- 会员信息管理
- 会员积分管理
- 会员等级管理

### 3.6 优惠券管理

- 优惠券模板管理
- 优惠券发放
- 优惠券使用
- 优惠券查询

### 3.7 系统管理

- 系统配置管理
- 餐厅信息管理
- 全局异常处理

### 3.8 AI 功能

- AI 菜品推荐
- AI 菜单生成

## 4. API 接口

### 4.1 菜品相关

- 新增菜品：`POST /api/dish`
- 修改菜品：`PUT /api/dish`
- 删除菜品：`DELETE /api/dish/{id}`
- 分页查询菜品：`GET /api/dish/page?page={page}&size={size}`
- 根据分类查询菜品：`GET /api/dish/category/{categoryId}`
- 获取所有菜品：`GET /api/dish/list`
- 上传菜品图片：`POST /api/dish/upload`
- 菜品会员价管理：`/api/dish/memberPrice`

### 4.2 订单相关

- 提交订单：`POST /api/orders`
- 修改订单状态：`PUT /api/orders/status?orderNo={orderNo}&status={status}`
- 分页查询订单：`GET /api/orders/page?page={page}&size={size}`
- 获取所有订单：`GET /api/orders/list`
- 订单详情：`GET /api/orders/detail/{orderNo}`

### 4.3 分类相关

- 新增分类：`POST /api/category`
- 修改分类：`PUT /api/category`
- 删除分类：`DELETE /api/category/{id}`
- 获取所有分类：`GET /api/category/list`

### 4.4 用户相关

- 用户登录：`POST /api/sysUser/login`
- 获取用户信息：`GET /api/sysUser/info`
- 修改密码：`PUT /api/sysUser/password`
- 用户管理：`/api/sysUser/*`

### 4.5 会员相关

- 会员注册：`POST /api/member/register`
- 会员登录：`POST /api/member/login`
- 会员信息：`GET /api/member/info`
- 会员积分：`GET /api/member/points`

### 4.6 优惠券相关

- 优惠券模板列表：`GET /api/couponTemplate/list`
- 领取优惠券：`POST /api/coupon/receive`
- 我的优惠券：`GET /api/coupon/my`
- 使用优惠券：`POST /api/coupon/use`

### 4.7 系统相关

- 获取系统配置：`GET /api/system/config`
- 更新系统配置：`PUT /api/system/config`
- 餐厅信息：`GET /api/restaurant/info`

### 4.8 AI 相关

- AI 菜品推荐：`GET /api/ai/recommend`
- AI 菜单生成：`POST /api/ai/menu`

### 4.9 认证相关

- 登录：`POST /api/auth/login`
- 登出：`POST /api/auth/logout`
- 刷新 Token: `POST /api/auth/refresh`

## 5. 配置说明

### 5.1 基础配置

- 服务器端口：8082
- 数据库连接：`localhost:3306/huaiwei_yunshan`
- 数据库用户名：`root`
- 数据库密码：`123456`

### 5.2 文件上传配置

- 文件上传路径：`uploads/`
- 最大文件大小：10MB
- 支持的图片格式：JPG、PNG、JPEG、GIF

### 5.3 JWT 配置

- Token 有效期：24 小时
- 刷新 Token 有效期：7 天

## 6. 运行说明

### 6.1 环境要求

- JDK 8+
- Maven 3.6+
- MySQL 8.0+

### 6.2 启动步骤

1. 启动 MySQL 数据库
2. 执行 `sql/create_tables.sql` 创建表结构
3. 执行 `sql/create_member_tables.sql` 创建会员表
4. 执行 `sql/insert_data.sql` 插入基础数据
5. 运行 `start.bat` 启动后端服务
6. 访问 http://localhost:8082/doc.html 查看 API 文档

### 6.3 生产环境部署

1. 修改 `application-prod.properties` 配置
2. 使用 Maven 打包：`mvn clean package`
3. 运行：`java -jar target/restaurant-0.0.1-SNAPSHOT.jar`

## 7. 图片上传说明

### 7.1 上传接口

- 上传路径：`POST /api/dish/upload`
- 上传字段：`file`（multipart/form-data）
- 图片存储：`uploads/dishes/` 目录
- 图片访问：`http://localhost:8082/uploads/{filename}`

### 7.2 图片分类

系统支持按菜品分类存储图片：

- `uploads/dish image/主食/` - 主食类图片
- `uploads/dish image/淮扬菜/` - 淮扬菜图片
- `uploads/dish image/甜品/` - 甜品类图片
- `uploads/dish image/饮料/` - 饮料类图片

### 7.3 上传限制

- 支持格式：JPG、PNG、JPEG、GIF
- 最大大小：10MB
- 自动重命名：`dish_{timestamp}_{originalName}.png`

## 8. 数据库表结构

### 8.1 核心表

- `sys_user` - 系统用户表
- `member` - 会员表
- `category` - 菜品分类表
- `dish` - 菜品表
- `dish_member_price` - 菜品会员价表
- `orders` - 订单表
- `order_detail` - 订单明细表
- `restaurant` - 餐厅信息表
- `system_config` - 系统配置表

### 8.2 营销表

- `coupon_template` - 优惠券模板表
- `coupon` - 优惠券表
- `point_log` - 积分日志表

## 9. 安全说明

### 9.1 认证机制

- 采用 JWT Token 进行身份认证
- Token 存储在请求头：`Authorization: Bearer {token}`
- 未登录访问受保护接口将返回 401 错误

### 9.2 权限控制

- 通过 `@Auth` 注解控制接口访问权限
- 支持按用户角色进行权限控制
- 管理员、员工、会员等不同角色拥有不同权限

### 9.3 密码安全

- 密码采用 BCrypt 加密存储
- 密码长度要求：6-20 位
- 支持密码强度校验

## 10. 常见问题

### 10.1 启动失败

- 检查端口 8082 是否被占用
- 检查数据库连接配置
- 检查 JDK 版本是否为 8+

### 10.2 图片上传失败

- 检查 `uploads/` 目录是否有写权限
- 检查文件大小是否超过 10MB
- 检查文件是否为支持的图片格式

### 10.3 API 访问失败

- 检查 Token 是否有效
- 检查请求方法和参数是否正确
- 查看后端日志定位问题
