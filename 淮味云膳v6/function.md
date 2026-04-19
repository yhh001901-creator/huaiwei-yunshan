# 前后端图片传输实现方法

## 1. 后端实现

### 1.1 配置文件设置

在 `backend/src/main/resources/application.properties` 中添加文件上传配置：

```properties
# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
# 图片存储路径
upload.path=./uploads/dishes/
```

### 1.2 静态资源配置

在 `backend/src/main/java/com/restaurant/config/WebMvcConfig.java` 中添加静态资源访问配置：

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }

    // 其他配置...
}
```

### 1.3 图片上传接口

在 `backend/src/main/java/com/restaurant/controller/DishController.java` 中添加图片上传接口：

```java
@RestController
@RequestMapping("/api/dish")
public class DishController {

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 上传菜品图片
     * @param file 图片文件
     * @return 图片访问URL
     */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        // 确保上传目录存在
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 生成唯一文件名
        String fileName = "dish_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(uploadPath + fileName);

        try {
            file.transferTo(dest);
            // 返回图片访问URL
            String imageUrl = "/uploads/" + fileName;
            return Result.success(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    // 其他接口...
}
```

### 1.4 菜品更新接口

在 `DishController` 中确保更新菜品时正确处理图片URL：

```java
@PutMapping
public Result<?> update(@RequestBody Dish dish) {
    // 如果imageUrl为空，则不更新imageUrl字段
    if (dish.getImageUrl() == null) {
        // 获取原有菜品信息
        Dish existingDish = dishService.getById(dish.getId());
        if (existingDish != null) {
            // 保持原有imageUrl
            dish.setImageUrl(existingDish.getImageUrl());
        }
    }
    boolean updated = dishService.updateById(dish);
    if (updated) {
        return Result.success(dish);
    } else {
        return Result.error("修改菜品失败");
    }
}
```

## 2. 前端实现

### 2.1 图片选择和上传

在 `frontend/src/views/SettingsView.vue` 中添加图片选择和上传功能：

```vue
<!-- 添加菜品模态框 -->
<div v-if="showAddDishModal">
  <div class="space-y-4">
    <!-- 其他字段... -->
    <div>
      <label class="block text-sm font-medium text-on-surface-variant mb-2">菜品图片</label>
      <input type="file" accept="image/*" @change="handleImageUpload($event, 'new')" />
      <div v-if="newDish.imageUrl" class="mt-2">
        <img :src="newDish.imageUrl" class="w-20 h-20 object-cover rounded-lg" />
      </div>
    </div>
  </div>
</div>

<!-- 编辑菜品模态框 -->
<div v-if="showEditDishModal">
  <div class="space-y-4">
    <!-- 其他字段... -->
    <div>
      <label class="block text-sm font-medium text-on-surface-variant mb-2">菜品图片</label>
      <input type="file" accept="image/*" @change="handleImageUpload($event, 'edit')" />
      <div v-if="editDish.imageUrl" class="mt-2">
        <img :src="editDish.imageUrl" class="w-20 h-20 object-cover rounded-lg" />
      </div>
    </div>
  </div>
</div>
```

### 2.2 图片上传方法

在 `SettingsView.vue` 中添加图片上传方法：

```javascript
// 处理图片上传
const handleImageUpload = async (event, type) => {
  const file = event.target.files[0]
  if (file) {
    const formData = new FormData()
    formData.append('file', file)

    try {
      console.log('上传图片:', file.name)
      // 使用request实例发送请求
      const imageUrl = await request({
        method: 'POST',
        url: '/dish/upload',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })

      console.log('上传图片成功，返回URL:', imageUrl)
      if (type === 'new') {
        newDish.value.imageUrl = imageUrl
        console.log('设置新菜品图片URL:', newDish.value.imageUrl)
      } else {
        editDish.value.imageUrl = imageUrl
        console.log('设置编辑菜品图片URL:', editDish.value.imageUrl)
      }
    } catch (error) {
      console.error('上传图片失败:', error)
    }
  }
}
```

### 2.3 保存菜品方法

在 `SettingsView.vue` 中添加保存菜品方法：

```javascript
// 添加菜品
const addDish = async () => {
  try {
    console.log('添加菜品:', newDish.value)
    const result = await request({
      method: 'POST',
      url: '/dish',
      data: newDish.value
    })
    console.log('添加菜品成功:', result)
    showAddDishModal.value = false
    fetchDishes()
  } catch (error) {
    console.error('添加菜品失败:', error)
  }
}

// 更新菜品
const updateDish = async () => {
  try {
    console.log('更新菜品:', editDish.value)
    const result = await request({
      method: 'PUT',
      url: '/dish',
      data: editDish.value
    })
    console.log('更新菜品成功:', result)
    showEditDishModal.value = false
    fetchDishes()
  } catch (error) {
    console.error('更新菜品失败:', error)
  }
}
```

### 2.4 图片显示

在 `frontend/src/views/PosMain.vue` 中显示菜品图片：

```vue
<img 
  :alt="dish.name" 
  class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
  :src="dish.imageUrl || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20food%20placeholder&size=512x512'"
/>
```

## 3. 工作流程

1. **图片选择**：用户在前端选择本地图片文件
2. **图片上传**：前端通过 `handleImageUpload` 方法将图片上传到后端
3. **后端处理**：后端接收图片，保存到 `uploads/dishes` 目录，生成唯一文件名
4. **返回URL**：后端返回图片的访问URL，例如 `/uploads/dish_123456.jpg`
5. **前端设置**：前端将返回的URL设置到菜品对象的 `imageUrl` 字段
6. **保存菜品**：前端通过 `addDish` 或 `updateDish` 方法将菜品信息（包含图片URL）保存到数据库
7. **显示图片**：前端通过菜品的 `imageUrl` 字段显示图片

## 4. 技术要点

1. **文件上传**：使用 `FormData` 传递图片文件
2. **静态资源访问**：后端配置 `addResourceHandlers` 允许访问上传的图片
3. **唯一文件名**：使用时间戳和原始文件名生成唯一文件名，避免冲突
4. **错误处理**：添加详细的错误处理和日志输出
5. **响应拦截器**：前端使用 `request` 实例的响应拦截器处理后端返回的数据

## 5. 注意事项

1. **图片大小**：后端配置了最大文件大小为10MB，确保图片大小不超过此限制
2. **目录权限**：确保后端服务有足够的权限创建和写入 `uploads/dishes` 目录
3. **数据库字段**：确保数据库的 `image_url` 字段长度足够存储图片URL
4. **跨域问题**：前端通过Vite代理解决跨域问题
5. **图片显示**：添加默认图片，当菜品没有图片时显示占位图

## 6. 测试步骤

1. **启动后端服务**：运行 `java -jar target/huaiwei-yunshan-1.0.0.jar`
2. **启动前端服务**：运行 `npm run dev`
3. **访问前端应用**：打开浏览器访问 `http://localhost:5174`
4. **进入菜品管理**：点击"设置" -> "菜品管理"
5. **编辑菜品**：选择一个菜品，点击"编辑"
6. **选择图片**：选择一张本地图片文件
7. **保存菜品**：点击"保存"按钮
8. **验证结果**：再次编辑该菜品，查看图片是否正确显示；检查数据库中的 `image_url` 字段是否已更新
9. **查看图片**：检查后端的 `uploads/dishes` 目录是否已保存图片文件

## 7. 实际使用情况

### 7.1 图片存储结构

后端 `uploads` 目录结构如下：

```
uploads/
├── dish image/                 # 菜品图片（分类存储）
│   ├── 主食/
│   ├── 淮扬菜/
│   ├── 甜品/
│   └── 饮料/
└── dishes/                     # 上传的菜品图片（按时间戳命名）
```

### 7.2 已上传的图片

目前已上传的菜品图片包括：

- 精品淮扬菜：红烧肉、清蒸鲈鱼、宫保鸡丁等20款
- 主食：扬州炒饭、红烧牛肉面、炸酱面等20款
- 饮品：可乐、雪碧、芬达等20款
- 甜点：提拉米苏、巧克力蛋糕、草莓蛋糕等20款

### 7.3 图片访问示例

- 红烧肉图片：http://localhost:8082/uploads/dish_1774257931913_红烧肉.png
- 扬州炒饭图片：http://localhost:8082/uploads/dish_1774262118740_扬州炒饭.png
- 可乐图片：http://localhost:8082/uploads/dish_1774262909258_可乐.png
- 提拉米苏图片：http://localhost:8082/uploads/dish_1774263208681_提拉米苏.png
