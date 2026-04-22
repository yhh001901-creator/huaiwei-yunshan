<template>
  <div class="flex h-screen overflow-hidden text-on-background">
    <aside class="h-full w-[15%] fixed left-0 top-0 bg-slate-100 dark:bg-slate-800 flex flex-col py-8 z-50">
      <div class="px-6 mb-10">
        <h1 class="text-xl font-bold text-teal-900 dark:text-teal-100 mb-1">淮味云膳</h1>
        <p class="text-xs text-on-surface-variant font-medium opacity-70">{{ userStore.roleName }}端</p>
      </div>
      <nav class="flex-1 space-y-2">
        <router-link 
          v-if="userStore.userRole === 1"
          to="/admin"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
        >
          <span class="w-6 h-6 flex items-center justify-center">📊</span>
          <span>数据概览</span>
        </router-link>
        <router-link 
          v-if="userStore.userRole === 2"
          to="/cashier"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
        >
          <span class="w-6 h-6 flex items-center justify-center">🍽️</span>
          <span>点餐收银</span>
        </router-link>
        <router-link 
          to="/pos"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/pos' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">🍽️</span>
          <span>点餐</span>
        </router-link>
        <router-link 
          to="/orders"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/orders' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">📋</span>
          <span>订单</span>
        </router-link>
        <router-link 
          v-if="userStore.userRole === 1"
          to="/settings"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/settings' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">⚙️</span>
          <span>设置</span>
        </router-link>
      </nav>
      <div class="px-6 pt-4 mt-auto">
        <div class="flex items-center gap-4 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors cursor-pointer" @click="handleLogout">
          <span class="w-6 h-6 flex items-center justify-center">🚪</span>
          <span>退出</span>
        </div>
      </div>
    </aside>
    
    <!-- Main Content Area (55% width, offset by sidebar) -->
    <main class="ml-[15%] w-[55%] h-full flex flex-col bg-surface overflow-hidden">
      <!-- TopNavBar -->
      <header class="h-20 w-full flex justify-between items-center px-8 border-b border-slate-100 dark:border-slate-800 bg-white dark:bg-slate-900">
        <nav class="flex gap-8 font-inter text-lg font-semibold">
          <a 
            v-for="category in categories" 
            :key="category"
            :class="[
              'cursor-pointer pb-2 transition-all duration-300',
              activeCategory === category ? 'text-teal-700 dark:text-teal-400 border-b-2 border-teal-700' : 'text-slate-500 dark:text-slate-400 hover:text-teal-600 dark:hover:text-teal-300'
            ]"
            @click="switchCategory(category)"
          >{{ category }}</a>
        </nav>
        <div class="flex items-center gap-4">
          <!-- AI Analysis Button -->
          <button 
            @click="openAiDialog"
            class="flex items-center gap-2 px-4 py-2 bg-gradient-to-r from-teal-500 to-emerald-500 text-white rounded-lg font-semibold hover:opacity-90 transition-opacity whitespace-nowrap"
          >
            <span>✨</span> AI智能分析
          </button>
          
          <div class="relative">
            <span class="bell-icon w-6 h-6 flex items-center justify-center text-teal-700 cursor-pointer hover:text-teal-900 dark:hover:text-teal-300 transition-colors" @click="toggleNotifications">🔔</span>
            <div v-if="showNotifications" class="notifications-panel absolute right-0 mt-2 w-48 bg-white dark:bg-slate-800 rounded-lg shadow-lg p-4 z-50">
              <h4 class="font-semibold mb-2">通知</h4>
              <div class="space-y-2">
                <div class="text-sm">暂无新通知</div>
              </div>
            </div>
          </div>
          <div class="relative">
            <span class="user-icon w-6 h-6 flex items-center justify-center text-teal-700 cursor-pointer hover:text-teal-900 dark:hover:text-teal-300 transition-colors" @click="toggleUserInfo">👤</span>
            <div v-if="showUserInfo" class="user-info-panel absolute right-0 mt-2 w-48 bg-white dark:bg-slate-800 rounded-lg shadow-lg p-4 z-50">
              <div class="flex items-center gap-3 mb-3">
                <div class="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
                  <span class="text-primary font-semibold">管</span>
                </div>
                <div>
                  <p class="font-semibold">管理员</p>
                  <p class="text-xs text-slate-500 dark:text-slate-400">admin@huaiwei.com</p>
                </div>
              </div>
              <div class="border-t border-slate-200 dark:border-slate-700 pt-3">
                <router-link to="/settings?tab=profile" class="block text-sm py-2 hover:bg-slate-100 dark:hover:bg-slate-700 rounded px-2 transition-colors">个人设置</router-link>
                <a href="#" class="block text-sm py-2 hover:bg-slate-100 dark:hover:bg-slate-700 rounded px-2 transition-colors">退出登录</a>
              </div>
            </div>
          </div>
        </div>
      </header>
      
      <!-- Product Grid Content -->
      <section class="flex-1 overflow-y-auto p-10 bg-surface">
        <div class="grid grid-cols-3 gap-6">
          <!-- Dish Cards -->
          <div 
            v-for="dish in dishList" 
            :key="dish.id"
            :class="[
              'bg-surface-container-lowest rounded-xl overflow-hidden flex flex-col group transition-all duration-200',
              dish.status === 1 ? 'cursor-pointer active:scale-95' : 'opacity-70 cursor-not-allowed'
            ]"
          >
            <div class="h-44 overflow-hidden relative">
              <img 
                :alt="dish.name" 
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                :src="(dish.imageUrl ? backendUrl + dish.imageUrl : 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20food%20placeholder&size=512x512')"
              />
              <div v-if="dish.status === 0" class="absolute inset-0 bg-black/40 flex items-center justify-center">
                <span class="text-white font-bold text-lg px-4 py-2 border-2 border-white rounded-lg">售罄</span>
              </div>
              <div class="absolute top-3 right-3">
                <span 
                  :class="[
                    'text-[10px] font-bold px-2 py-1 rounded-full uppercase tracking-tight',
                    dish.status === 1 ? 'bg-tertiary-container text-on-tertiary-container' : 'bg-error-container text-on-error-container'
                  ]"
                >
                  {{ dish.status === 1 ? '起售' : '停售' }}
                </span>
              </div>
            </div>
            <div class="p-5 flex flex-col gap-1">
              <h3 class="text-on-surface font-semibold text-lg line-clamp-1">{{ dish.name }}</h3>
              <div class="flex justify-between items-center mt-2">
                <span class="text-primary font-headline font-bold text-xl">¥ {{ dish.price }}</span>
                <div 
                  v-if="dish.status === 1"
                  class="bg-primary text-white p-2 rounded-full flex items-center justify-center cursor-pointer"
                  @click="addToCart(dish)"
                >
                  <span class="text-xl">+</span>
                </div>
                <div v-else class="bg-outline-variant text-white p-2 rounded-full flex items-center justify-center">
                  <span class="text-xl">✕</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
    
    <!-- NavigationDrawer / Checkout Column (30%) -->
    <aside class="h-full w-[30%] fixed right-0 top-0 bg-slate-50 dark:bg-slate-900 border-l border-slate-200 dark:border-slate-800 shadow-xl z-50 flex flex-col p-6">
      <header class="mb-8">
        <div class="flex justify-between items-center mb-2">
          <h2 class="text-lg font-bold text-slate-900 dark:text-slate-50">当前订单</h2>
          <button 
            @click="showTableModal = true"
            class="flex items-center gap-2 bg-teal-50 dark:bg-teal-900/30 px-4 py-1.5 rounded-lg border border-teal-200 dark:border-teal-700 hover:bg-teal-100 dark:hover:bg-teal-800/50 transition-colors active:scale-95 cursor-pointer"
          >
            <span class="text-teal-700 dark:text-teal-400 text-sm font-medium">桌号:</span>
            <span class="text-primary font-bold text-base">{{ currentTable || '未选桌台' }}</span>
          </button>
        </div>
        <p class="text-sm text-on-surface-variant">{{ cartItems.length }} 件商品在购物车中</p>
      </header>
      
      <!-- Cart Items List -->
      <div class="flex-1 overflow-y-auto space-y-4 pr-2">
        <div 
          v-for="(item, index) in cartItems" 
          :key="index"
          class="bg-surface-container-lowest rounded-xl p-4 flex items-center gap-4 transition-all hover:brightness-110"
        >
          <img 
            :alt="item.name" 
            class="w-14 h-14 rounded-lg object-cover"
            :src="(item.imageUrl ? backendUrl + item.imageUrl : 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20food%20placeholder&size=512x512')"
          />
          <div class="flex-1">
            <h4 class="font-semibold text-on-surface text-base">{{ item.name }}</h4>
            <div class="flex items-center gap-2 mt-1">
              <button 
                class="bg-surface-container p-1 rounded"
                @click="decreaseQuantity(index)"
              >
                <span class="text-sm">−</span>
              </button>
              <span class="text-sm text-on-surface-variant">{{ item.quantity }}</span>
              <button 
                class="bg-surface-container p-1 rounded"
                @click="increaseQuantity(index)"
              >
                <span class="text-sm">+</span>
              </button>
            </div>
          </div>
          <span class="font-bold text-primary">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
        </div>
        <div v-if="cartItems.length === 0" class="text-center py-10 text-on-surface-variant">
          购物车为空
        </div>
        <div v-if="showCheckoutSuccess" class="fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg z-50 animate-fade-in-out">
          结账成功
        </div>
      </div>
      
      <!-- Order Summary & CTA -->
      <footer class="mt-auto pt-6 border-t border-slate-200 dark:border-slate-800">
        <div class="space-y-3 mb-6">
          <div class="flex justify-between text-on-surface-variant text-base">
            <span>小计</span>
            <span>¥ {{ totalPrice.toFixed(2) }}</span>
          </div>
          <div class="flex justify-between text-on-surface-variant text-base">
            <span>折扣</span>
            <span class="text-secondary">- ¥ 0.00</span>
          </div>
          <div class="flex justify-between items-end pt-2">
            <span class="text-on-surface font-bold text-xl">总计</span>
            <span class="text-primary font-headline font-extrabold text-4xl">¥ {{ totalPrice.toFixed(2) }}</span>
          </div>
        </div>
        <button 
          class="w-full bg-green-700 hover:bg-green-800 text-white font-bold rounded-xl py-6 text-2xl uppercase tracking-wider flex items-center justify-center gap-3 transition-all active:scale-[0.98]"
          :disabled="cartItems.length === 0"
          @click="checkout"
        >
          <span class="text-3xl">💳</span>
          结账
        </button>
      </footer>
    </aside>
    
    <!-- AI Analysis Dialog -->
    <div v-if="showAiDialog" class="fixed inset-0 bg-black/60 z-[100] flex items-center justify-center backdrop-blur-sm transition-all duration-300">
      <div class="bg-white dark:bg-slate-900 w-[700px] max-h-[85vh] rounded-2xl shadow-2xl flex flex-col overflow-hidden">
        
        <!-- Modal Header -->
        <div class="px-6 py-4 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center bg-teal-50 dark:bg-teal-900/30">
          <div class="flex items-center gap-3">
            <span class="text-2xl">✨</span>
            <h2 class="text-xl font-bold text-teal-900 dark:text-teal-100 font-manrope">DeepSeek 智能订单分析</h2>
          </div>
          <button @click="showAiDialog = false" class="text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 text-3xl leading-none">&times;</button>
        </div>
        
        <!-- Modal Body -->
        <div class="flex-1 overflow-y-auto p-6 flex flex-col gap-6">
          
          <!-- API Key Settings -->
          <div class="bg-slate-50 dark:bg-slate-800/50 p-4 rounded-xl border border-slate-200 dark:border-slate-700">
            <h3 class="text-sm font-semibold text-slate-700 dark:text-slate-300 mb-3 flex items-center gap-2">
              <span>🔑</span> API Key 配置
            </h3>
            <div class="flex gap-3">
              <input 
                v-model="aiApiKey" 
                type="password" 
                placeholder="在此输入您的 DeepSeek R1 API Key" 
                class="flex-1 px-4 py-2 bg-white dark:bg-slate-900 border border-slate-300 dark:border-slate-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500 text-sm"
              />
              <button 
                @click="saveApiKey" 
                :disabled="isSavingKey"
                class="px-5 py-2 bg-slate-800 dark:bg-slate-700 text-white rounded-lg font-medium hover:bg-slate-700 dark:hover:bg-slate-600 transition-colors disabled:opacity-50 text-sm whitespace-nowrap"
              >
                {{ isSavingKey ? '保存中...' : '保存配置' }}
              </button>
            </div>
            <p v-if="saveKeySuccess" class="text-green-600 text-xs mt-2">API Key 保存成功！</p>
          </div>
          
          <!-- Analysis Content Area -->
          <div class="flex-1 min-h-[300px] bg-slate-50 dark:bg-slate-800/30 rounded-xl border border-slate-200 dark:border-slate-700 p-5 flex flex-col relative">
            <div v-if="!aiResult && !isAnalyzing" class="m-auto text-center text-slate-400">
              <span class="text-4xl block mb-3">🤖</span>
              <p>点击下方按钮，基于最新订单数据生成经营分析报告</p>
            </div>
            
            <div v-else-if="isAnalyzing" class="m-auto text-center flex flex-col items-center">
              <div class="w-10 h-10 border-4 border-teal-200 border-t-teal-600 rounded-full animate-spin mb-4"></div>
              <p class="text-teal-700 dark:text-teal-400 font-medium animate-pulse">DeepSeek 模型正在思考并分析数据，请稍候...</p>
            </div>
            
            <div v-else class="prose prose-sm dark:prose-invert max-w-none ai-result-content">
              <!-- Simple formatting for markdown via v-html -->
              <div v-html="formattedAiResult"></div>
            </div>
          </div>
        </div>
        
        <!-- Modal Footer -->
        <div class="px-6 py-4 border-t border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-900 flex justify-end">
          <button 
            @click="startAnalysis" 
            :disabled="isAnalyzing || !aiApiKey"
            class="px-6 py-2.5 bg-gradient-to-r from-teal-600 to-emerald-600 text-white rounded-xl font-bold hover:shadow-lg hover:shadow-teal-500/30 transition-all active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
          >
            <span v-if="!isAnalyzing">🚀 立即开始分析</span>
            <span v-else>分析中...</span>
          </button>
        </div>
        
      </div>
    </div>
    <!-- Table Selection Modal -->
    <div v-if="showTableModal" class="fixed inset-0 bg-black/60 z-[100] flex items-center justify-center backdrop-blur-sm transition-all duration-300">
      <div class="bg-white dark:bg-slate-900 w-[600px] rounded-2xl shadow-2xl flex flex-col overflow-hidden">
        <div class="px-6 py-4 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
          <h2 class="text-xl font-bold text-slate-800 dark:text-slate-100">选择当前就餐桌台</h2>
          <button @click="showTableModal = false" class="text-slate-400 hover:text-slate-600 text-3xl leading-none">&times;</button>
        </div>
        
        <div class="p-6 overflow-y-auto max-h-[60vh] flex flex-col gap-6">
          <div v-for="area in tableAreas" :key="area.name">
            <h3 class="text-base font-semibold text-slate-500 mb-3 border-b border-slate-100 dark:border-slate-800 pb-2">{{ area.name }}</h3>
            <div class="grid grid-cols-5 gap-3">
              <button 
                v-for="table in area.tables" 
                :key="table"
                @click="selectTable(table)"
                :class="[
                  'py-3 rounded-xl font-bold text-lg transition-all active:scale-95',
                  currentTable === table 
                    ? 'bg-teal-600 text-white shadow-md shadow-teal-500/30' 
                    : 'bg-slate-100 text-slate-700 hover:bg-teal-100 hover:text-teal-800 dark:bg-slate-800 dark:text-slate-300 dark:hover:bg-teal-900/40'
                ]"
              >
                {{ table }}
              </button>
            </div>
          </div>
        </div>
        
        <div class="p-4 border-t border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-900 flex justify-between items-center">
          <span class="text-sm text-slate-500">提示：如果桌台已被占用，系统将提示。</span>
          <button @click="showTableModal = false" class="px-5 py-2 bg-slate-200 hover:bg-slate-300 text-slate-800 rounded-lg font-medium transition-colors">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { listDishes } from '../api/dish'
import { submitOrder } from '../api/order'
import { getSystemConfig, saveSystemConfig, analyzeOrders } from '../api/ai'
import request, { imageBaseURL } from '../utils/request'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const backendUrl = imageBaseURL // 使用imageBaseURL，自动适配当前环境

// 响应式数据
const dishList = ref([])
const cartItems = ref([])
const showCheckoutSuccess = ref(false)
const showNotifications = ref(false)
const showUserInfo = ref(false)
const activeCategory = ref('')
const categories = ref([])
const categoryMap = ref({})
const currentTable = ref('') // 动态桌号，默认空
const showTableModal = ref(false)

// 桌台区域定义
const tableAreas = ref([
  { name: 'A区 (大厅)', tables: ['A01', 'A02', 'A03', 'A04', 'A05', 'A06', 'A07', 'A08', 'A09', 'A10'] },
  { name: 'B区 (靠窗)', tables: ['B01', 'B02', 'B03', 'B04', 'B05'] },
  { name: 'VIP包厢', tables: ['V01', 'V02', 'V03', 'V05', 'V06', 'V08'] }
])

// AI 智能分析相关状态
const showAiDialog = ref(false)
const aiApiKey = ref('')
const isSavingKey = ref(false)
const saveKeySuccess = ref(false)
const isAnalyzing = ref(false)
const aiResult = ref('')
const systemConfigRecord = ref(null) // 保存原配置对象以供更新

// 切换通知面板
const toggleNotifications = () => {
  showNotifications.value = !showNotifications.value
  // 关闭用户信息面板
  if (showUserInfo.value) {
    showUserInfo.value = false
  }
}

// 切换用户信息面板
const toggleUserInfo = () => {
  showUserInfo.value = !showUserInfo.value
  // 关闭通知面板
  if (showNotifications.value) {
    showNotifications.value = false
  }
}

// 点击页面其他地方关闭面板
const handleClickOutside = (event) => {
  const notificationsPanel = document.querySelector('.notifications-panel')
  const userInfoPanel = document.querySelector('.user-info-panel')
  const bellIcon = document.querySelector('.bell-icon')
  const userIcon = document.querySelector('.user-icon')
  
  if (notificationsPanel && !notificationsPanel.contains(event.target) && !bellIcon.contains(event.target)) {
    showNotifications.value = false
  }
  
  if (userInfoPanel && !userInfoPanel.contains(event.target) && !userIcon.contains(event.target)) {
    showUserInfo.value = false
  }
}

// 计算属性：总价格
const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => {
    return total + (item.price * item.quantity)
  }, 0)
})

// 获取分类列表
const fetchCategories = async () => {
  try {
    console.log('开始获取分类列表...')
    console.log('API 地址:', request.defaults.baseURL)
    const response = await request.get('/category/list')
    console.log('获取分类列表成功:', response)
    const categoryList = response
    categories.value = categoryList.map(cat => cat.name)
    // 创建分类名称到ID的映射
    categoryMap.value = {}
    categoryList.forEach(cat => {
      categoryMap.value[cat.name] = cat.id
    })
    // 设置默认激活的分类
    if (categories.value.length > 0) {
      activeCategory.value = categories.value[0]
      await fetchDishesByCategory(activeCategory.value)
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    console.error('错误详情:', error.message)
    console.error('错误响应:', error.response)
  }
}

// 切换分类
const switchCategory = async (category) => {
  activeCategory.value = category
  await fetchDishesByCategory(category)
}

// 根据分类获取菜品
const fetchDishesByCategory = async (category) => {
  try {
    console.log('开始获取菜品列表...')
    const dishes = await listDishes()
    console.log('获取菜品列表成功:', dishes)
    // 根据分类过滤菜品
    const categoryId = categoryMap.value[category]
    if (categoryId) {
      dishList.value = dishes.filter(dish => dish.categoryId === categoryId)
    } else {
      // 如果找不到分类ID，显示所有菜品
      dishList.value = dishes
    }
  } catch (error) {
    console.error('获取菜品列表失败:', error)
    console.error('错误详情:', error.message)
    console.error('错误响应:', error.response)
  }
}

// 生命周期钩子：组件挂载时获取分类和菜品列表
onMounted(async () => {
  console.log('组件挂载，开始初始化数据...')
  console.log('环境变量 DEV:', import.meta.env.DEV)
  console.log('API 基础地址:', request.defaults.baseURL)
  try {
    await fetchCategories()
    console.log('初始化数据成功')
  } catch (error) {
    console.error('初始化数据失败:', error)
    console.error('错误详情:', error.message)
    console.error('错误响应:', error.response)
    // 可以在这里添加一个页面内的错误提示
  }
  
  // 添加全局点击事件监听器
  document.addEventListener('click', handleClickOutside)
})

// 生命周期钩子：组件卸载时移除事件监听器
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

// 选中桌台
const selectTable = (table) => {
  currentTable.value = table
  showTableModal.value = false
}

// === AI 智能分析功能实现 ===

// 格式化AI返回的 Markdown 文本供前端显示
const formattedAiResult = computed(() => {
  if (!aiResult.value) return ''
  let html = aiResult.value
    .replace(/### (.*?)\n/g, '<h3 class="text-lg font-bold text-teal-800 dark:text-teal-300 mt-4 mb-2">$1</h3>')
    .replace(/## (.*?)\n/g, '<h2 class="text-xl font-bold text-teal-900 dark:text-teal-200 mt-5 mb-3">$1</h2>')
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\* (.*?)\n/g, '<li class="ml-4 list-disc">$1</li>')
    .replace(/\n\n/g, '</p><p class="mb-2">')
    .replace(/\n/g, '<br/>');
  return `<p class="mb-2">${html}</p>`
})

// 打开弹窗并获取已有配置
const openAiDialog = async () => {
  showAiDialog.value = true
  aiResult.value = ''
  saveKeySuccess.value = false
  
  try {
    const res = await getSystemConfig()
    systemConfigRecord.value = res
    aiApiKey.value = systemConfigRecord.value?.aiApiKey || ''
  } catch (error) {
    console.error('获取系统配置失败:', error)
  }
}

// 保存 API Key
const saveApiKey = async () => {
  if (!aiApiKey.value) return
  isSavingKey.value = true
  saveKeySuccess.value = false
  
  try {
    const configToSave = { ...systemConfigRecord.value, aiApiKey: aiApiKey.value }
    await saveSystemConfig(configToSave)
    saveKeySuccess.value = true
    systemConfigRecord.value = configToSave
    setTimeout(() => { saveKeySuccess.value = false }, 3000)
  } catch (error) {
    console.error('保存 API Key 失败:', error)
  } finally {
    isSavingKey.value = false
  }
}

// 开始分析
const startAnalysis = async () => {
  if (!aiApiKey.value) {
    alert("请先配置并保存 API Key");
    return;
  }
  isAnalyzing.value = true
  aiResult.value = ''
  
  try {
    const res = await analyzeOrders()
    aiResult.value = res
  } catch (error) {
    console.error('分析请求失败:', error)
    aiResult.value = '请求失败，请检查网络或后端服务日志。错误信息：' + error.message
  } finally {
    isAnalyzing.value = false
  }
}

// 添加菜品到购物车
const addToCart = (dish) => {
  // 检查购物车中是否已有该菜品
  const existingItemIndex = cartItems.value.findIndex(item => item.id === dish.id)
  
  if (existingItemIndex !== -1) {
    // 如果已有，增加数量
    cartItems.value[existingItemIndex].quantity++
  } else {
    // 如果没有，添加新项
    cartItems.value.push({
      ...dish,
      quantity: 1
    })
  }
  
  // 移除ElMessage提示
}

// 增加数量
const increaseQuantity = (index) => {
  cartItems.value[index].quantity++
}

// 减少数量
const decreaseQuantity = (index) => {
  if (cartItems.value[index].quantity > 1) {
    cartItems.value[index].quantity--
  } else {
    // 如果数量为1，从购物车中移除
    cartItems.value.splice(index, 1)
  }
}

// 结账
const checkout = async () => {
  if (cartItems.value.length === 0) {
    return
  }
  
  try {
      if (!currentTable.value.trim()) {
        alert("请输入桌号后再结账！");
        return;
      }
      
      const orderData = {
        orderNo: 'ORD' + Date.now(),
        userId: userStore.userInfo?.userId || 1,
        totalAmount: totalPrice.value,
        status: 1,
        tableNo: currentTable.value.trim(),
        orderDetails: cartItems.value.map(item => ({
        dishId: item.id,
        dishName: item.name,
        quantity: item.quantity,
        unitPrice: item.price
      }))
    }
    
    await submitOrder(orderData)
    
    showCheckoutSuccess.value = true
    
    setTimeout(() => {
      showCheckoutSuccess.value = false
    }, 2000)
    
    cartItems.value = []
  } catch (error) {
    console.error('提交订单失败:', error)
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.material-symbols-outlined {
  font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24;
}

/* 导入Google字体 */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Manrope:wght@500;600;700;800&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap');

body {
  font-family: 'Inter', sans-serif;
}

h1, h2, .total-price {
  font-family: 'Manrope', sans-serif;
}

@keyframes fadeInOut {
  0% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(0.8);
  }
  10% {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1);
  }
  90% {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1);
  }
  100% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(0.8);
  }
}

.animate-fade-in-out {
  animation: fadeInOut 2s ease-in-out;
}

/* 响应式设计 */
/* 平板设备 (768px - 1023px) */
@media (max-width: 1023px) {
  /* 调整侧边栏宽度 */
  aside:first-child {
    width: 20%;
  }
  
  /* 调整主内容区宽度 */
  main {
    width: 50%;
    margin-left: 20%;
  }
  
  /* 调整购物车宽度 */
  aside:last-child {
    width: 30%;
  }
  
  /* 调整菜品网格 */
  section > div {
    grid-template-columns: repeat(2, 1fr);
    gap: 4;
  }
  
  /* 调整顶部导航 */
  header nav {
    gap: 4;
  }
  
  /* 调整AI按钮 */
  header button {
    padding: 1px 3px;
    font-size: 12px;
  }
}

/* 手机设备 (小于768px) */
@media (max-width: 767px) {
  /* 改为单栏布局 */
  .flex.h-screen {
    flex-direction: column;
  }
  
  /* 侧边栏改为顶部导航栏 */
  aside:first-child {
    width: 100%;
    height: auto;
    position: relative;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;
  }
  
  /* 侧边栏内容调整 */
  aside:first-child div:first-child {
    margin: 0;
  }
  
  aside:first-child h1 {
    font-size: 16px;
  }
  
  aside:first-child p {
    display: none;
  }
  
  /* 导航链接改为横向排列 */
  aside:first-child nav {
    display: flex;
    flex-direction: row;
    gap: 10px;
  }
  
  aside:first-child nav a {
    padding: 8px 12px;
    font-size: 12px;
  }
  
  aside:first-child nav span {
    display: none;
  }
  
  /* 退出按钮 */
  aside:first-child div:last-child {
    display: none;
  }
  
  /* 主内容区 */
  main {
    width: 100%;
    margin-left: 0;
    margin-top: 0;
  }
  
  /* 顶部导航调整 */
  main header {
    padding: 0 16px;
  }
  
  /* 分类导航改为可滚动 */
  main header nav {
    overflow-x: auto;
    white-space: nowrap;
    padding-bottom: 8px;
    gap: 16px;
  }
  
  /* AI按钮隐藏 */
  main header button {
    display: none;
  }
  
  /* 菜品网格调整 */
  main section {
    padding: 16px;
  }
  
  main section > div {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  /* 菜品卡片调整 */
  main section > div > div {
    min-height: 200px;
  }
  
  /* 图片高度调整 */
  main section > div > div div:first-child {
    height: 120px;
  }
  
  /* 购物车改为底部固定 */
  aside:last-child {
    width: 100%;
    height: 30vh;
    position: fixed;
    bottom: 0;
    top: auto;
  }
  
  /* 购物车头部 */
  aside:last-child header {
    margin-bottom: 10px;
  }
  
  /* 购物车列表 */
  aside:last-child div:nth-child(2) {
    max-height: 120px;
  }
  
  /* 购物车底部 */
  aside:last-child footer {
    padding-top: 10px;
  }
  
  /* 结账按钮 */
  aside:last-child footer button {
    padding: 12px;
    font-size: 16px;
  }
  
  /* 模态框调整 */
  .fixed.inset-0 > div {
    width: 90%;
    max-width: 500px;
  }
  
  /* 桌台选择网格调整 */
  .grid.grid-cols-5 {
    grid-template-columns: repeat(3, 1fr);
  }
}

/* 小屏幕手机 (小于480px) */
@media (max-width: 479px) {
  /* 菜品网格改为单列 */
  main section > div {
    grid-template-columns: 1fr;
  }
  
  /* 顶部导航简化 */
  aside:first-child nav a {
    padding: 6px 10px;
    font-size: 11px;
  }
  
  /* 购物车高度调整 */
  aside:last-child {
    height: 25vh;
  }
}
</style>