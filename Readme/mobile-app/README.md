# 淮味云膳 - 移动端应用

## 项目说明

本项目是淮味云膳餐厅服务系统的移动端应用，基于 uni-app 框架开发，支持编译到微信小程序、H5、App 等多个平台。

## 项目结构

```
mobile-app/
├── pages.json                            # 页面配置
├── manifest.json                         # 应用配置
├── App.vue                               # 根组件
├── main.js                               # 入口文件
├── src/
│   ├── pages/                            # 页面
│   │   ├── index/                        # 首页
│   │   │   └── index.vue                 # 首页组件
│   │   ├── login/                        # 登录页
│   │   │   └── login.vue                 # 登录组件
│   │   ├── member/                       # 会员页
│   │   │   └── member.vue                # 会员中心
│   │   ├── mine/                         # 我的页面
│   │   │   └── mine.vue                  # 个人中心
│   │   └── orders/                       # 订单页
│   │       └── orders.vue                # 订单列表
│   ├── static/                           # 静态资源
│   │   ├── placeholder.svg               # 占位图
│   │   ├── tab-index.svg                 # 首页图标（未激活）
│   │   ├── tab-index-active.svg          # 首页图标（激活）
│   │   ├── tab-mine.svg                  # 我的图标（未激活）
│   │   ├── tab-mine-active.svg           # 我的图标（激活）
│   │   ├── tab-order.svg                 # 订单图标（未激活）
│   │   └── tab-order-active.svg          # 订单图标（激活）
│   ├── stores/                           # 状态管理（Pinia）
│   │   ├── cart.js                       # 购物车状态
│   │   └── user.js                       # 用户状态
│   └── utils/                            # 工具类
│       └── request.js                    # HTTP 请求封装
├── target/                               # 构建输出目录
├── 参考代码/                              # 参考代码
│   └── PosMain.html                      # POS 端参考代码
├── 资料/                                 # 资料
│   └── 菜品清单.md                        # 菜品清单文档
└── README.md                             # 项目说明
```

## 技术栈

- **框架**: uni-app
- **基础框架**: Vue 3
- **状态管理**: Pinia 2.1+
- **UI 框架**: uView UI / uni-ui
- **HTTP 客户端**: uni.request 封装

## 核心功能模块

### 1. 首页 (index)

- 轮播图展示
- 推荐菜品
- 活动信息
- 快速导航

### 2. 登录页 (login)

- 微信授权登录
- 手机号登录
- 账号密码登录
- 注册入口

### 3. 会员中心 (member)

- 会员信息展示
- 会员等级
- 积分查询
- 优惠券列表
- 会员权益

### 4. 个人中心 (mine)

- 个人信息管理
- 订单入口
- 地址管理
- 客服联系
- 设置入口

### 5. 订单页 (orders)

- 订单列表
- 订单状态筛选
- 订单详情
- 订单操作（支付、评价、取消等）

## 开发指南

### 环境要求

- HBuilderX 3.0+
- Node.js 16+
- 微信开发者工具（小程序开发）

### 安装依赖

```bash
npm install
```

### 运行到微信小程序

1. 在 HBuilderX 中打开项目
2. 点击菜单栏 运行 -> 运行到小程序模拟器 -> 微信开发者工具
3. 在微信开发者工具中导入项目
4. 配置 AppID

### 运行到 H5

```bash
npm run dev:h5
```

### 构建小程序

```bash
npm run build:mp-weixin
```

### 构建 H5

```bash
npm run build:h5
```

## 目录说明

### pages/

存放所有页面文件，每个页面一个目录：

- `index/` - 首页
- `login/` - 登录页
- `member/` - 会员中心
- `mine/` - 个人中心
- `orders/` - 订单页

### static/

存放静态资源文件：

- 图片资源
- SVG 图标
- 字体文件

### stores/

使用 Pinia 进行状态管理：

- `cart.js` - 购物车状态管理
  - 添加商品
  - 移除商品
  - 更新数量
  - 清空购物车
  
- `user.js` - 用户状态管理
  - 用户信息
  - 登录状态
  - Token 管理

### utils/

工具函数：

- `request.js` - uni.request 封装
  - 统一请求头
  - Token 自动添加
  - 响应拦截
  - 错误处理

## 配置说明

### pages.json

配置页面路由、导航栏、 tabBar 等：

```json
{
  "pages": [
    {
      "path": "pages/index/index",
      "style": {
        "navigationBarTitleText": "淮味云膳"
      }
    }
  ],
  "tabBar": {
    "color": "#7A7E83",
    "selectedColor": "#007AFF",
    "list": [
      {
        "pagePath": "pages/index/index",
        "text": "首页",
        "iconPath": "static/tab-index.svg",
        "selectedIconPath": "static/tab-index-active.svg"
      },
      {
        "pagePath": "pages/orders/orders",
        "text": "订单",
        "iconPath": "static/tab-order.svg",
        "selectedIconPath": "static/tab-order-active.svg"
      },
      {
        "pagePath": "pages/mine/mine",
        "text": "我的",
        "iconPath": "static/tab-mine.svg",
        "selectedIconPath": "static/tab-mine-active.svg"
      }
    ]
  }
}
```

### manifest.json

配置应用信息、权限、各平台参数等：

```json
{
  "name": "淮味云膳",
  "appid": "__UNI__xxxxxxx",
  "description": "餐厅服务系统",
  "versionName": "1.0.0",
  "versionCode": "100",
  "transformPx": false,
  "app-plus": {
    // App 平台配置
  },
  "mp-weixin": {
    // 微信小程序配置
    "appid": "wx_xxxxxxxxxxxxxxxx",
    "setting": {
      "urlCheck": false
    }
  },
  "h5": {
    // H5 平台配置
  }
}
```

## API 配置

在 `src/utils/request.js` 中配置：

```javascript
const BASE_URL = 'http://localhost:8082/api'

export const request = (options) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': uni.getStorageSync('token') || ''
      },
      success: (res) => {
        resolve(res.data)
      },
      fail: (err) => {
        uni.showToast({
          title: '请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}
```

## 认证机制

### Token 管理

- 登录后将 Token 存储在 uni.getStorageSync
- 请求时自动在 Header 中添加 Token
- Token 过期自动跳转登录页

### 登录流程

1. 用户点击登录
2. 调用登录接口获取 Token
3. 存储 Token 到本地
4. 跳转到首页

## 样式规范

### 使用 rpx 单位

推荐使用 rpx 进行响应式布局：

```css
.container {
  padding: 20rpx;
  font-size: 28rpx;
}
```

### 使用 Flex 布局

```css
.flex-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
```

## 多端适配

### 平台判断

```javascript
// #ifdef MP-WEIXIN
console.log('微信小程序环境')
// #endif

// #ifdef H5
console.log('H5 环境')
// #endif

// #ifdef APP-PLUS
console.log('App 环境')
// #endif
```

### 条件编译

在模板中使用条件编译：

```vue
<template>
  <view>
    <!-- #ifdef MP-WEIXIN -->
    <button wx:if="{{true}}">微信小程序按钮</button>
    <!-- #endif -->
    
    <!-- #ifdef H5 -->
    <button v-if="true">H5 按钮</button>
    <!-- #endif -->
  </view>
</template>
```

## 性能优化

### 图片优化

- 使用 WebP 格式
- 图片懒加载
- 使用 CDN

### 分包加载

```json
{
  "subPackages": [
    {
      "root": "pages/member",
      "pages": [
        {
          "path": "member"
        }
      ]
    }
  ]
}
```

### 数据缓存

```javascript
// 缓存数据
uni.setStorageSync('key', data)

// 读取缓存
const data = uni.getStorageSync('key')
```

## 常见问题

### 1. 小程序开发工具报错

- 检查 AppID 配置是否正确
- 检查域名白名单
- 关闭校验合法域名（开发阶段）

### 2. 跨域问题（H5）

在 manifest.json 中配置 devServer 代理：

```json
"h5": {
  "devServer": {
    "proxy": {
      "/api": {
        "target": "http://localhost:8082",
        "changeOrigin": true
      }
    }
  }
}
```

### 3. 真机调试

- 使用 HBuilderX 运行到手机
- 使用微信开发者工具的真机调试功能
- 配置网络权限（App）

## 发布流程

### 微信小程序发布

1. 在 HBuilderX 中构建小程序
2. 在微信开发者工具中上传代码
3. 登录微信公众平台提交审核
4. 审核通过后发布

### H5 发布

1. 构建 H5 版本
2. 上传到服务器
3. 配置 Nginx

### App 发布

1. 打包生成安装包
2. 提交应用市场审核
3. 发布上线

## 开发规范

### 命名规范

- 文件名：小写 + 连字符（如 `dish-list.vue`）
- 组件名：PascalCase
- 变量名：camelCase
- 常量名：UPPER_SNAKE_CASE

### 代码规范

- 使用 ESLint 进行代码检查
- 使用 Prettier 进行代码格式化
- 遵循 Vue 3 组合式 API 最佳实践
- 遵循 uni-app 开发规范

### Git 规范

- feat: 新功能
- fix: 修复 bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 代码重构
- test: 测试相关
- chore: 构建/工具链相关

## 调试技巧

### 小程序调试

- 使用微信开发者工具的调试器
- 使用 console.log 输出日志
- 使用 Network 面板查看网络请求

### H5 调试

- 使用浏览器开发者工具
- 使用 vConsole 插件

### App 调试

- 使用 HBuilderX 的调试功能
- 使用真机调试

## 更新日志

### v6.0.0

- 迁移到 Vue 3
- 使用 Pinia 进行状态管理
- 优化用户体验
- 支持多平台

## 相关文档

- [uni-app 文档](https://uniapp.dcloud.net.cn/)
- [Vue 3 文档](https://cn.vuejs.org/)
- [Pinia 文档](https://pinia.vuejs.org/zh/)
- [微信小程序文档](https://developers.weixin.qq.com/miniprogram/dev/framework/)
- [后端 API 文档](http://localhost:8082/doc.html)

## 资料

- [菜品清单](资料/菜品清单.md) - 完整的菜品清单文档
