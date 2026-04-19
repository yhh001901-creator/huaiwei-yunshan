# 淮味云膳餐馆服务系统 - API文档

## 1. 接口基础信息

- **API基础路径**：`http://localhost:8082/api`

- **请求方法**：GET、POST、PUT、DELETE

- **响应格式**：JSON

- **统一响应结构**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {}
  }
  ```

## 2. 菜品相关接口

### 2.1 新增菜品

- **路径**：`/dish`

- **方法**：POST

- **请求参数**：
  
  | 参数名           | 类型         | 必选  | 描述    |
  |:------------- |:---------- |:--- |:----- |
  | `name`        | String     | 是   | 菜品名称  |
  | `price`       | BigDecimal | 是   | 菜品价格  |
  | `categoryId`  | Long       | 是   | 分类ID  |
  | `description` | String     | 否   | 菜品描述  |
  | `imageUrl`    | String     | 否   | 图片URL |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "name": "红烧肉",
      "price": 68.00,
      "categoryId": 1,
      "description": "传统红烧肉，肥而不腻",
      "imageUrl": "/uploads/dish_123456.jpg"
    }
  }
  ```

### 2.2 修改菜品

- **路径**：`/dish`

- **方法**：PUT

- **请求参数**：
  
  | 参数名           | 类型         | 必选  | 描述    |
  |:------------- |:---------- |:--- |:----- |
  | `id`          | Long       | 是   | 菜品ID  |
  | `name`        | String     | 是   | 菜品名称  |
  | `price`       | BigDecimal | 是   | 菜品价格  |
  | `categoryId`  | Long       | 是   | 分类ID  |
  | `description` | String     | 否   | 菜品描述  |
  | `imageUrl`    | String     | 否   | 图片URL |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "name": "红烧肉",
      "price": 78.00,
      "categoryId": 1,
      "description": "传统红烧肉，肥而不腻",
      "imageUrl": "/uploads/dish_123456.jpg"
    }
  }
  ```

### 2.3 删除菜品

- **路径**：`/dish/{id}`

- **方法**：DELETE

- **路径参数**：
  
  | 参数名  | 类型   | 必选  | 描述   |
  |:---- |:---- |:--- |:---- |
  | `id` | Long | 是   | 菜品ID |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": null
  }
  ```

### 2.4 分页查询菜品

- **路径**：`/dish/page`

- **方法**：GET

- **查询参数**：
  
  | 参数名    | 类型      | 必选  | 描述   |
  |:------ |:------- |:--- |:---- |
  | `page` | Integer | 是   | 页码   |
  | `size` | Integer | 是   | 每页数量 |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "total": 80,
      "records": [
        {
          "id": 1,
          "name": "红烧肉",
          "price": 68.00,
          "categoryId": 1,
          "description": "传统红烧肉，肥而不腻",
          "imageUrl": "/uploads/dish_123456.jpg"
        },
        // 更多菜品...
      ]
    }
  }
  ```

### 2.5 根据分类查询菜品

- **路径**：`/dish/category/{categoryId}`

- **方法**：GET

- **路径参数**：
  
  | 参数名          | 类型   | 必选  | 描述   |
  |:------------ |:---- |:--- |:---- |
  | `categoryId` | Long | 是   | 分类ID |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": [
      {
        "id": 1,
        "name": "红烧肉",
        "price": 68.00,
        "categoryId": 1,
        "description": "传统红烧肉，肥而不腻",
        "imageUrl": "/uploads/dish_123456.jpg"
      },
      // 更多同分类菜品...
    ]
  }
  ```

### 2.6 获取所有菜品

- **路径**：`/dish/list`

- **方法**：GET

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": [
      {
        "id": 1,
        "name": "红烧肉",
        "price": 68.00,
        "categoryId": 1,
        "description": "传统红烧肉，肥而不腻",
        "imageUrl": "/uploads/dish_123456.jpg"
      },
      // 所有菜品...
    ]
  }
  ```

### 2.7 上传菜品图片

- **路径**：`/dish/upload`

- **方法**：POST

- **请求参数**：
  
  | 参数名    | 类型            | 必选  | 描述   |
  |:------ |:------------- |:--- |:---- |
  | `file` | MultipartFile | 是   | 图片文件 |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": "/uploads/dish_123456.jpg"
  }
  ```

## 3. 订单相关接口

### 3.1 提交订单

- **路径**：`/orders`

- **方法**：POST

- **请求参数**：
  
  | 参数名            | 类型         | 必选  | 描述                      |
  |:-------------- |:---------- |:--- |:----------------------- |
  | `orderNo`      | String     | 是   | 订单号                     |
  | `tableNo`      | String     | 是   | 桌号                      |
  | `totalAmount`  | BigDecimal | 是   | 总金额                     |
  | `status`       | Integer    | 是   | 订单状态（0：待付款，1：已付款，2：已完成） |
  | `orderDetails` | Array      | 是   | 订单明细                    |

- **订单明细结构**：
  
  | 参数名        | 类型         | 必选  | 描述   |
  |:---------- |:---------- |:--- |:---- |
  | `dishId`   | Long       | 是   | 菜品ID |
  | `dishName` | String     | 是   | 菜品名称 |
  | `quantity` | Integer    | 是   | 数量   |
  | `price`    | BigDecimal | 是   | 单价   |
  | `subtotal` | BigDecimal | 是   | 小计   |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "orderNo": "ORD20260415001",
      "tableNo": "A01",
      "totalAmount": 156.00,
      "status": 0,
      "createTime": "2026-04-15 12:00:00",
      "orderDetails": [
        {
          "id": 1,
          "orderId": 1,
          "dishId": 1,
          "dishName": "红烧肉",
          "quantity": 1,
          "price": 68.00,
          "subtotal": 68.00
        },
        {
          "id": 2,
          "orderId": 1,
          "dishId": 2,
          "dishName": "清蒸鲈鱼",
          "quantity": 1,
          "price": 88.00,
          "subtotal": 88.00
        }
      ]
    }
  }
  ```

### 3.2 修改订单状态

- **路径**：`/orders/status`

- **方法**：PUT

- **查询参数**：
  
  | 参数名       | 类型      | 必选  | 描述                      |
  |:--------- |:------- |:--- |:----------------------- |
  | `orderNo` | String  | 是   | 订单号                     |
  | `status`  | Integer | 是   | 订单状态（0：待付款，1：已付款，2：已完成） |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": null
  }
  ```

### 3.3 分页查询订单

- **路径**：`/orders/page`

- **方法**：GET

- **查询参数**：
  
  | 参数名    | 类型      | 必选  | 描述   |
  |:------ |:------- |:--- |:---- |
  | `page` | Integer | 是   | 页码   |
  | `size` | Integer | 是   | 每页数量 |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "total": 100,
      "records": [
        {
          "id": 1,
          "orderNo": "ORD20260415001",
          "tableNo": "A01",
          "totalAmount": 156.00,
          "status": 1,
          "createTime": "2026-04-15 12:00:00"
        },
        // 更多订单...
      ]
    }
  }
  ```

### 3.4 获取所有订单

- **路径**：`/orders/list`

- **方法**：GET

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": [
      {
        "id": 1,
        "orderNo": "ORD20260415001",
        "tableNo": "A01",
        "totalAmount": 156.00,
        "status": 1,
        "createTime": "2026-04-15 12:00:00"
      },
      // 所有订单...
    ]
  }
  ```

## 4. 分类相关接口

### 4.1 新增分类

- **路径**：`/category`

- **方法**：POST

- **请求参数**：
  
  | 参数名           | 类型     | 必选  | 描述   |
  |:------------- |:------ |:--- |:---- |
  | `name`        | String | 是   | 分类名称 |
  | `description` | String | 否   | 分类描述 |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "name": "精品淮扬菜",
      "description": "传统淮扬菜系"
    }
  }
  ```

### 4.2 修改分类

- **路径**：`/category`

- **方法**：PUT

- **请求参数**：
  
  | 参数名           | 类型     | 必选  | 描述   |
  |:------------- |:------ |:--- |:---- |
  | `id`          | Long   | 是   | 分类ID |
  | `name`        | String | 是   | 分类名称 |
  | `description` | String | 否   | 分类描述 |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "name": "精品淮扬菜",
      "description": "传统淮扬菜系，口味清淡"
    }
  }
  ```

### 4.3 删除分类

- **路径**：`/category/{id}`

- **方法**：DELETE

- **路径参数**：
  
  | 参数名  | 类型   | 必选  | 描述   |
  |:---- |:---- |:--- |:---- |
  | `id` | Long | 是   | 分类ID |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": null
  }
  ```

### 4.4 获取所有分类

- **路径**：`/category/list`

- **方法**：GET

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": [
      {
        "id": 1,
        "name": "精品淮扬菜",
        "description": "传统淮扬菜系"
      },
      {
        "id": 2,
        "name": "主食",
        "description": "各类主食"
      },
      {
        "id": 3,
        "name": "饮品",
        "description": "各类饮品"
      },
      {
        "id": 4,
        "name": "甜点",
        "description": "各类甜点"
      }
    ]
  }
  ```

## 5. 用户相关接口

### 5.1 用户登录

- **路径**：`/sysUser/login`

- **方法**：POST

- **请求参数**：
  
  | 参数名        | 类型     | 必选  | 描述  |
  |:---------- |:------ |:--- |:--- |
  | `username` | String | 是   | 用户名 |
  | `password` | String | 是   | 密码  |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "username": "admin",
      "name": "管理员",
      "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
  }
  ```

### 5.2 获取用户信息

- **路径**：`/sysUser/info`

- **方法**：GET

- **请求头**：
  
  | 参数名             | 类型     | 必选  | 描述           |
  |:--------------- |:------ |:--- |:------------ |
  | `Authorization` | String | 是   | Bearer token |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "username": "admin",
      "name": "管理员"
    }
  }
  ```

### 5.3 修改密码

- **路径**：`/sysUser/password`

- **方法**：PUT

- **请求参数**：
  
  | 参数名           | 类型     | 必选  | 描述  |
  |:------------- |:------ |:--- |:--- |
  | `oldPassword` | String | 是   | 旧密码 |
  | `newPassword` | String | 是   | 新密码 |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": null
  }
  ```

## 6. 系统相关接口

### 6.1 获取系统配置

- **路径**：`/system/config`

- **方法**：GET

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "restaurantName": "淮味云膳",
      "phone": "13800138000",
      "address": "江苏省南京市秦淮区",
      "businessHours": "10:00-22:00"
    }
  }
  ```

### 6.2 更新系统配置

- **路径**：`/system/config`

- **方法**：PUT

- **请求参数**：
  
  | 参数名              | 类型     | 必选  | 描述   |
  |:---------------- |:------ |:--- |:---- |
  | `restaurantName` | String | 是   | 餐厅名称 |
  | `phone`          | String | 是   | 联系电话 |
  | `address`        | String | 是   | 地址   |
  | `businessHours`  | String | 是   | 营业时间 |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "id": 1,
      "restaurantName": "淮味云膳",
      "phone": "13800138000",
      "address": "江苏省南京市秦淮区",
      "businessHours": "10:00-22:00"
    }
  }
  ```

## 7. AI相关接口

### 7.1 AI菜品推荐

- **路径**：`/ai/recommend`

- **方法**：GET

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": [
      {
        "id": 1,
        "name": "红烧肉",
        "price": 68.00,
        "categoryId": 1,
        "description": "传统红烧肉，肥而不腻",
        "imageUrl": "/uploads/dish_123456.jpg"
      },
      {
        "id": 2,
        "name": "清蒸鲈鱼",
        "price": 88.00,
        "categoryId": 1,
        "description": "新鲜鲈鱼，清蒸保留原味",
        "imageUrl": "/uploads/dish_123457.jpg"
      },
      // 更多推荐菜品...
    ]
  }
  ```

### 7.2 AI菜单生成

- **路径**：`/ai/menu`

- **方法**：POST

- **请求参数**：
  
  | 参数名           | 类型         | 必选  | 描述   |
  |:------------- |:---------- |:--- |:---- |
  | `theme`       | String     | 是   | 菜单主题 |
  | `personCount` | Integer    | 是   | 人数   |
  | `budget`      | BigDecimal | 是   | 预算   |

- **响应示例**：
  
  ```json
  {
    "code": 200,
    "msg": "成功",
    "data": {
      "theme": "家庭聚餐",
      "personCount": 4,
      "budget": 300.00,
      "dishes": [
        {
          "id": 1,
          "name": "红烧肉",
          "price": 68.00,
          "categoryId": 1,
          "description": "传统红烧肉，肥而不腻",
          "imageUrl": "/uploads/dish_123456.jpg"
        },
        {
          "id": 2,
          "name": "清蒸鲈鱼",
          "price": 88.00,
          "categoryId": 1,
          "description": "新鲜鲈鱼，清蒸保留原味",
          "imageUrl": "/uploads/dish_123457.jpg"
        },
        {
          "id": 3,
          "name": "扬州炒饭",
          "price": 28.00,
          "categoryId": 2,
          "description": "经典扬州炒饭，粒粒分明",
          "imageUrl": "/uploads/dish_123458.jpg"
        },
        {
          "id": 4,
          "name": "可乐",
          "price": 10.00,
          "categoryId": 3,
          "description": "冰镇可乐",
          "imageUrl": "/uploads/dish_123459.jpg"
        }
      ],
      "totalPrice": 194.00
    }
  }
  ```

## 8. 错误码说明

| 错误码 | 描述      |
|:--- |:------- |
| 200 | 成功      |
| 400 | 请求参数错误  |
| 401 | 未授权     |
| 404 | 资源不存在   |
| 500 | 服务器内部错误 |

## 9. 使用示例

### 9.1 前端调用示例（Axios）

```javascript
// 导入axios
import axios from 'axios';

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
});

// 请求拦截器
request.interceptors.request.use(config => {
  // 添加token
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截器
request.interceptors.response.use(response => {
  return response.data;
}, error => {
  console.error('请求错误:', error);
  return Promise.reject(error);
});

// 调用示例
// 获取所有菜品
const getDishes = async () => {
  try {
    const data = await request({
      method: 'GET',
      url: '/dish/list'
    });
    console.log('菜品列表:', data);
  } catch (error) {
    console.error('获取菜品失败:', error);
  }
};

// 上传图片
const uploadImage = async (file) => {
  const formData = new FormData();
  formData.append('file', file);

  try {
    const imageUrl = await request({
      method: 'POST',
      url: '/dish/upload',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    console.log('图片上传成功:', imageUrl);
    return imageUrl;
  } catch (error) {
    console.error('上传图片失败:', error);
  }
};
```

### 9.2 后端调用示例（Postman）

1. **获取所有菜品**
   
   - 方法：GET
   - URL：`http://localhost:8082/api/dish/list`
   - 响应：200 OK

2. **上传图片**
   
   - 方法：POST
   - URL：`http://localhost:8082/api/dish/upload`
   - 类型：form-data
   - 参数：file（选择文件）
   - 响应：200 OK

3. **提交订单**
   
   - 方法：POST
   
   - URL：`http://localhost:8082/api/orders`
   
   - 类型：JSON
   
   - 请求体：
     
     ```json
     {
       "orderNo": "ORD20260415001",
       "tableNo": "A01",
       "totalAmount": 156.00,
       "status": 0,
       "orderDetails": [
         {
           "dishId": 1,
           "dishName": "红烧肉",
           "quantity": 1,
           "price": 68.00,
           "subtotal": 68.00
         },
         {
           "dishId": 2,
           "dishName": "清蒸鲈鱼",
           "quantity": 1,
           "price": 88.00,
           "subtotal": 88.00
         }
       ]
     }
     ```
   
   - 响应：200 OK