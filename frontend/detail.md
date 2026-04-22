# 淮味云膳餐馆服务系统 - 前端说明

## 1. 项目结构

```
frontend/
├── android/            # Android应用目录
├── src/
│   ├── assets/          # 静态资源
│   │   └── main.css     # Tailwind CSS基础样式
│   ├── components/      # 组件
│   ├── views/           # 页面视图
│   │   ├── HomeView.vue    # 首页
│   │   ├── PosMain.vue     # POS系统页面
│   │   ├── OrdersView.vue  # 订单管理页面
│   │   └── SettingsView.vue # 设置页面
│   ├── router/          # 路由配置
│   │   └── index.js     # 路由定义
│   ├── utils/           # 工具函数
│   │   └── request.js   # axios请求封装
│   ├── api/             # API调用
│   │   ├── ai.js        # AI相关API
│   │   ├── dish.js      # 菜品相关API
│   │   └── order.js     # 订单相关API
│   ├── App.vue          # 根组件
│   └── main.js          # 应用入口
├── index.html           # HTML入口
├── package.json         # 项目依赖
├── package-lock.json    # 依赖锁定文件
├── vite.config.js       # Vite配置（含API代理）
├── tailwind.config.js   # Tailwind CSS配置
├── postcss.config.js    # PostCSS配置
├── start.bat            # 启动脚本
├── version.md           # 版本信息
└── detail.md            # 项目说明
```

## 2. 技术栈

- Vue 3
- Vue Router 4
- Tailwind CSS 3
- Vite 5
- Axios
- Element Plus
- Capacitor (Android打包)

## 3. 核心功能

### 3.1 首页

- 系统功能介绍
- 快速导航到POS系统
- 系统状态概览

### 3.2 POS系统

- 菜品展示与分类
- 菜品搜索
- 点餐功能
- 购物车管理
- 订单结算
- 数量加减调整
- 实时价格计算

### 3.3 订单管理

- 订单列表展示
- 订单状态管理
- 订单详情查看
- 订单搜索与筛选

### 3.4 设置页面

- 系统配置管理
- 用户信息管理
- 系统状态监控

## 4. 页面路由

- 首页：/ 
- POS系统：/pos
- 订单管理：/orders
- 设置：/settings

## 5. API代理配置

- 代理路径：/api
- 目标地址：http://localhost:8082
- 端口：5173

## 6. API调用

### 6.1 菜品相关

- 获取所有菜品：GET /api/dish/list
- 根据分类获取菜品：GET /api/dish/category/{categoryId}

### 6.2 订单相关

- 创建订单：POST /api/orders
- 获取订单列表：GET /api/orders/list
- 更新订单状态：PUT /api/orders/status

### 6.3 AI相关

- AI菜品推荐：GET /api/ai/recommend
- AI菜单生成：POST /api/ai/menu

## 7. 运行说明

1. 进入frontend目录：`cd frontend`
2. 安装依赖：`npm install`
3. 启动开发服务器：`npm run dev` 或运行start.bat
4. 访问应用：http://localhost:5173
5. 构建生产版本：`npm run build`

## 8. Android应用说明

1. 构建Web应用：`npm run build`
2. 同步到Android：`npx cap sync android`
3. 打开Android Studio：`npx cap open android`
4. 构建APK：在Android Studio中构建release版本
5. 安装APK：将生成的APK文件安装到Android设备
