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
          <h2 class="text-2xl font-bold text-on-surface">待制作订单</h2>
          <p class="text-sm text-on-surface-variant mt-1">查看并处理待制作的订单</p>
        </div>
        <button
          @click="fetchOrders"
          class="bg-teal-600 hover:bg-teal-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors"
        >
          刷新列表
        </button>
      </header>

      <div class="grid grid-cols-3 gap-6">
        <div class="space-y-4">
          <h3 class="text-lg font-bold text-yellow-600 flex items-center gap-2">
            <span class="w-3 h-3 bg-yellow-500 rounded-full animate-pulse"></span>
            待接单 ({{ pendingOrders.length }})
          </h3>
          <div class="space-y-4">
            <div
              v-for="order in pendingOrders"
              :key="order.id"
              class="bg-white rounded-xl shadow-sm p-4 border-l-4 border-yellow-500"
            >
              <div class="flex justify-between items-start mb-3">
                <div>
                  <p class="font-bold text-slate-800">{{ order.tableNo || '外带' }}</p>
                  <p class="text-xs text-slate-500">{{ order.orderNo }}</p>
                </div>
                <span class="px-2 py-1 bg-yellow-100 text-yellow-800 rounded-full text-xs font-medium">
                  待接单
                </span>
              </div>
              <div class="space-y-1 mb-3">
                <div v-for="item in order.orderDetails" :key="item.id" class="flex justify-between text-sm">
                  <span>{{ item.dishName }}</span>
                  <span class="text-slate-500">x{{ item.quantity }}</span>
                </div>
              </div>
              <div class="flex justify-between items-center pt-3 border-t border-slate-100">
                <span class="text-xs text-slate-400">{{ order.createTime }}</span>
                <button
                  @click="acceptOrder(order)"
                  class="px-4 py-2 bg-teal-600 text-white rounded-lg text-sm font-medium hover:bg-teal-700 transition-colors"
                >
                  接单
                </button>
              </div>
            </div>
            <div v-if="pendingOrders.length === 0" class="text-center py-8 text-slate-400">
              暂无待接单订单
            </div>
          </div>
        </div>

        <div class="space-y-4">
          <h3 class="text-lg font-bold text-blue-600 flex items-center gap-2">
            <span class="w-3 h-3 bg-blue-500 rounded-full animate-pulse"></span>
            制作中 ({{ cookingOrders.length }})
          </h3>
          <div class="space-y-4">
            <div
              v-for="order in cookingOrders"
              :key="order.id"
              class="bg-white rounded-xl shadow-sm p-4 border-l-4 border-blue-500"
            >
              <div class="flex justify-between items-start mb-3">
                <div>
                  <p class="font-bold text-slate-800">{{ order.tableNo || '外带' }}</p>
                  <p class="text-xs text-slate-500">{{ order.orderNo }}</p>
                </div>
                <span class="px-2 py-1 bg-blue-100 text-blue-800 rounded-full text-xs font-medium">
                  制作中
                </span>
              </div>
              <div class="space-y-1 mb-3">
                <div v-for="item in order.orderDetails" :key="item.id" class="flex justify-between text-sm">
                  <span>{{ item.dishName }}</span>
                  <span class="text-slate-500">x{{ item.quantity }}</span>
                </div>
              </div>
              <div class="flex justify-between items-center pt-3 border-t border-slate-100">
                <span class="text-xs text-slate-400">{{ order.createTime }}</span>
                <button
                  @click="completeOrder(order)"
                  class="px-4 py-2 bg-green-600 text-white rounded-lg text-sm font-medium hover:bg-green-700 transition-colors"
                >
                  完成
                </button>
              </div>
            </div>
            <div v-if="cookingOrders.length === 0" class="text-center py-8 text-slate-400">
              暂无制作中订单
            </div>
          </div>
        </div>

        <div class="space-y-4">
          <h3 class="text-lg font-bold text-green-600 flex items-center gap-2">
            <span class="w-3 h-3 bg-green-500 rounded-full"></span>
            已完成 ({{ completedOrders.length }})
          </h3>
          <div class="space-y-4 max-h-[calc(100vh-200px)] overflow-y-auto">
            <div
              v-for="order in completedOrders"
              :key="order.id"
              class="bg-white rounded-xl shadow-sm p-4 border-l-4 border-green-500 opacity-80"
            >
              <div class="flex justify-between items-start mb-3">
                <div>
                  <p class="font-bold text-slate-800">{{ order.tableNo || '外带' }}</p>
                  <p class="text-xs text-slate-500">{{ order.orderNo }}</p>
                </div>
                <span class="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs font-medium">
                  已完成
                </span>
              </div>
              <div class="space-y-1 mb-3">
                <div v-for="item in order.orderDetails" :key="item.id" class="flex justify-between text-sm">
                  <span>{{ item.dishName }}</span>
                  <span class="text-slate-500">x{{ item.quantity }}</span>
                </div>
              </div>
              <div class="flex justify-between items-center pt-3 border-t border-slate-100">
                <span class="text-xs text-slate-400">{{ order.createTime }}</span>
                <span class="text-green-600 font-bold">¥{{ parseFloat(order.totalAmount).toFixed(2) }}</span>
              </div>
            </div>
            <div v-if="completedOrders.length === 0" class="text-center py-8 text-slate-400">
              暂无已完成订单
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import request from '../utils/request'

const router = useRouter()
const userStore = useUserStore()

const orders = ref([])
let refreshInterval = null

const pendingOrders = computed(() => {
  return orders.value.filter(o => o.status === 1)
})

const cookingOrders = computed(() => {
  return orders.value.filter(o => o.status === 2)
})

const completedOrders = computed(() => {
  return orders.value.filter(o => o.status === 3).slice(0, 10)
})

const fetchOrders = async () => {
  try {
    // 厨房端获取所有订单，使用分页接口，第一页，获取100条记录
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
    console.log('获取厨房订单成功，数量:', orders.value.length)
  } catch (error) {
    console.error('获取订单失败:', error)
    orders.value = []
  }
}

const acceptOrder = async (order) => {
  try {
    await request.put(`/orders/status?orderNo=${order.orderNo}&status=2`)
    order.status = 2
  } catch (error) {
    console.error('接单失败:', error)
    alert('接单失败，请重试')
  }
}

const completeOrder = async (order) => {
  try {
    await request.put(`/orders/status?orderNo=${order.orderNo}&status=3`)
    order.status = 3
  } catch (error) {
    console.error('完成订单失败:', error)
    alert('完成订单失败，请重试')
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  fetchOrders()
  refreshInterval = setInterval(fetchOrders, 30000)
})

onUnmounted(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})
</script>
