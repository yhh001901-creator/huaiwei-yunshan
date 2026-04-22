<template>
  <div class="flex h-screen overflow-hidden text-on-background">
    <aside class="h-full w-[15%] fixed left-0 top-0 bg-slate-100 dark:bg-slate-800 flex flex-col py-8 z-50">
      <div class="px-6 mb-10">
        <h1 class="text-xl font-bold text-teal-900 dark:text-teal-100 mb-1">淮味云膳</h1>
        <p class="text-xs text-on-surface-variant font-medium opacity-70">后厨端</p>
      </div>
      <nav class="flex-1 space-y-2">
        <router-link 
          to="/kitchen"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/kitchen' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">🍳</span>
          <span>待制作</span>
        </router-link>
        <router-link 
          to="/kitchen/inventory"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/kitchen/inventory' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">📦</span>
          <span>库存</span>
        </router-link>
        <router-link 
          to="/orders"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/orders' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">📋</span>
          <span>全部订单</span>
        </router-link>
      </nav>
      <div class="px-6 mt-auto">
        <div class="flex items-center gap-3 py-3 px-4 bg-surface-container rounded-xl">
          <div class="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
            <span class="text-primary font-semibold">{{ userStore.username?.charAt(0) || '厨' }}</span>
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
      <header class="mb-8 flex justify-between items-center">
        <div>
          <h2 class="text-2xl font-bold text-on-surface">菜品库存</h2>
          <p class="text-sm text-on-surface-variant mt-1">实时监控各菜品库存数量</p>
        </div>
        <div class="flex gap-3">
          <button
            @click="fetchInventory"
            class="bg-teal-600 hover:bg-teal-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors"
          >
            刷新列表
          </button>
          <div class="flex items-center gap-2 text-sm text-slate-500">
            <span class="w-2 h-2 bg-green-500 rounded-full animate-pulse"></span>
            <span>自动刷新中 (30s)</span>
          </div>
        </div>
      </header>

      <div class="grid grid-cols-4 gap-6">
        <div
          v-for="dish in inventory"
          :key="dish.id"
          class="bg-white rounded-xl shadow-sm p-5 border border-slate-200 hover:shadow-md transition-shadow"
          :class="{
            'border-l-4 border-l-red-500': dish.stock <= 10,
            'border-l-4 border-l-yellow-500': dish.stock > 10 && dish.stock <= 50,
            'border-l-4 border-l-green-500': dish.stock > 50
          }"
        >
          <div class="flex justify-between items-start mb-3">
            <div class="flex-1">
              <p class="font-bold text-slate-800 text-lg">{{ dish.name }}</p>
              <p class="text-sm text-slate-500 mt-1">¥{{ parseFloat(dish.price).toFixed(2) }}</p>
            </div>
            <span 
              class="px-3 py-1 rounded-full text-xs font-medium"
              :class="{
                'bg-red-100 text-red-800': dish.stock <= 10,
                'bg-yellow-100 text-yellow-800': dish.stock > 10 && dish.stock <= 50,
                'bg-green-100 text-green-800': dish.stock > 50
              }"
            >
              {{ dish.stock <= 10 ? '库存不足' : dish.stock <= 50 ? '库存偏低' : '库存充足' }}
            </span>
          </div>
          
          <div class="mt-4">
            <div class="flex items-end justify-between mb-2">
              <span class="text-sm text-slate-600">库存数量</span>
              <span 
                class="text-2xl font-bold"
                :class="{
                  'text-red-600': dish.stock <= 10,
                  'text-yellow-600': dish.stock > 10 && dish.stock <= 50,
                  'text-green-600': dish.stock > 50
                }"
              >
                {{ dish.stock }}
              </span>
            </div>
            <div class="w-full bg-slate-200 rounded-full h-2 overflow-hidden">
              <div 
                class="h-full rounded-full transition-all duration-500"
                :class="{
                  'bg-red-500': dish.stock <= 10,
                  'bg-yellow-500': dish.stock > 10 && dish.stock <= 50,
                  'bg-green-500': dish.stock > 50
                }"
                :style="{ width: `${Math.min((dish.stock / 100) * 100, 100)}%` }"
              ></div>
            </div>
          </div>

          <p v-if="dish.description" class="text-xs text-slate-400 mt-3 line-clamp-2">
            {{ dish.description }}
          </p>
        </div>
      </div>

      <div v-if="inventory.length === 0" class="text-center py-20 text-slate-400">
        暂无菜品库存数据
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import request from '../utils/request'

const router = useRouter()
const userStore = useUserStore()

const inventory = ref([])
let refreshInterval = null

const fetchInventory = async () => {
  try {
    const response = await request.get('/dish/list')
    inventory.value = response || []
    console.log('获取菜品库存成功，数量:', inventory.value.length)
  } catch (error) {
    console.error('获取库存失败:', error)
    inventory.value = []
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  fetchInventory()
  refreshInterval = setInterval(fetchInventory, 30000)
})

onUnmounted(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
