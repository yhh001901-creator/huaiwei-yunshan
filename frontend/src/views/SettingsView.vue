<template>
  <div class="flex h-screen overflow-hidden text-on-background">
    <aside class="h-full w-[15%] fixed left-0 top-0 bg-slate-100 dark:bg-slate-800 flex flex-col py-8 z-50">
      <div class="px-6 mb-10">
        <h1 class="text-xl font-bold text-teal-900 dark:text-teal-100 mb-1">淮味云膳</h1>
        <p class="text-xs text-on-surface-variant font-medium opacity-70">管理员端</p>
      </div>
      <nav class="flex-1 space-y-2">
        <router-link to="/admin" class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200" >
          <span class="w-6 h-6 flex items-center justify-center"></span>
          <span>数据概览</span>
        </router-link>
        <router-link to="/orders" class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200" :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/orders' }" >
          <span class="w-6 h-6 flex items-center justify-center"></span>
          <span>订单</span>
        </router-link>
        <router-link to="/settings" class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200" :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/settings' }" >
          <span class="w-6 h-6 flex items-center justify-center">⚙️</span>
          <span>设置</span>
        </router-link>
      </nav>
      <div class="px-6 mt-auto">
        <div class="flex items-center gap-3 py-3 px-4 bg-surface-container rounded-xl">
          <div class="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
            <span class="text-primary font-semibold">{{ userStore.username?.charAt(0) || '管' }}</span>
          </div>
          <div class="flex-1">
            <p class="text-sm font-medium">{{ userStore.username }}</p>
            <p class="text-xs text-on-surface-variant">{{ userStore.roleName }}</p>
          </div>
          <button @click="handleLogout" class="text-red-500 hover:text-red-600 text-sm">退出</button>
        </div>
      </div>
    </aside>
    <!-- Main Content (85%) -->
    <main class="h-full w-[85%] ml-[15%] bg-slate-50 dark:bg-slate-900 p-8 overflow-y-auto">
      <header class="mb-8">
        <h2 class="text-2xl font-bold text-on-surface">设置</h2>
        <p class="text-sm text-on-surface-variant mt-1">管理系统设置</p>
      </header>
      <!-- Settings Tabs -->
      <div class="bg-surface rounded-xl shadow-sm p-6">
        <div class="flex border-b border-slate-200 dark:border-slate-700 mb-6">
          <button 
            v-for="tab in tabs" 
            :key="tab.id"
            @click="activeTab = tab.id"
            class="px-4 py-2 text-sm font-medium transition-colors"
            :class="activeTab === tab.id ? 'text-primary border-b-2 border-primary' : 'text-on-surface-variant hover:text-on-surface'"
          >
            {{ tab.name }}
          </button>
        </div>
        <!-- Basic Settings -->
        <div v-if="activeTab === 'basic'" class="space-y-6">
          <h3 class="text-lg font-semibold text-on-surface">基本设置</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-on-surface-variant mb-2">餐厅名称</label>
              <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="restaurantInfo.name" />
            </div>
            <div>
              <label class="block text-sm font-medium text-on-surface-variant mb-2">联系电话</label>
              <input type="tel" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="restaurantInfo.phone" />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-on-surface-variant mb-2">地址</label>
              <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="restaurantInfo.address" />
            </div>
            <div>
              <label class="block text-sm font-medium text-on-surface-variant mb-2">营业时间</label>
              <div class="flex gap-2">
                <input type="time" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="restaurantInfo.openTime" />
                <span class="flex items-center">至</span>
                <input type="time" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="restaurantInfo.closeTime" />
              </div>
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-on-surface-variant mb-2">餐厅描述</label>
              <textarea class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" rows="4" v-model="restaurantInfo.description"></textarea>
            </div>
          </div>
          <div class="flex justify-end">
            <button class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors" @click="saveBasicSettings">保存设置</button>
          </div>
        </div>
        <!-- Member Management -->
        <div v-if="activeTab === 'member'" class="space-y-6">
          <h3 class="text-lg font-semibold text-on-surface">会员管理</h3>
          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left">
              <thead class="text-xs text-on-surface-variant uppercase bg-surface-container">
                <tr>
                  <th scope="col" class="px-6 py-3">ID</th>
                  <th scope="col" class="px-6 py-3">用户名</th>
                  <th scope="col" class="px-6 py-3">手机号</th>
                  <th scope="col" class="px-6 py-3">会员状态</th>
                  <th scope="col" class="px-6 py-3">操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="member in members" :key="member.userId" class="bg-surface border-b">
                  <td class="px-6 py-4">{{ member.userId }}</td>
                  <td class="px-6 py-4">{{ member.username }}</td>
                  <td class="px-6 py-4">{{ member.phone }}</td>
                  <td class="px-6 py-4">
                    <span v-if="member.isMember" class="px-2 py-1 text-xs font-medium rounded-full bg-green-100 text-green-800 dark:bg-green-900/40 dark:text-green-200">已开通</span>
                    <span v-else class="px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-800 dark:bg-gray-900/40 dark:text-gray-200">未开通</span>
                  </td>
                  <td class="px-6 py-4">
                    <button v-if="!member.isMember" @click="activateMember(member)" class="text-primary hover:underline text-sm">开通会员</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <!-- Coupon Management -->
        <div v-if="activeTab === 'coupon'" class="space-y-6">
          <div class="flex justify-between items-center">
            <h3 class="text-lg font-semibold text-on-surface">优惠券管理</h3>
            <button @click="openAddCouponModal" class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">添加优惠券模板</button>
          </div>
          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left">
              <thead class="text-xs text-on-surface-variant uppercase bg-surface-container">
                <tr>
                  <th scope="col" class="px-6 py-3">ID</th>
                  <th scope="col" class="px-6 py-3">名称</th>
                  <th scope="col" class="px-6 py-3">类型</th>
                  <th scope="col" class="px-6 py-3">面额</th>
                  <th scope="col" class="px-6 py-3">使用条件</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="coupon in couponTemplates" :key="coupon.id" class="bg-surface border-b">
                  <td class="px-6 py-4">{{ coupon.id }}</td>
                  <td class="px-6 py-4">{{ coupon.name }}</td>
                  <td class="px-6 py-4">{{ coupon.type === 1 ? '满减券' : '折扣券' }}</td>
                  <td class="px-6 py-4">{{ coupon.type === 1 ? `¥${coupon.value}` : `${coupon.value}%` }}</td>
                  <td class="px-6 py-4">{{ coupon.minSpend ? `满¥${coupon.minSpend}可用` : '无门槛' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <!-- Dish Management -->
        <div v-if="activeTab === 'dish'" class="space-y-6">
          <div class="flex justify-between items-center">
            <h3 class="text-lg font-semibold text-on-surface">菜品管理</h3>
            <div class="flex gap-3">
              <button @click="restockAll" class="border border-slate-300 dark:border-slate-700 px-4 py-2 rounded-lg text-sm font-medium transition-colors">一键补货</button>
              <button @click="openAddDishModal" class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">添加菜品</button>
            </div>
          </div>
          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left">
              <thead class="text-xs text-on-surface-variant uppercase bg-surface-container">
                <tr>
                  <th scope="col" class="px-6 py-3">ID</th>
                  <th scope="col" class="px-6 py-3">菜品名称</th>
                  <th scope="col" class="px-6 py-3">分类</th>
                  <th scope="col" class="px-6 py-3">价格</th>
                  <th scope="col" class="px-6 py-3">会员价</th>
                  <th scope="col" class="px-6 py-3">状态</th>
                  <th scope="col" class="px-6 py-3">操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="dish in dishes" :key="dish.id" class="bg-surface border-b">
                  <td class="px-6 py-4">{{ dish.id }}</td>
                  <td class="px-6 py-4">{{ dish.name }}</td>
                  <td class="px-6 py-4">{{ getCategoryName(dish.categoryId) }}</td>
                  <td class="px-6 py-4">¥{{ dish.price }}</td>
                  <td class="px-6 py-4">¥{{ getMemberPrice(dish.id) }}</td>
                  <td class="px-6 py-4">
                    <span v-if="dish.status === 1" class="px-2 py-1 text-xs font-medium rounded-full bg-green-100 text-green-800 dark:bg-green-900/40 dark:text-green-200">起售</span>
                    <span v-else class="px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-800 dark:bg-gray-900/40 dark:text-gray-200">停售</span>
                  </td>
                  <td class="px-6 py-4">
                    <div class="flex gap-2">
                      <button @click="openEditDishModal(dish)" class="text-primary hover:underline text-sm">编辑</button>
                      <button @click="deleteDish(dish.id)" class="text-red-500 hover:underline text-sm">删除</button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <!-- User Management -->
        <div v-if="activeTab === 'user'" class="space-y-6">
          <div class="flex justify-between items-center">
            <h3 class="text-lg font-semibold text-on-surface">用户管理</h3>
            <button @click="openAddUserModal" class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">添加用户</button>
          </div>
          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left">
              <thead class="text-xs text-on-surface-variant uppercase bg-surface-container">
                <tr>
                  <th scope="col" class="px-6 py-3">ID</th>
                  <th scope="col" class="px-6 py-3">用户名</th>
                  <th scope="col" class="px-6 py-3">手机号</th>
                  <th scope="col" class="px-6 py-3">角色</th>
                  <th scope="col" class="px-6 py-3">操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="user in users" :key="user.id" class="bg-surface border-b">
                  <td class="px-6 py-4">{{ user.id }}</td>
                  <td class="px-6 py-4">{{ user.username }}</td>
                  <td class="px-6 py-4">{{ user.phone }}</td>
                  <td class="px-6 py-4">
                    <span v-if="user.role === 1" class="px-2 py-1 text-xs font-medium rounded-full bg-blue-100 text-blue-800 dark:bg-blue-900/40 dark:text-blue-200">管理员</span>
                    <span v-else-if="user.role === 2" class="px-2 py-1 text-xs font-medium rounded-full bg-yellow-100 text-yellow-800 dark:bg-yellow-900/40 dark:text-yellow-200">收银员</span>
                    <span v-else-if="user.role === 3" class="px-2 py-1 text-xs font-medium rounded-full bg-green-100 text-green-800 dark:bg-green-900/40 dark:text-green-200">后厨</span>
                    <span v-else class="px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-800 dark:bg-gray-900/40 dark:text-gray-200">顾客</span>
                  </td>
                  <td class="px-6 py-4">
                    <div class="flex gap-2">
                      <button @click="openEditUserModal(user)" class="text-primary hover:underline text-sm">编辑</button>
                      <button @click="deleteUser(user.id)" class="text-red-500 hover:underline text-sm">删除</button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <!-- Profile Settings -->
        <div v-if="activeTab === 'profile'" class="space-y-6">
          <h3 class="text-lg font-semibold text-on-surface">个人设置</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="space-y-4">
              <h4 class="text-md font-medium text-on-surface">基本信息</h4>
              <div>
                <label class="block text-sm font-medium text-on-surface-variant mb-2">用户名</label>
                <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="userInfo.username" />
              </div>
              <div>
                <label class="block text-sm font-medium text-on-surface-variant mb-2">手机号</label>
                <input type="tel" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="userInfo.phone" />
              </div>
              <button class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors" @click="updateProfile">更新信息</button>
            </div>
            <div class="space-y-4">
              <h4 class="text-md font-medium text-on-surface">修改密码</h4>
              <div>
                <label class="block text-sm font-medium text-on-surface-variant mb-2">旧密码</label>
                <input type="password" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="passwordInfo.oldPassword" />
              </div>
              <div>
                <label class="block text-sm font-medium text-on-surface-variant mb-2">新密码</label>
                <input type="password" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="passwordInfo.newPassword" />
              </div>
              <div>
                <label class="block text-sm font-medium text-on-surface-variant mb-2">确认新密码</label>
                <input type="password" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="passwordInfo.confirmPassword" />
              </div>
              <button class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors" @click="changePassword">修改密码</button>
            </div>
          </div>
        </div>
        <!-- System Settings -->
        <div v-if="activeTab === 'system'" class="space-y-6">
          <h3 class="text-lg font-semibold text-on-surface">系统配置</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-on-surface-variant mb-2">系统名称</label>
              <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="systemConfig.systemName" />
            </div>
            <div>
              <label class="block text-sm font-medium text-on-surface-variant mb-2">系统版本</label>
              <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="systemConfig.version" />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-on-surface-variant mb-2">数据库连接</label>
              <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="systemConfig.databaseUrl" />
            </div>
            <div>
              <label class="block text-sm font-medium text-on-surface-variant mb-2">服务端口</label>
              <input type="number" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="systemConfig.port" />
            </div>
          </div>
          <div class="flex justify-end">
            <button class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors" @click="saveSystemSettings">保存配置</button>
          </div>
        </div>
      </div>
    </main>
    <!-- Add Dish Modal -->
    <div v-if="showAddDishModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-surface rounded-xl shadow-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-semibold text-on-surface mb-4">添加菜品</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">菜品名称</label>
            <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="newDish.name" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">分类</label>
            <select class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="newDish.categoryId">
              <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">价格</label>
            <input type="number" step="0.01" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="newDish.price" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">状态</label>
            <select class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="newDish.status">
              <option value="1">起售</option>
              <option value="0">停售</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">描述</label>
            <textarea class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" rows="3" v-model="newDish.description"></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">菜品图片</label>
            <input type="file" accept="image/*" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" @change="handleImageUpload($event, 'new')" />
            <div v-if="newDish.imageUrl" class="mt-2">
              <img :src="newDish.imageUrl ? backendUrl + newDish.imageUrl : ''" class="w-20 h-20 object-cover rounded-lg" />
            </div>
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button class="px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg text-sm font-medium transition-colors" @click="showAddDishModal = false">取消</button>
          <button class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors" @click="addDish">保存</button>
        </div>
      </div>
    </div>
    <!-- Edit Dish Modal -->
    <div v-if="showEditDishModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-surface rounded-xl shadow-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-semibold text-on-surface mb-4">编辑菜品</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">菜品名称</label>
            <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="editDish.name" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">分类</label>
            <select class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="editDish.categoryId">
              <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">价格</label>
            <input type="number" step="0.01" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="editDish.price" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">状态</label>
            <select class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="editDish.status">
              <option value="1">起售</option>
              <option value="0">停售</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">描述</label>
            <textarea class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" rows="3" v-model="editDish.description"></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">菜品图片</label>
            <input type="file" accept="image/*" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" @change="handleImageUpload($event, 'edit')" />
            <div v-if="editDish.imageUrl" class="mt-2">
              <img :src="editDish.imageUrl ? backendUrl + editDish.imageUrl : ''" class="w-20 h-20 object-cover rounded-lg" />
            </div>
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button class="px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg text-sm font-medium transition-colors" @click="showEditDishModal = false">取消</button>
          <button class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors" @click="updateDish">保存</button>
        </div>
      </div>
    </div>
    <!-- Add User Modal -->
    <div v-if="showAddUserModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-surface rounded-xl shadow-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-semibold text-on-surface mb-4">添加用户</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">用户名</label>
            <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="newUser.username" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">密码</label>
            <input type="password" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="newUser.password" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">手机号</label>
            <input type="tel" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="newUser.phone" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">角色</label>
            <select class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="newUser.role">
              <option :value="1">管理员</option>
              <option :value="2">收银员</option>
              <option :value="3">后厨</option>
              <option :value="0">顾客</option>
            </select>
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button class="px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg text-sm font-medium transition-colors" @click="showAddUserModal = false">取消</button>
          <button class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors" @click="addUser">保存</button>
        </div>
      </div>
    </div>
    <!-- Edit User Modal -->
    <div v-if="showEditUserModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-surface rounded-xl shadow-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-semibold text-on-surface mb-4">编辑用户</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">用户名</label>
            <input type="text" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="editUser.username" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">手机号</label>
            <input type="tel" class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="editUser.phone" />
          </div>
          <div>
            <label class="block text-sm font-medium text-on-surface-variant mb-2">角色</label>
            <select class="w-full px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg bg-surface-container dark:bg-slate-800 text-on-surface" v-model="editUser.role">
              <option :value="1">管理员</option>
              <option :value="2">收银员</option>
              <option :value="3">后厨</option>
              <option :value="0">顾客</option>
            </select>
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button class="px-4 py-2 border border-slate-300 dark:border-slate-700 rounded-lg text-sm font-medium transition-colors" @click="showEditUserModal = false">取消</button>
          <button class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors" @click="updateUser">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import request, { imageBaseURL } from '../utils/request'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()
const backendUrl = imageBaseURL // 使用imageBaseURL，自动适配当前环境

// 响应式数据
const activeTab = ref('basic')
const restaurantInfo = ref({ name: '', phone: '', address: '', openTime: '09:00', closeTime: '21:00', description: '' })
const dishes = ref([])
const users = ref([])
const members = ref([])
const couponTemplates = ref([])
const memberPrices = ref({})
const systemConfig = ref({ systemName: '淮味云膳', version: '1.0.0', databaseUrl: 'jdbc:mysql://localhost:3306/huaiwei-v2', port: 8080 })

// 菜品管理相关数据
const showAddDishModal = ref(false)
const showEditDishModal = ref(false)
const newDish = ref({ name: '', categoryId: '', price: '', status: 1, description: '', imageUrl: '' })
const editDish = ref({ id: '', name: '', categoryId: '', price: '', status: 1, description: '', imageUrl: '' })
const categories = ref([])

// 用户管理相关数据
const showAddUserModal = ref(false)
const showEditUserModal = ref(false)
const newUser = ref({ username: '', password: '', phone: '', role: 0 })
const editUser = ref({ id: '', username: '', phone: '', role: 0 })

// 个人设置相关数据
const userInfo = ref({ username: '', phone: '' })
const passwordInfo = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })

// 标签页数据
const tabs = [
  { id: 'basic', name: '基本设置' },
  { id: 'member', name: '会员管理' },
  { id: 'coupon', name: '优惠券' },
  { id: 'dish', name: '菜品管理' },
  { id: 'user', name: '用户管理' },
  { id: 'profile', name: '个人设置' },
  { id: 'system', name: '系统配置' }
]

// 生命周期钩子：组件挂载时的操作
onMounted(() => {
  // 检查URL参数，自动切换到个人设置标签页
  const urlParams = new URLSearchParams(window.location.search)
  const tab = urlParams.get('tab')
  if (tab === 'profile') {
    activeTab.value = 'profile'
  }
  fetchBasicSettings()
  fetchDishes()
  fetchUsers()
  fetchCategories()
  fetchUserProfile()
  fetchMembers()
  fetchCouponTemplates()
  fetchMemberPrices()
})

// 获取会员价
const fetchMemberPrices = async () => {
  try {
    const res = await request.get('/dish/member-price/list')
    const map = {}
    res.forEach(p => {
      map[p.dishId] = p.memberPrice
    })
    memberPrices.value = map
  } catch (e) {
    console.error('获取会员价失败:', e)
  }
}

const getMemberPrice = (dishId) => {
  return memberPrices.value[dishId]
}

// 一键补货
const restockAll = async () => {
  if (!confirm('确认将所有菜品库存设置为999吗？')) return
  try {
    const dishesRes = await request.get('/dish/list')
    for (const dish of dishesRes) {
      if (dish.stock !== undefined) {
        const updateData = { ...dish, stock: 999 }
        await request.put('/dish', updateData)
      }
    }
    await fetchDishes()
  } catch (error) {
    console.error('补货失败:', error)
    alert('补货失败')
  }
}

// 获取会员列表
const fetchMembers = async () => {
  try {
    // 清空之前的会员列表
    members.value = []
    
    // 直接从会员表获取数据，而不是从用户表
    try {
      const memberRes = await request.get('/member/list')
      console.log('直接从会员表获取数据:', memberRes)
      
      if (Array.isArray(memberRes)) {
        // 使用用户ID和昵称双重去重
        const uniqueMembers = new Map()
        for (const member of memberRes) {
          const key = `${member.userId || member.id}-${member.username || member.name || member.nickname || '未知用户'}`
          if (!uniqueMembers.has(key)) {
            uniqueMembers.set(key, member)
          }
        }
        
        members.value = Array.from(uniqueMembers.values())
        console.log('最终会员列表:', members.value)
        return
      }
    } catch (e) {
      console.error('直接获取会员列表失败:', e)
    }
    
    // 如果直接获取会员列表失败，回退到原来的方法
    console.log('回退到原来的方法获取会员列表')
    const res = await request.get('/user/list')
    console.log('获取用户列表:', res)
    
    // 使用用户ID去重，避免重复用户
    const uniqueUsers = new Map()
    for (const user of res) {
      if (user.id && !uniqueUsers.has(user.id)) {
        uniqueUsers.set(user.id, user)
      }
    }
    
    console.log('去重后的用户列表:', Array.from(uniqueUsers.values()))
    
    for (const user of uniqueUsers.values()) {
      try {
        const mRes = await request.get('/member/info', { params: { userId: user.id } })
        if (mRes) {
          console.log('获取会员信息:', mRes)
          members.value.push(mRes)
        }
      } catch (e) {
        // 该用户可能没有会员档案
      }
    }
    
    // 对最终的会员列表再次去重
    const finalUniqueMembers = new Map()
    for (const member of members.value) {
      const key = `${member.userId || member.id}-${member.username || member.name || member.nickname || '未知用户'}`
      if (!finalUniqueMembers.has(key)) {
        finalUniqueMembers.set(key, member)
      }
    }
    
    members.value = Array.from(finalUniqueMembers.values())
    console.log('最终去重后的会员列表:', members.value)
  } catch (error) {
    console.error('获取会员列表失败:', error)
  }
}

// 开通会员
const activateMember = async (m) => {
  if (!confirm('确认开通该用户为会员吗？')) return
  try {
    await request.post('/member/activate', { userId: m.userId })
    // 重新获取会员列表，确保管理员端显示最新的会员信息
    await fetchMembers()
  } catch (error) {
    console.error('开通会员失败:', error)
    alert('开通会员失败')
  }
}

// 获取优惠券模板
const fetchCouponTemplates = async () => {
  try {
    const res = await request.get('/coupon/template/list')
    couponTemplates.value = res
  } catch (error) {
    console.error('获取优惠券模板失败:', error)
  }
}

// 添加优惠券模板
const openAddCouponModal = () => {
  alert('添加优惠券模板功能请使用数据库直接添加或API调用')
}

// 获取基本设置
const fetchBasicSettings = async () => {
  try {
    const response = await request.get('/restaurant/info')
    restaurantInfo.value = response
  } catch (error) {
    console.error('获取基本设置失败:', error)
  }
}

// 保存基本设置
const saveBasicSettings = async () => {
  try {
    await request.post('/restaurant/save', restaurantInfo.value)
    // 保存成功
  } catch (error) {
    console.error('保存基本设置失败:', error)
  }
}

// 获取菜品列表
const fetchDishes = async () => {
  try {
    const response = await request.get('/dish/list')
    console.log('获取菜品列表响应:', response)
    dishes.value = response
    console.log('获取到的菜品数据:', dishes.value)
  } catch (error) {
    console.error('获取菜品列表失败:', error)
  }
}

// 获取菜品分类
const fetchCategories = async () => {
  try {
    const response = await request.get('/category/list')
    categories.value = response
  } catch (error) {
    console.error('获取菜品分类失败:', error)
  }
}

// 获取分类文本
const getCategoryName = (categoryId) => {
  const cat = categories.value.find(c => c.id === categoryId)
  return cat ? cat.name : '未知分类'
}

// 打开添加菜品模态框
const openAddDishModal = () => {
  newDish.value = { name: '', categoryId: categories.value.length > 0 ? categories.value[0].id : '', price: '', status: 1, description: '' }
  showAddDishModal.value = true
}

// 打开编辑菜品模态框
const openEditDishModal = (dish) => {
  editDish.value = { id: dish.id, name: dish.name, categoryId: dish.categoryId, price: dish.price, status: dish.status, description: dish.description, imageUrl: dish.imageUrl }
  showEditDishModal.value = true
}

// 处理本地图片选择
const handleLocalImageSelect = (event, type) => {
  const file = event.target.files[0]
  if (file) {
    // 使用FileReader读取本地图片
    const reader = new FileReader()
    reader.onload = (e) => {
      const imageUrl = e.target.result
      console.log('选择本地图片，URL:', imageUrl)
      if (type === 'new') {
        newDish.value.imageUrl = imageUrl
        console.log('设置新菜品图片URL:', newDish.value.imageUrl)
      } else {
        editDish.value.imageUrl = imageUrl
        console.log('设置编辑菜品图片URL:', editDish.value.imageUrl)
      }
    }
    reader.readAsDataURL(file)
  }
}

// 处理图片上传
const handleImageUpload = async (event, type) => {
  const file = event.target.files[0]
  if (file) {
    const formData = new FormData()
    formData.append('file', file)
    try {
      console.log('上传图片:', file.name)
      // 使用request实例发送请求
      const response = await request({
        method: 'POST',
        url: '/dish/upload',
        data: formData // 不手动设置Content-Type，让浏览器自动生成
      })
      // 确保提取出纯字符串路径
      const imageUrl = typeof response === 'string' ? response : (response && response.data ? response.data : '')
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
      console.dir(error)
      console.error('上传图片失败详细信息:', error.response)
      if (error.response && error.response.data) {
        console.error('服务器返回的错误数据:', error.response.data)
      }
    }
  }
}

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
    console.log('更新菜品开始:', editDish.value)
    console.log('编辑菜品图片URL:', editDish.value.imageUrl)
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
    console.error('更新菜品失败详细信息:', error.response)
  }
}

// 删除菜品
const deleteDish = async (id) => {
  try {
    const response = await axios.delete(`/api/dish/${id}`)
    if (response.data.code === 200) {
      fetchDishes()
    }
  } catch (error) {
    console.error('删除菜品失败:', error)
  }
}

// 获取用户列表
const fetchUsers = async () => {
  try {
    const response = await request.get('/user/list')
    users.value = response
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

// 打开添加用户模态框
const openAddUserModal = () => {
  newUser.value = { username: '', password: '', phone: '', role: 0 }
  showAddUserModal.value = true
}

// 打开编辑用户模态框
const openEditUserModal = (user) => {
  editUser.value = { id: user.id, username: user.username, phone: user.phone, role: user.role }
  showEditUserModal.value = true
}

// 添加用户
const addUser = async () => {
  try {
    await request.post('/user', newUser.value)
    showAddUserModal.value = false
    fetchUsers()
  } catch (error) {
    console.error('添加用户失败:', error)
    alert('添加用户失败')
  }
}

// 更新用户
const updateUser = async () => {
  try {
    await request.put('/user', editUser.value)
    showEditUserModal.value = false
    fetchUsers()
  } catch (error) {
    console.error('更新用户失败:', error)
    alert('更新用户失败')
  }
}

// 删除用户
const deleteUser = async (id) => {
  if (!confirm('确定要删除该用户吗？')) return;
  try {
    await request.delete(`/user/${id}`)
    fetchUsers()
  } catch (error) {
    console.error('删除用户失败:', error)
    alert('删除用户失败')
  }
}

// 保存系统设置
const saveSystemSettings = async () => {
  try {
    await request.post('/system/save', systemConfig.value)
    // 保存成功
  } catch (error) {
    console.error('保存系统设置失败:', error)
  }
}

// 获取用户信息
const fetchUserProfile = async () => {
  try {
    const response = await request.get('/user/profile')
    userInfo.value = response
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 更新用户信息
const updateProfile = async () => {
  try {
    await request.put('/user/profile', userInfo.value)
    // 更新成功
  } catch (error) {
    console.error('更新用户信息失败:', error)
  }
}

// 修改密码
const changePassword = async () => {
  if (passwordInfo.value.newPassword !== passwordInfo.value.confirmPassword) {
    console.error('新密码和确认密码不一致')
    return
  }
  try {
    await request.put('/user/password', {
      oldPassword: passwordInfo.value.oldPassword,
      newPassword: passwordInfo.value.newPassword
    })
    passwordInfo.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
  } catch (error) {
    console.error('修改密码失败:', error)
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Manrope:wght@500;600;700;800&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap');

/* 导入Google字体 */
body {
  font-family: 'Inter', sans-serif;
}
h1, h2, h3 {
  font-family: 'Manrope', sans-serif;
}
</style>