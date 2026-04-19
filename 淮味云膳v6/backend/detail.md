# 淮味云膳餐馆服务系统 - 后端说明

## 1. 项目结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── restaurant/
│   │   │           ├── common/          # 通用类
│   │   │           │   └── Result.java   # 统一返回结果类
│   │   │           ├── config/           # 配置类
│   │   │           │   ├── Knife4jConfig.java      # Knife4j配置
│   │   │           │   ├── MyBatisPlusConfig.java  # MyBatis-Plus配置
│   │   │           │   └── WebMvcConfig.java       # Web MVC配置
│   │   │           ├── controller/       # 控制器
│   │   │           │   ├── AiController.java        # AI控制器
│   │   │           │   ├── CategoryController.java  # 分类控制器
│   │   │           │   ├── DishController.java      # 菜品控制器
│   │   │           │   ├── GlobalExceptionHandler.java  # 全局异常处理器
│   │   │           │   ├── OrdersController.java    # 订单控制器
│   │   │           │   ├── RestaurantController.java # 餐厅控制器
│   │   │           │   ├── SysUserController.java    # 用户控制器
│   │   │           │   └── SystemController.java     # 系统控制器
│   │   │           ├── entity/           # 实体类
│   │   │           │   ├── Category.java      # 分类实体
│   │   │           │   ├── Dish.java          # 菜品实体
│   │   │           │   ├── OrderDetail.java   # 订单明细实体
│   │   │           │   ├── Orders.java        # 订单实体
│   │   │           │   ├── Restaurant.java    # 餐厅实体
│   │   │           │   ├── SysUser.java       # 用户实体
│   │   │           │   └── SystemConfig.java  # 系统配置实体
│   │   │           ├── mapper/            # Mapper接口
│   │   │           │   ├── CategoryMapper.java      # 分类Mapper
│   │   │           │   ├── DishMapper.java          # 菜品Mapper
│   │   │           │   ├── OrderDetailMapper.java   # 订单明细Mapper
│   │   │           │   ├── OrdersMapper.java        # 订单Mapper
│   │   │           │   ├── RestaurantMapper.java    # 餐厅Mapper
│   │   │           │   ├── SysUserMapper.java       # 用户Mapper
│   │   │           │   └── SystemMapper.java        # 系统Mapper
│   │   │           ├── service/           # 服务层
│   │   │           │   ├── impl/          # 服务实现
│   │   │           │   │   ├── AiServiceImpl.java        # AI服务实现
│   │   │           │   │   ├── CategoryServiceImpl.java  # 分类服务实现
│   │   │           │   │   ├── DishServiceImpl.java      # 菜品服务实现
│   │   │           │   │   ├── OrdersServiceImpl.java    # 订单服务实现
│   │   │           │   │   ├── RestaurantServiceImpl.java # 餐厅服务实现
│   │   │           │   │   ├── SysUserServiceImpl.java    # 用户服务实现
│   │   │           │   │   └── SystemServiceImpl.java     # 系统服务实现
│   │   │           │   ├── AiService.java        # AI服务接口
│   │   │           │   ├── CategoryService.java  # 分类服务接口
│   │   │           │   ├── DishService.java      # 菜品服务接口
│   │   │           │   ├── OrdersService.java    # 订单服务接口
│   │   │           │   ├── RestaurantService.java # 餐厅服务接口
│   │   │           │   ├── SysUserService.java    # 用户服务接口
│   │   │           │   └── SystemService.java     # 系统服务接口
│   │   │           └── Application.java        # 应用入口
│   │   └── resources/               # 资源文件
│   │       └── application.properties  # 应用配置
│   └── test/                        # 测试代码
├── sql/                            # SQL脚本
│   ├── alter_table.sql             # 表结构修改SQL
│   ├── create_tables.sql           # 建表SQL
│   ├── insert_data.sql             # 数据插入SQL
│   └── test_insert.sql             # 测试数据插入SQL
├── target/                         # 构建输出
├── uploads/                        # 上传文件目录
│   ├── dish image/                 # 菜品图片
│   └── dishes/                     # 上传的菜品图片
├── pom.xml                         # Maven配置
├── start.bat                       # 启动脚本
├── stop_server.bat                 # 停止脚本（批处理）
├── stop_server.ps1                 # 停止脚本（PowerShell）
├── version.md                      # 版本信息
└── 后端测试.json                     # 测试文件
```

## 2. 技术栈

- Spring Boot 2.7.15
- MyBatis-Plus 3.5.5
- MySQL 8.0
- Knife4j 2.0.9（API文档）

## 3. 核心功能

### 3.1 菜品管理

- 新增菜品
- 修改菜品
- 删除菜品
- 分页查询菜品列表
- 根据分类ID查询菜品列表
- 获取所有菜品列表
- 菜品图片上传

### 3.2 订单管理

- 提交订单
- 修改订单状态
- 分页查询订单列表
- 获取所有订单列表

### 3.3 分类管理

- 新增分类
- 修改分类
- 删除分类
- 获取所有分类列表

### 3.4 用户管理

- 用户登录
- 用户信息管理
- 密码修改

### 3.5 系统管理

- 系统配置管理

### 3.6 AI功能

- AI菜品推荐
- AI菜单生成

## 4. API接口

### 4.1 菜品相关

- 新增菜品：POST /api/dish
- 修改菜品：PUT /api/dish
- 删除菜品：DELETE /api/dish/{id}
- 分页查询菜品：GET /api/dish/page?page={page}&size={size}
- 根据分类查询菜品：GET /api/dish/category/{categoryId}
- 获取所有菜品：GET /api/dish/list
- 上传菜品图片：POST /api/dish/upload

### 4.2 订单相关

- 提交订单：POST /api/orders
- 修改订单状态：PUT /api/orders/status?orderNo={orderNo}&status={status}
- 分页查询订单：GET /api/orders/page?page={page}&size={size}
- 获取所有订单：GET /api/orders/list

### 4.3 分类相关

- 新增分类：POST /api/category
- 修改分类：PUT /api/category
- 删除分类：DELETE /api/category/{id}
- 获取所有分类：GET /api/category/list

### 4.4 用户相关

- 用户登录：POST /api/sysUser/login
- 获取用户信息：GET /api/sysUser/info
- 修改密码：PUT /api/sysUser/password

### 4.5 系统相关

- 获取系统配置：GET /api/system/config
- 更新系统配置：PUT /api/system/config

### 4.6 AI相关

- AI菜品推荐：GET /api/ai/recommend
- AI菜单生成：POST /api/ai/menu

## 5. 配置说明

- 服务器端口：8082
- 数据库连接：localhost:3306/huaiwei_yunshan
- 数据库用户名：root
- 数据库密码：123456
- 文件上传路径：uploads/
- 最大文件大小：10MB

## 6. 运行说明

1. 启动MySQL数据库
2. 执行sql/create_tables.sql创建表结构
3. 执行sql/insert_data.sql插入基础数据
4. 运行start.bat启动后端服务
5. 访问 http://localhost:8082/doc.html 查看API文档

## 7. 图片上传说明

1. 上传路径：POST /api/dish/upload
2. 上传字段：file（multipart/form-data）
3. 图片存储：uploads/dishes/目录
4. 图片访问：http://localhost:8082/uploads/{filename}
5. 支持格式：JPG、PNG、JPEG、GIF
6. 最大大小：10MB
