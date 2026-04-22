<template>
  <div class="flex h-screen overflow-hidden text-on-background">
    <aside class="h-full w-[15%] fixed left-0 top-0 bg-slate-100 dark:bg-slate-800 flex flex-col py-8 z-50">
      <div class="px-6 mb-10">
        <h1 class="text-xl font-bold text-teal-900 dark:text-teal-100 mb-1">淮味云膳</h1>
        <p class="text-xs text-on-surface-variant font-medium opacity-70">管理员端</p>
      </div>
      <nav class="flex-1 space-y-2">
        <router-link 
          to="/admin"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/admin' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">📊</span>
          <span>数据概览</span>
        </router-link>
        <router-link 
          to="/orders"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/orders' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">📋</span>
          <span>订单管理</span>
        </router-link>
        <router-link 
          to="/settings"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/settings' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">⚙️</span>
          <span>系统设置</span>
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

    <main class="h-full w-[85%] ml-[15%] bg-slate-50 dark:bg-slate-900 p-8 overflow-y-auto">
      <header class="mb-8">
        <h2 class="text-2xl font-bold text-on-surface">数据概览</h2>
        <p class="text-sm text-on-surface-variant mt-1">查看系统运营数据</p>
      </header>

      <div class="grid grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm text-slate-500">今日订单</p>
              <p class="text-3xl font-bold text-teal-600 mt-2">{{ todayOrders }}</p>
            </div>
            <div class="w-12 h-12 bg-teal-100 rounded-xl flex items-center justify-center">
              <span class="text-2xl">📋</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm text-slate-500">今日营业额</p>
              <p class="text-3xl font-bold text-green-600 mt-2">¥{{ todayRevenue.toFixed(2) }}</p>
            </div>
            <div class="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center">
              <span class="text-2xl">💰</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm text-slate-500">待处理订单</p>
              <p class="text-3xl font-bold text-yellow-600 mt-2">{{ pendingOrders }}</p>
            </div>
            <div class="w-12 h-12 bg-yellow-100 rounded-xl flex items-center justify-center">
              <span class="text-2xl">⏳</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm text-slate-500">菜品数量</p>
              <p class="text-3xl font-bold text-blue-600 mt-2">{{ dishCount }}</p>
            </div>
            <div class="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center">
              <span class="text-2xl">🍽️</span>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-2 gap-6">
        <div class="bg-white rounded-xl shadow-sm p-6">
          <h3 class="text-lg font-bold text-slate-800 mb-4">最近订单</h3>
          <div class="space-y-3">
            <div
              v-for="order in recentOrders"
              :key="order.id"
              @click="openOrderDetail(order)"
              class="flex items-center justify-between p-3 bg-slate-50 rounded-lg cursor-pointer hover:bg-slate-100 transition-colors"
            >
              <div>
                <p class="font-medium text-slate-800">{{ order.tableNo || '外带' }}</p>
                <p class="text-xs text-slate-500">{{ order.createTime }}</p>
              </div>
              <div class="text-right">
                <p class="font-bold text-teal-600">¥{{ parseFloat(order.totalAmount).toFixed(2) }}</p>
                <span
                  :class="[
                    'text-xs px-2 py-1 rounded-full',
                    order.status === 1 ? 'bg-yellow-100 text-yellow-800' :
                    order.status === 2 ? 'bg-blue-100 text-blue-800' :
                    order.status === 3 ? 'bg-green-100 text-green-800' :
                    'bg-red-100 text-red-800'
                  ]"
                >
                  {{ getStatusText(order.status) }}
                </span>
              </div>
            </div>
            <div v-if="recentOrders.length === 0" class="text-center py-8 text-slate-400">
              暂无订单
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-sm p-6">
          <h3 class="text-lg font-bold text-slate-800 mb-4">热销菜品</h3>
          <div class="space-y-3">
            <div
              v-for="(item, index) in hotDishes"
              :key="index"
              class="flex items-center justify-between p-3 bg-slate-50 rounded-lg"
            >
              <div class="flex items-center gap-3">
                <span
                  :class="[
                    'w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold',
                    index === 0 ? 'bg-yellow-400 text-white' :
                    index === 1 ? 'bg-gray-400 text-white' :
                    index === 2 ? 'bg-orange-400 text-white' :
                    'bg-slate-200 text-slate-600'
                  ]"
                >
                  {{ index + 1 }}
                </span>
                <span class="font-medium text-slate-800">{{ item.name }}</span>
              </div>
              <span class="text-teal-600 font-bold">¥{{ item.total.toFixed(2) }}</span>
            </div>
            <div v-if="hotDishes.length === 0" class="text-center py-8 text-slate-400">
              暂无数据
            </div>
          </div>
        </div>
      </div>

      <div class="mt-6 bg-white rounded-xl shadow-sm p-6">
        <h3 class="text-lg font-bold text-slate-800 mb-4">快速操作</h3>
        <div class="grid grid-cols-3 gap-4">
          <button
            ref="dishButtonRef"
            @click="openDishModal"
            class="flex flex-col items-center gap-2 p-4 bg-teal-50 rounded-xl hover:bg-teal-100 transition-colors"
          >
            <span class="text-3xl">🍽️</span>
            <span class="text-sm font-medium text-teal-700">菜品管理</span>
          </button>
          <button
            ref="userButtonRef"
            @click="openUserModal"
            class="flex flex-col items-center gap-2 p-4 bg-blue-50 rounded-xl hover:bg-blue-100 transition-colors"
          >
            <span class="text-3xl">👥</span>
            <span class="text-sm font-medium text-blue-700">用户管理</span>
          </button>
          <button
            ref="restaurantButtonRef"
            @click="openRestaurantModal"
            class="flex flex-col items-center gap-2 p-4 bg-green-50 rounded-xl hover:bg-green-100 transition-colors"
          >
            <span class="text-3xl">🏪</span>
            <span class="text-sm font-medium text-green-700">餐厅信息</span>
          </button>
          <button
            ref="memberButtonRef"
            @click="openMemberPanel"
            class="flex flex-col items-center gap-2 p-4 bg-orange-50 rounded-xl hover:bg-orange-100 transition-colors"
          >
            <span class="text-3xl">👑</span>
            <span class="text-sm font-medium text-orange-700">会员管理</span>
          </button>
        </div>
      </div>
    </main>

    <!-- 会员管理弹窗 -->
    <div v-if="showMemberPanel" class="fixed inset-0 bg-black/50 z-[100] flex items-center justify-center" @click.self="showMemberPanel = false">
      <div ref="memberModalRef" class="bg-white rounded-2xl w-[800px] max-h-[80vh] flex flex-col" style="will-change: transform, opacity;">
        <div class="flex items-center justify-between p-4 border-b border-slate-200">
          <h2 class="text-lg font-bold text-slate-800">会员管理</h2>
          <div class="flex items-center gap-3">
            <button @click="showMemberPanel = false" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
          </div>
        </div>
        <div class="flex-1 overflow-y-auto p-4">
          <div class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="border-b border-slate-200">
                  <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">昵称</th>
                  <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">手机号</th>
                  <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">积分</th>
                  <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">会员状态</th>
                  <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">到期日</th>
                  <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="m in allMembers" :key="m.id" class="border-b border-slate-100 hover:bg-slate-50">
                  <td class="py-3 px-4 text-sm">{{ m.nickname || m.phone }}</td>
                  <td class="py-3 px-4 text-sm">{{ m.phone }}</td>
                  <td class="py-3 px-4 text-sm font-bold text-orange-600">{{ m.point || 0 }}</td>
                  <td class="py-3 px-4 text-sm">
                    <span :class="['px-2 py-1 rounded-full text-xs font-medium', m.isMember === 1 ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-600']">
                      {{ m.isMember === 1 ? '会员' : '普通' }}
                    </span>
                  </td>
                  <td class="py-3 px-4 text-sm">{{ m.memberExpire || '-' }}</td>
                  <td class="py-3 px-4">
                    <button v-if="m.isMember !== 1" @click="activateMember(m)" class="text-orange-600 hover:text-orange-700 text-sm font-medium">开通</button>
                    <span v-else class="text-slate-400 text-sm">已开通</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-if="allMembers.length === 0" class="text-center py-12 text-slate-400">暂无会员数据</div>
        </div>
      </div>
    </div>

    <!-- 菜品管理弹窗 -->
    <div v-if="showDishModal" class="fixed inset-0 bg-black/50 z-[100] flex items-center justify-center" @click.self="closeDishModal">
      <div ref="dishModalRef" class="bg-white rounded-2xl w-[800px] max-h-[80vh] flex flex-col" style="will-change: transform, opacity;">
        <div class="flex items-center justify-between p-4 border-b border-slate-200">
          <h2 class="text-lg font-bold text-slate-800">菜品管理</h2>
          <div class="flex items-center gap-3">
            <button @click="openAddDishModal" class="bg-teal-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-teal-700">添加菜品</button>
            <button @click="closeDishModal" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
          </div>
        </div>
        <div class="flex-1 overflow-y-auto p-4">
          <table class="w-full">
            <thead>
              <tr class="border-b border-slate-200">
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">菜品名称</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">分类</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">价格</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">库存</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">状态</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="dish in dishes" :key="dish.id" class="border-b border-slate-100 hover:bg-slate-50">
                <td class="py-3 px-4 text-sm">{{ dish.name }}</td>
                <td class="py-3 px-4 text-sm">{{ getCategoryName(dish.categoryId) }}</td>
                <td class="py-3 px-4 text-sm">¥{{ dish.price }}</td>
                <td class="py-3 px-4 text-sm">
                  <div class="flex items-center gap-2">
                    <span :class="[
                      'px-2 py-1 rounded-full text-xs font-medium',
                      dish.stock <= 0 ? 'bg-red-100 text-red-800' :
                      dish.stock <= 5 ? 'bg-orange-100 text-orange-800' :
                      'bg-green-100 text-green-800'
                    ]">
                      {{ dish.stock ?? '—' }}
                    </span>
                    <button @click="quickRestock(dish.id)" class="text-xs text-teal-600 hover:text-teal-700" title="一键补货">
                      补货
                    </button>
                  </div>
                </td>
                <td class="py-3 px-4 text-sm">
                  <span :class="['px-2 py-1 rounded-full text-xs font-medium', dish.status === 1 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800']">
                    {{ dish.status === 1 ? '起售' : '停售' }}
                  </span>
                </td>
                <td class="py-3 px-4">
                  <button @click="openEditDishModal(dish)" class="text-teal-600 hover:text-teal-700 text-sm font-medium mr-3">编辑</button>
                  <button @click="deleteDish(dish.id)" class="text-red-500 hover:text-red-600 text-sm font-medium">删除</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 用户管理弹窗 -->
    <div v-if="showUserModal" class="fixed inset-0 bg-black/50 z-[100] flex items-center justify-center" @click.self="closeUserModal">
      <div ref="userModalRef" class="bg-white rounded-2xl w-[700px] max-h-[80vh] flex flex-col" style="will-change: transform, opacity;">
        <div class="flex items-center justify-between p-4 border-b border-slate-200">
          <h2 class="text-lg font-bold text-slate-800">用户管理</h2>
          <div class="flex items-center gap-3">
            <button @click="openAddUserModal" class="bg-teal-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-teal-700">添加用户</button>
            <button @click="closeUserModal" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
          </div>
        </div>
        <div class="flex-1 overflow-y-auto p-4">
          <table class="w-full">
            <thead>
              <tr class="border-b border-slate-200">
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">用户名</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">手机号</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">角色</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-slate-500">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.id" class="border-b border-slate-100 hover:bg-slate-50">
                <td class="py-3 px-4 text-sm">{{ user.username }}</td>
                <td class="py-3 px-4 text-sm">{{ user.phone }}</td>
                <td class="py-3 px-4 text-sm">
                  <span :class="['px-2 py-1 rounded-full text-xs font-medium',
                    user.role === 1 ? 'bg-blue-100 text-blue-800' :
                    user.role === 2 ? 'bg-green-100 text-green-800' :
                    user.role === 3 ? 'bg-orange-100 text-orange-800' :
                    'bg-gray-100 text-gray-800']">
                    {{ user.role === 1 ? '管理员' : user.role === 2 ? '收银员' : user.role === 3 ? '后厨' : '顾客' }}
                  </span>
                </td>
                <td class="py-3 px-4">
                  <button @click="openEditUserModal(user)" class="text-teal-600 hover:text-teal-700 text-sm font-medium mr-3">编辑</button>
                  <button @click="deleteUser(user.id)" class="text-red-500 hover:text-red-600 text-sm font-medium">删除</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 餐厅信息弹窗 -->
    <div v-if="showRestaurantModal" class="fixed inset-0 bg-black/50 z-[100] flex items-center justify-center" @click.self="closeRestaurantModal">
      <div ref="restaurantModalRef" class="bg-white rounded-2xl w-[500px] max-h-[80vh] flex flex-col" style="will-change: transform, opacity;">
        <div class="flex items-center justify-between p-4 border-b border-slate-200">
          <h2 class="text-lg font-bold text-slate-800">餐厅信息</h2>
          <button @click="closeRestaurantModal" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
        </div>
        <div class="flex-1 overflow-y-auto p-4 space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">餐厅名称</label>
            <input type="text" v-model="restaurantInfo.name" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">联系电话</label>
            <input type="tel" v-model="restaurantInfo.phone" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">餐厅地址</label>
            <input type="text" v-model="restaurantInfo.address" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div class="flex gap-4">
            <div class="flex-1">
              <label class="block text-sm font-medium text-slate-600 mb-2">营业开始</label>
              <input type="time" v-model="restaurantInfo.openTime" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
            </div>
            <div class="flex-1">
              <label class="block text-sm font-medium text-slate-600 mb-2">营业结束</label>
              <input type="time" v-model="restaurantInfo.closeTime" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">餐厅简介</label>
            <textarea v-model="restaurantInfo.description" rows="3" class="w-full px-4 py-2 border border-slate-300 rounded-lg"></textarea>
          </div>
        </div>
        <div class="p-4 border-t border-slate-200 flex justify-end">
          <button @click="saveRestaurantInfo" class="bg-teal-600 text-white px-6 py-2 rounded-lg font-medium hover:bg-teal-700">保存设置</button>
        </div>
      </div>
    </div>

    <!-- 添加/编辑菜品弹窗 -->
    <div v-if="showDishFormModal" class="fixed inset-0 bg-black/50 z-[110] flex items-center justify-center" @click.self="showDishFormModal = false">
      <div class="bg-white rounded-2xl w-[450px] p-6 max-h-[90vh] overflow-y-auto">
        <h3 class="text-lg font-bold text-slate-800 mb-4">{{ editingDish.id ? '编辑菜品' : '添加菜品' }}</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">菜品名称</label>
            <input type="text" v-model="editingDish.name" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">分类</label>
            <select v-model="editingDish.categoryId" class="w-full px-4 py-2 border border-slate-300 rounded-lg">
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">价格</label>
            <input type="number" step="0.01" v-model="editingDish.price" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">库存</label>
            <input type="number" v-model="editingDish.stock" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">状态</label>
            <select v-model="editingDish.status" class="w-full px-4 py-2 border border-slate-300 rounded-lg">
              <option :value="1">起售</option>
              <option :value="0">停售</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">描述</label>
            <textarea v-model="editingDish.description" rows="2" class="w-full px-4 py-2 border border-slate-300 rounded-lg"></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">菜品图片</label>
            <input type="file" accept="image/*" class="w-full px-4 py-2 border border-slate-300 rounded-lg" @change="handleImageUpload" />
            <div v-if="editingDish.imageUrl" class="mt-2">
              <img :src="getImageUrl(editingDish.imageUrl)" class="w-20 h-20 object-cover rounded-lg" />
            </div>
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button @click="showDishFormModal = false" class="px-4 py-2 border border-slate-300 rounded-lg text-sm font-medium">取消</button>
          <button @click="saveDish" class="bg-teal-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-teal-700">保存</button>
        </div>
      </div>
    </div>

    <!-- 添加/编辑用户弹窗 -->
    <div v-if="showUserFormModal" class="fixed inset-0 bg-black/50 z-[110] flex items-center justify-center" @click.self="showUserFormModal = false">
      <div class="bg-white rounded-2xl w-[400px] p-6">
        <h3 class="text-lg font-bold text-slate-800 mb-4">{{ editingUser.id ? '编辑用户' : '添加用户' }}</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">用户名</label>
            <input type="text" v-model="editingUser.username" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div v-if="!editingUser.id">
            <label class="block text-sm font-medium text-slate-600 mb-2">密码</label>
            <input type="password" v-model="editingUser.password" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">手机号</label>
            <input type="tel" v-model="editingUser.phone" class="w-full px-4 py-2 border border-slate-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-600 mb-2">角色</label>
            <select v-model="editingUser.role" class="w-full px-4 py-2 border border-slate-300 rounded-lg">
              <option :value="1">管理员</option>
              <option :value="2">收银员</option>
              <option :value="3">后厨</option>
              <option :value="0">顾客</option>
            </select>
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button @click="showUserFormModal = false" class="px-4 py-2 border border-slate-300 rounded-lg text-sm font-medium">取消</button>
          <button @click="saveUser" class="bg-teal-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-teal-700">保存</button>
        </div>
      </div>
    </div>

    <!-- 订单详情弹窗 -->
    <div v-if="showOrderDetail" class="fixed inset-0 bg-black/50 z-[100] flex items-center justify-center" @click.self="closeOrderDetail">
      <div ref="orderDetailRef" class="bg-white rounded-2xl w-[500px] max-h-[80vh] flex flex-col" style="will-change: transform, opacity;">
        <div class="flex items-center justify-between p-4 border-b border-slate-200">
          <h2 class="text-lg font-bold text-slate-800">订单详情</h2>
          <button @click="closeOrderDetail" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
        </div>
        <div class="flex-1 overflow-y-auto p-4">
          <div class="space-y-4">
            <div class="flex justify-between items-center pb-3 border-b border-slate-100">
              <span class="text-slate-500">订单号</span>
              <span class="font-medium">{{ selectedOrder?.orderNo }}</span>
            </div>
            <div class="flex justify-between items-center pb-3 border-b border-slate-100">
              <span class="text-slate-500">桌号</span>
              <span class="font-medium">{{ selectedOrder?.tableNo || '外带' }}</span>
            </div>
            <div class="flex justify-between items-center pb-3 border-b border-slate-100">
              <span class="text-slate-500">下单时间</span>
              <span class="font-medium">{{ selectedOrder?.createTime }}</span>
            </div>
            <div class="flex justify-between items-center pb-3 border-b border-slate-100">
              <span class="text-slate-500">订单状态</span>
              <span
                :class="[
                  'px-2 py-1 rounded-full text-xs font-medium',
                  selectedOrder?.status === 1 ? 'bg-yellow-100 text-yellow-800' :
                  selectedOrder?.status === 2 ? 'bg-blue-100 text-blue-800' :
                  selectedOrder?.status === 3 ? 'bg-green-100 text-green-800' :
                  'bg-red-100 text-red-800'
                ]"
              >
                {{ getStatusText(selectedOrder?.status) }}
              </span>
            </div>
            <div class="pt-2">
              <h3 class="font-bold text-slate-800 mb-3">菜品明细</h3>
              <div class="space-y-3">
                <div
                  v-for="(item, index) in orderDetails"
                  :key="index"
                  class="flex items-center gap-3 p-3 bg-slate-50 rounded-lg"
                >
                  <div class="flex-1">
                    <h4 class="font-medium text-slate-800">{{ item.dishName }}</h4>
                    <div class="flex justify-between items-center mt-1">
                      <span class="text-teal-600 font-bold">¥{{ item.unitPrice }}</span>
                      <span class="text-slate-500">x{{ item.quantity }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="p-4 border-t border-slate-200 bg-slate-50">
          <div class="flex justify-between items-center text-xl font-bold">
            <span>总计</span>
            <span class="text-teal-600">¥{{ parseFloat(selectedOrder?.totalAmount || 0).toFixed(2) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import request, { imageBaseURL } from '../utils/request'

const router = useRouter()
const userStore = useUserStore()

const backendUrl = imageBaseURL // 使用imageBaseURL，自动适配当前环境

const orders = ref([])
const dishes = ref([])
const users = ref([])
const categories = ref([])

const dishButtonRef = ref(null)
const userButtonRef = ref(null)
const restaurantButtonRef = ref(null)
const dishModalRef = ref(null)
const userModalRef = ref(null)
const restaurantModalRef = ref(null)
const orderDetailRef = ref(null)

const showDishModal = ref(false)
const showUserModal = ref(false)
const showRestaurantModal = ref(false)
const showDishFormModal = ref(false)
const showUserFormModal = ref(false)
const showOrderDetail = ref(false)

const selectedOrder = ref(null)
const orderDetails = ref([])

const editingDish = ref({ id: null, name: '', categoryId: '', price: '', stock: 999, status: 1, imageUrl: '', description: '' })
const editingUser = ref({ id: null, username: '', password: '', phone: '', role: 0 })

const showMemberPanel = ref(false)
const allMembers = ref([])

const memberButtonRef = ref(null)
const memberModalRef = ref(null)

const openMemberPanel = async () => {
  showMemberPanel.value = true
  
  await nextTick()
  
  if (memberModalRef.value) {
    memberModalRef.value.style.opacity = '0'
    memberModalRef.value.style.transform = 'scale(0.1)'
  }
  
  try {
    // 优先使用会员列表接口
    let members = []
    try {
      const memberRes = await request.get('/member/list')
      if (Array.isArray(memberRes) && memberRes.length > 0) {
        members = memberRes
      }
    } catch (e) {
      console.log('会员列表接口不可用，降级到旧方法', e)
    }
    
    // 如果会员列表接口返回空，使用旧方法
    if (members.length === 0) {
      const usersRes = await request.get('/user/list')
      const seenIds = new Set()
      const users = []
      for (const user of usersRes) {
        if (!seenIds.has(user.id)) {
          seenIds.add(user.id)
          users.push(user)
        }
      }
      for (const user of users) {
        try {
          const mRes = await request.get('/member/info', { params: { userId: user.id } })
          if (mRes) members.push(mRes)
        } catch (e) {
          members.push({ id: user.id, userId: user.id, phone: user.phone || '', nickname: user.username || '', point: 0, isMember: 0, memberExpire: null })
        }
      }
    }
    
    // 对会员列表去重
    const uniqueMembers = []
    const seenMemberIds = new Set()
    for (const member of members) {
      const id = member.userId || member.id
      if (!seenMemberIds.has(id)) {
        seenMemberIds.add(id)
        uniqueMembers.push(member)
      }
    }
    
    allMembers.value = uniqueMembers
    console.log('最终会员列表:', uniqueMembers)
  } catch (e) {
    console.error('获取会员列表失败:', e)
  }

  await nextTick()
  
  if (memberButtonRef.value && memberModalRef.value) {
    const buttonRect = memberButtonRef.value.getBoundingClientRect()
    const modalRect = memberModalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(-10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      },
      { 
        transform: `translate(${deltaX * 0.3}px, ${deltaY * 0.3}px) scale(0.5) rotate(-3deg)`, 
        opacity: 0.8,
        borderRadius: '24px',
        offset: 0.4
      },
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      }
    ]
    
    const options = {
      duration: 380,
      easing: 'cubic-bezier(0.2, 0.9, 0.4, 1.1)',
      fill: 'forwards'
    }
    
    memberModalRef.value.animate(keyframes, options)
  }
}

const activateMember = async (m) => {
  if (!confirm('确认开通该用户为会员吗？')) return
  try {
    await request.post('/member/activate', { userId: m.userId })
    m.isMember = 1
  } catch (e) {
    console.error('开通会员失败:', e)
    alert('开通会员失败')
  }
}

const restaurantInfo = ref({
  name: '',
  phone: '',
  address: '',
  openTime: '09:00',
  closeTime: '21:00',
  description: ''
})

const todayOrders = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return orders.value.filter(o => o.createTime?.startsWith(today)).length
})

const todayRevenue = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return orders.value
    .filter(o => o.createTime?.startsWith(today) && o.status !== 4)
    .reduce((sum, o) => sum + parseFloat(o.totalAmount || 0), 0)
})

const pendingOrders = computed(() => {
  return orders.value.filter(o => o.status === 1 || o.status === 2).length
})

const dishCount = computed(() => dishes.value.length)

const recentOrders = computed(() => {
  return [...orders.value]
    .sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    .slice(0, 5)
})

const hotDishes = computed(() => {
  const dishSales = {}
  orders.value.forEach(order => {
    if (order.orderDetails) {
      order.orderDetails.forEach(item => {
        if (!dishSales[item.dishName]) dishSales[item.dishName] = 0
        dishSales[item.dishName] += item.unitPrice * item.quantity
      })
    }
  })
  return Object.entries(dishSales)
    .map(([name, total]) => ({ name, total }))
    .sort((a, b) => b.total - a.total)
    .slice(0, 5)
})

const getCategoryName = (categoryId) => {
  const cat = categories.value.find(c => c.id === categoryId)
  return cat ? cat.name : '未知'
}

const getStatusText = (status) => {
  const map = { 0: '待付款', 1: '待接单', 2: '制作中', 3: '已完成', 4: '已取消' }
  return map[status] || '未知'
}

const openDishModal = async () => {
  showDishModal.value = true
  fetchDishes()
  await nextTick()
  
  if (dishButtonRef.value && dishModalRef.value) {
    const buttonRect = dishButtonRef.value.getBoundingClientRect()
    const modalRect = dishModalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(-10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      },
      { 
        transform: `translate(${deltaX * 0.3}px, ${deltaY * 0.3}px) scale(0.5) rotate(-3deg)`, 
        opacity: 0.8,
        borderRadius: '24px',
        offset: 0.4
      },
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      }
    ]
    
    const options = {
      duration: 380,
      easing: 'cubic-bezier(0.2, 0.9, 0.4, 1.1)',
      fill: 'forwards'
    }
    
    dishModalRef.value.animate(keyframes, options)
  }
}

const openUserModal = async () => {
  showUserModal.value = true
  fetchUsers()
  await nextTick()
  
  if (userButtonRef.value && userModalRef.value) {
    const buttonRect = userButtonRef.value.getBoundingClientRect()
    const modalRect = userModalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(-10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      },
      { 
        transform: `translate(${deltaX * 0.3}px, ${deltaY * 0.3}px) scale(0.5) rotate(-3deg)`, 
        opacity: 0.8,
        borderRadius: '24px',
        offset: 0.4
      },
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      }
    ]
    
    const options = {
      duration: 380,
      easing: 'cubic-bezier(0.2, 0.9, 0.4, 1.1)',
      fill: 'forwards'
    }
    
    userModalRef.value.animate(keyframes, options)
  }
}

const openRestaurantModal = async () => {
  showRestaurantModal.value = true
  fetchRestaurantInfo()
  await nextTick()
  
  if (restaurantButtonRef.value && restaurantModalRef.value) {
    const buttonRect = restaurantButtonRef.value.getBoundingClientRect()
    const modalRect = restaurantModalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(-10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      },
      { 
        transform: `translate(${deltaX * 0.3}px, ${deltaY * 0.3}px) scale(0.5) rotate(-3deg)`, 
        opacity: 0.8,
        borderRadius: '24px',
        offset: 0.4
      },
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      }
    ]
    
    const options = {
      duration: 380,
      easing: 'cubic-bezier(0.2, 0.9, 0.4, 1.1)',
      fill: 'forwards'
    }
    
    restaurantModalRef.value.animate(keyframes, options)
  }
}

const closeDishModal = async () => {
  if (dishButtonRef.value && dishModalRef.value) {
    const buttonRect = dishButtonRef.value.getBoundingClientRect()
    const modalRect = dishModalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      },
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      }
    ]
    
    const options = {
      duration: 280,
      easing: 'cubic-bezier(0.4, 0, 0.2, 1)',
      fill: 'forwards'
    }
    
    const animation = dishModalRef.value.animate(keyframes, options)
    
    animation.onfinish = () => {
      showDishModal.value = false
    }
  } else {
    showDishModal.value = false
  }
}

const closeUserModal = async () => {
  if (userButtonRef.value && userModalRef.value) {
    const buttonRect = userButtonRef.value.getBoundingClientRect()
    const modalRect = userModalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      },
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      }
    ]
    
    const options = {
      duration: 280,
      easing: 'cubic-bezier(0.4, 0, 0.2, 1)',
      fill: 'forwards'
    }
    
    const animation = userModalRef.value.animate(keyframes, options)
    
    animation.onfinish = () => {
      showUserModal.value = false
    }
  } else {
    showUserModal.value = false
  }
}

const closeRestaurantModal = async () => {
  if (restaurantButtonRef.value && restaurantModalRef.value) {
    const buttonRect = restaurantButtonRef.value.getBoundingClientRect()
    const modalRect = restaurantModalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      },
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      }
    ]
    
    const options = {
      duration: 280,
      easing: 'cubic-bezier(0.4, 0, 0.2, 1)',
      fill: 'forwards'
    }
    
    const animation = restaurantModalRef.value.animate(keyframes, options)
    
    animation.onfinish = () => {
      showRestaurantModal.value = false
    }
  } else {
    showRestaurantModal.value = false
  }
}

const openAddDishModal = () => {
  editingDish.value = { id: null, name: '', categoryId: categories.value[0]?.id || '', price: '', stock: 999, status: 1, imageUrl: '', description: '' }
  showDishFormModal.value = true
}

const openEditDishModal = (dish) => {
  editingDish.value = { ...dish }
  showDishFormModal.value = true
}

const openAddUserModal = () => {
  editingUser.value = { id: null, username: '', password: '', phone: '', role: 0 }
  showUserFormModal.value = true
}

const openEditUserModal = (user) => {
  editingUser.value = { ...user, password: '' }
  showUserFormModal.value = true
}

const getImageUrl = (imageUrl) => {
  if (!imageUrl) return ''
  if (imageUrl.startsWith('data:') || imageUrl.startsWith('http')) return imageUrl
  return backendUrl + imageUrl
}

const handleImageUpload = async (event) => {
  const file = event.target.files[0]
  if (file) {
    const formData = new FormData()
    formData.append('file', file)
    
    try {
      const imageUrl = await request.post('/dish/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      editingDish.value.imageUrl = typeof imageUrl === 'string' ? imageUrl : ''
    } catch (error) {
      console.error('上传图片失败:', error)
    }
  }
}

const quickRestock = async (id) => {
  try {
    await request.put('/dish', { id, stock: 999 })
    fetchDishes()
  } catch (error) {
    console.error('补货失败:', error)
  }
}

const saveDish = async () => {
  if (!editingDish.value.name) {
    alert('请填写菜品名称')
    return
  }
  if (!editingDish.value.categoryId) {
    alert('请选择菜品分类')
    return
  }
  if (!editingDish.value.price || editingDish.value.price <= 0) {
    alert('请填写正确的菜品价格')
    return
  }
  
  try {
    const payload = {
      name: editingDish.value.name,
      categoryId: parseInt(editingDish.value.categoryId),
      price: parseFloat(editingDish.value.price),
      stock: parseInt(editingDish.value.stock) || 999,
      status: parseInt(editingDish.value.status) || 1,
      imageUrl: editingDish.value.imageUrl || '',
      description: editingDish.value.description || ''
    }
    
    if (editingDish.value.id) {
      payload.id = parseInt(editingDish.value.id)
      await request.put('/dish', payload)
    } else {
      await request.post('/dish', payload)
    }
    
    showDishFormModal.value = false
    fetchDishes()
    alert('保存成功')
  } catch (error) {
    alert('保存失败: ' + (error.message || '未知错误'))
    console.error('保存菜品失败:', error)
  }
}

const deleteDish = async (id) => {
  if (!confirm('确定删除该菜品？')) return
  try {
    await request.delete(`/dish/${id}`)
    fetchDishes()
  } catch (error) {
    console.error('删除菜品失败:', error)
  }
}

const saveUser = async () => {
  try {
    if (editingUser.value.id) {
      await request.put('/user', editingUser.value)
    } else {
      await request.post('/user', editingUser.value)
    }
    showUserFormModal.value = false
    fetchUsers()
  } catch (error) {
    console.error('保存用户失败:', error)
  }
}

const deleteUser = async (id) => {
  if (!confirm('确定删除该用户？')) return
  try {
    await request.delete(`/user/${id}`)
    fetchUsers()
  } catch (error) {
    console.error('删除用户失败:', error)
  }
}

const saveRestaurantInfo = async () => {
  try {
    await request.post('/restaurant/save', restaurantInfo.value)
    closeRestaurantModal()
  } catch (error) {
    console.error('保存餐厅信息失败:', error)
  }
}

const openOrderDetail = async (order) => {
  selectedOrder.value = order
  showOrderDetail.value = true
  
  try {
    const response = await request.get(`/orders/${order.orderNo}`)
    orderDetails.value = response.orderDetails || []
  } catch (error) {
    console.error('获取订单详情失败:', error)
    orderDetails.value = []
  }
  
  await nextTick()
  
  if (orderDetailRef.value) {
    const keyframes = [
      { transform: 'scale(0.9)', opacity: 0 },
      { transform: 'scale(1)', opacity: 1 }
    ]
    
    const options = {
      duration: 250,
      easing: 'cubic-bezier(0.2, 0.9, 0.4, 1.1)',
      fill: 'forwards'
    }
    
    orderDetailRef.value.animate(keyframes, options)
  }
}

const closeOrderDetail = async () => {
  if (orderDetailRef.value) {
    const keyframes = [
      { transform: 'scale(1)', opacity: 1 },
      { transform: 'scale(0.9)', opacity: 0 }
    ]
    
    const options = {
      duration: 200,
      easing: 'cubic-bezier(0.4, 0, 0.2, 1)',
      fill: 'forwards'
    }
    
    const animation = orderDetailRef.value.animate(keyframes, options)
    
    animation.onfinish = () => {
      showOrderDetail.value = false
      selectedOrder.value = null
      orderDetails.value = []
    }
  } else {
    showOrderDetail.value = false
    selectedOrder.value = null
    orderDetails.value = []
  }
}

const fetchOrders = async () => {
  try {
    // 管理员端获取所有订单，使用分页接口，第一页，获取100条记录
    const response = await request.get('/orders/page?page=1&size=100')
    // MyBatis-Plus 分页返回格式可能是 {records: [...]} 或直接是 [...]
    let ordersData = response
    if (response.records) {
      ordersData = response.records
    } else if (!Array.isArray(response)) {
      console.error('意外的响应格式:', response)
      orders.value = []
      return
    }
    
    const ordersWithDetails = await Promise.all(ordersData.map(async (order) => {
      if (!order.orderDetails || order.orderDetails.length === 0) {
        try {
          const detail = await request.get(`/orders/${order.orderNo}`)
          return { ...order, orderDetails: detail.orderDetails || [] }
        } catch {
          return { ...order, orderDetails: [] }
        }
      }
      return order
    }))
    orders.value = ordersWithDetails.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    console.log('获取订单成功，数量:', orders.value.length)
  } catch (error) {
    console.error('获取订单失败:', error)
    orders.value = []
  }
}

const fetchDishes = async () => {
  try {
    dishes.value = await request.get('/dish/list')
  } catch (error) {
    console.error('获取菜品失败:', error)
  }
}

const fetchUsers = async () => {
  try {
    users.value = await request.get('/user/list')
  } catch (error) {
    console.error('获取用户失败:', error)
  }
}

const fetchCategories = async () => {
  try {
    categories.value = await request.get('/category/list')
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

const fetchRestaurantInfo = async () => {
  try {
    const response = await request.get('/restaurant/info')
    if (response) restaurantInfo.value = response
  } catch (error) {
    console.error('获取餐厅信息失败:', error)
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  fetchOrders()
  fetchDishes()
  fetchCategories()
  fetchUsers()
  fetchRestaurantInfo()
})
</script>
