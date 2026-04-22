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
          v-if="userStore.userRole === 3"
          to="/kitchen"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
        >
          <span class="w-6 h-6 flex items-center justify-center">🍳</span>
          <span>待制作</span>
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
      <div class="px-6 mt-auto">
        <div class="flex items-center gap-3 py-3 px-4 bg-surface-container rounded-xl">
          <div class="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
            <span class="text-primary font-semibold">{{ userStore.username?.charAt(0) || '用' }}</span>
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
        <h2 class="text-2xl font-bold text-on-surface">订单管理</h2>
        <p class="text-sm text-on-surface-variant mt-1">查看和管理所有订单</p>
      </header>

      <!-- Orders Table -->
      <div class="bg-surface rounded-xl shadow-sm p-6">
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-lg font-semibold text-on-surface">订单列表</h3>
          <div class="flex gap-3">
            <button 
              v-if="userStore.userRole === 1"
              class="bg-teal-600 hover:bg-teal-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors shadow-sm shadow-teal-500/30 flex items-center gap-1"
              @click="openOverviewModal"
            >
              <span>📊</span> 订单概览
            </button>
            <button 
              class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors"
              @click="fetchOrders"
            >
              刷新列表
            </button>
          </div>
        </div>
        
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead>
              <tr class="border-b border-slate-200 dark:border-slate-700">
                <th class="text-left py-3 px-4 text-sm font-medium text-on-surface-variant">订单号</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-on-surface-variant">用户ID</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-on-surface-variant">总金额</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-on-surface-variant">状态</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-on-surface-variant">下单时间</th>
                <th class="text-left py-3 px-4 text-sm font-medium text-on-surface-variant">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id" class="border-b border-slate-200 dark:border-slate-700 hover:bg-slate-100 dark:hover:bg-slate-800">
                <td class="py-3 px-4 text-sm text-on-surface">{{ order.orderNo }}</td>
                <td class="py-3 px-4 text-sm text-on-surface">{{ order.userId }}</td>
                <td class="py-3 px-4 text-sm text-on-surface">¥{{ parseFloat(order.totalAmount).toFixed(2) }}</td>
                <td class="py-3 px-4 text-sm">
                  <span :class="{
                    'px-2 py-1 rounded-full text-xs font-medium': true,
                    'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400': order.status === 3,
                    'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/30 dark:text-yellow-400': order.status === 1 || order.status === 2,
                    'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400': order.status === 4,
                    'bg-gray-100 text-gray-800 dark:bg-gray-900/30 dark:text-gray-400': order.status === 0
                  }">
                    {{ getStatusText(order.status) }}
                  </span>
                </td>
                <td class="py-3 px-4 text-sm text-on-surface-variant">{{ order.createTime }}</td>
                <td class="py-3 px-4">
                  <button 
                    @click="viewOrderDetails(order)"
                    class="text-primary hover:text-primary/80 text-sm font-medium transition-colors active:scale-95"
                  >
                    查看详情
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- Empty State -->
        <div v-if="orders.length === 0" class="text-center py-12">
          <div class="w-16 h-16 mx-auto mb-4 bg-surface-container rounded-full flex items-center justify-center">
            <span class="text-2xl">📋</span>
          </div>
          <h4 class="text-lg font-semibold text-on-surface mb-2">暂无订单</h4>
          <p class="text-sm text-on-surface-variant">当有新订单时，会显示在这里</p>
        </div>
      </div>
    </main>

    <!-- Order Details Modal -->
    <div v-if="showOrderDetailsModal" class="fixed inset-0 bg-black/60 z-[100] flex items-center justify-center backdrop-blur-sm transition-all duration-300">
      <div class="bg-white dark:bg-slate-900 w-[700px] rounded-2xl shadow-2xl flex flex-col overflow-hidden">
        <div class="px-6 py-4 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center bg-slate-50 dark:bg-slate-800">
          <h2 class="text-xl font-bold text-slate-800 dark:text-slate-100">订单详情 <span class="text-sm font-normal text-slate-500 ml-2">#{{ currentOrder.orderNo }}</span></h2>
          <button @click="showOrderDetailsModal = false" class="text-slate-400 hover:text-slate-600 text-3xl leading-none">&times;</button>
        </div>
        
        <div class="p-6 overflow-y-auto max-h-[60vh]">
          <div v-if="isLoadingDetails" class="flex justify-center items-center py-10">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
            <span class="ml-3 text-slate-500">加载明细中...</span>
          </div>
          <template v-else>
            <div class="mb-4 flex flex-wrap gap-4 text-sm text-slate-600 dark:text-slate-400">
               <div class="bg-slate-100 dark:bg-slate-800 px-3 py-1.5 rounded-md"><span class="font-bold mr-1">桌号:</span> {{ currentOrder.tableNo || '无' }}</div>
               <div class="bg-slate-100 dark:bg-slate-800 px-3 py-1.5 rounded-md"><span class="font-bold mr-1">状态:</span> {{ getStatusText(currentOrder.status) }}</div>
               <div class="bg-slate-100 dark:bg-slate-800 px-3 py-1.5 rounded-md"><span class="font-bold mr-1">下单时间:</span> {{ currentOrder.createTime }}</div>
            </div>
            
            <table class="w-full text-left border-collapse mt-4">
              <thead>
                <tr class="bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300">
                  <th class="py-2 px-4 font-semibold text-sm rounded-l-lg">菜品名称</th>
                  <th class="py-2 px-4 font-semibold text-sm">单价</th>
                  <th class="py-2 px-4 font-semibold text-sm">数量</th>
                  <th class="py-2 px-4 font-semibold text-sm rounded-r-lg">小计</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in currentOrder.orderDetails" :key="item.id" class="border-b border-slate-100 dark:border-slate-800/50">
                  <td class="py-3 px-4 text-sm font-medium text-slate-800 dark:text-slate-200">{{ item.dishName }}</td>
                  <td class="py-3 px-4 text-sm text-slate-600 dark:text-slate-400">¥{{ parseFloat(item.unitPrice).toFixed(2) }}</td>
                  <td class="py-3 px-4 text-sm text-slate-500 font-semibold">x{{ item.quantity }}</td>
                  <td class="py-3 px-4 text-sm font-bold text-teal-600 dark:text-teal-400">¥{{ (item.unitPrice * item.quantity).toFixed(2) }}</td>
                </tr>
              </tbody>
            </table>
            
            <div v-if="!currentOrder.orderDetails || currentOrder.orderDetails.length === 0" class="text-center py-10 bg-slate-50 dark:bg-slate-800/50 rounded-lg mt-4 border border-dashed border-slate-200 dark:border-slate-700">
              <span class="text-3xl mb-2 block">🍽️</span>
              <span class="text-slate-500 text-sm">暂无菜品明细数据<br>可能由于这是在修复漏洞前下的旧订单，当时并未保存明细。</span>
            </div>
          </template>
        </div>
        
        <div class="p-4 border-t border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-900 flex justify-between items-center">
           <div class="text-lg font-bold text-slate-800 dark:text-slate-100">
             总计: <span class="text-primary text-2xl ml-2 tracking-tight">¥{{ parseFloat(currentOrder.totalAmount || 0).toFixed(2) }}</span>
           </div>
           <button @click="showOrderDetailsModal = false" class="px-6 py-2 bg-primary hover:bg-primary/90 text-white rounded-lg font-medium transition-colors">关闭</button>
        </div>
      </div>
    </div>

    <!-- Overview Modal Wizard -->
    <div v-if="showOverviewModal" class="fixed inset-0 bg-black/60 z-[100] flex items-center justify-center backdrop-blur-sm transition-all duration-300">
      <div class="bg-white dark:bg-slate-900 w-[800px] rounded-2xl shadow-2xl flex flex-col overflow-hidden">
        <div class="px-6 py-4 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center bg-slate-50 dark:bg-slate-800">
          <h2 class="text-xl font-bold text-slate-800 dark:text-slate-100">订单概览与销售统计</h2>
          <button @click="closeOverviewModal" class="text-slate-400 hover:text-slate-600 text-3xl leading-none">&times;</button>
        </div>
        
        <div class="p-6 flex-1 min-h-[500px] flex flex-col">
          <!-- Stepper -->
          <div class="flex items-center mb-8 relative">
             <div class="flex-1 text-center relative z-10"><div :class="['w-10 h-10 mx-auto rounded-full flex items-center justify-center font-bold text-lg mb-2 transition-colors ring-4 ring-white dark:ring-slate-900', overviewStep >= 1 ? 'bg-primary text-white' : 'bg-slate-200 text-slate-500']">1</div><span class="text-sm font-medium" :class="overviewStep >= 1 ? 'text-primary' : 'text-slate-500'">选择日期</span></div>
             <div class="flex-1 text-center relative z-10"><div :class="['w-10 h-10 mx-auto rounded-full flex items-center justify-center font-bold text-lg mb-2 transition-colors ring-4 ring-white dark:ring-slate-900', overviewStep >= 2 ? 'bg-primary text-white' : 'bg-slate-200 text-slate-500']">2</div><span class="text-sm font-medium" :class="overviewStep >= 2 ? 'text-primary' : 'text-slate-500'">选择维度</span></div>
             <div class="flex-1 text-center relative z-10"><div :class="['w-10 h-10 mx-auto rounded-full flex items-center justify-center font-bold text-lg mb-2 transition-colors ring-4 ring-white dark:ring-slate-900', overviewStep >= 3 ? 'bg-primary text-white' : 'bg-slate-200 text-slate-500']">3</div><span class="text-sm font-medium" :class="overviewStep >= 3 ? 'text-primary' : 'text-slate-500'">生成图表</span></div>
             <div class="absolute top-5 left-[16.66%] right-[16.66%] h-1 bg-slate-200 -z-0 rounded-full overflow-hidden"><div class="h-full bg-primary transition-all duration-300" :style="{ width: ((overviewStep - 1) * 50) + '%' }"></div></div>
          </div>
          
          <!-- Step 1: Date -->
          <div v-show="overviewStep === 1" class="flex-1 flex flex-col items-center justify-center py-10 fade-in">
            <h3 class="text-lg font-semibold text-slate-700 dark:text-slate-300 mb-6">请选择需要统计的订单日期</h3>
            <div class="relative">
              <input type="date" v-model="overviewDate" class="px-6 py-4 text-lg border-2 border-primary/30 rounded-xl focus:border-primary focus:ring-0 bg-slate-50 dark:bg-slate-800 text-slate-800 dark:text-slate-100 shadow-inner w-64 text-center cursor-pointer font-medium outline-none transition-colors" />
            </div>
            <button @click="nextStep(2)" :disabled="!overviewDate" class="mt-8 px-8 py-3 bg-primary text-white font-bold rounded-xl hover:bg-primary/90 disabled:opacity-50 disabled:cursor-not-allowed shadow-md shadow-primary/30 active:scale-95 transition-all text-lg">下一步：选择维度</button>
          </div>
          
          <!-- Step 2: Dimension -->
          <div v-show="overviewStep === 2" class="flex-1 flex flex-col items-center justify-center py-10 fade-in">
            <h3 class="text-lg font-semibold text-slate-700 dark:text-slate-300 mb-8">请选择销售数据统计维度</h3>
            <div class="flex gap-6">
              <div 
                @click="overviewDimension = 'dish'"
                :class="['w-48 p-6 rounded-2xl border-2 cursor-pointer transition-all text-center', overviewDimension === 'dish' ? 'border-primary bg-primary/10 shadow-lg shadow-primary/20 scale-105' : 'border-slate-200 dark:border-slate-700 hover:border-primary/50 text-slate-500 scale-100']"
              >
                <div class="text-5xl mb-4">🍲</div>
                <div class="font-bold text-lg text-slate-800 dark:text-slate-100 mb-2">菜品销售情况</div>
                <div class="text-xs text-slate-500">按各个单品统计营业额分布</div>
              </div>
              <div 
                @click="overviewDimension = 'category'"
                :class="['w-48 p-6 rounded-2xl border-2 cursor-pointer transition-all text-center', overviewDimension === 'category' ? 'border-primary bg-primary/10 shadow-lg shadow-primary/20 scale-105' : 'border-slate-200 dark:border-slate-700 hover:border-primary/50 text-slate-500 scale-100']"
              >
                <div class="text-5xl mb-4">🏷️</div>
                <div class="font-bold text-lg text-slate-800 dark:text-slate-100 mb-2">品类销售情况</div>
                <div class="text-xs text-slate-500">按分类汇总统揽销售分布结构</div>
              </div>
            </div>
            <div class="mt-12 flex gap-4">
              <button @click="overviewStep = 1" class="px-6 py-3 bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-200 font-bold rounded-xl hover:bg-slate-300 dark:hover:bg-slate-600 transition-all">上一步</button>
              <button @click="nextStep(3)" class="px-8 py-3 bg-primary text-white font-bold rounded-xl hover:bg-primary/90 shadow-md shadow-primary/30 active:scale-95 transition-all">下一步：生成图表</button>
            </div>
          </div>
          
          <!-- Step 3: Chart -->
          <div v-show="overviewStep === 3" class="flex-1 flex flex-col fade-in relative">
            <div class="flex justify-between items-center mb-4">
              <div class="text-slate-600 dark:text-slate-400 font-medium bg-slate-100 dark:bg-slate-800 px-4 py-2 rounded-lg">
                周期: <span class="text-slate-900 dark:text-slate-100 font-bold mr-4">{{ overviewDate }}</span>
                维度: <span class="text-primary font-bold">{{ overviewDimension === 'dish' ? '菜品销量/额' : '品类销量/额' }}</span>
              </div>
              <div class="flex bg-slate-100 dark:bg-slate-800 p-1.5 rounded-xl border border-slate-200 dark:border-slate-700">
                <button @click="setChartType('pie')" :class="['px-5 py-2 rounded-lg text-sm font-bold transition-all flex items-center gap-2', overviewChartType === 'pie' ? 'bg-white dark:bg-slate-600 shadow-sm text-primary' : 'text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200']"><span>🥧</span> 饼状图</button>
                <button @click="setChartType('bar')" :class="['px-5 py-2 rounded-lg text-sm font-bold transition-all flex items-center gap-2', overviewChartType === 'bar' ? 'bg-white dark:bg-slate-600 shadow-sm text-primary' : 'text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200']"><span>📊</span> 柱状图</button>
              </div>
            </div>
            
            <div v-if="isGeneratingChart" class="flex-1 flex flex-col items-center justify-center bg-slate-50/50 dark:bg-slate-900/50 rounded-xl border border-slate-100 dark:border-slate-800">
               <div class="animate-spin rounded-full h-12 w-12 border-b-4 border-primary border-t-transparent mb-4"></div>
               <div class="text-slate-500 font-medium tracking-wide">疯狂计算与制图中...</div>
            </div>
            <div v-else-if="!hasChartData" class="flex-1 flex flex-col items-center justify-center text-slate-400 bg-slate-50/50 dark:bg-slate-900/50 rounded-xl border border-dashed border-slate-200 dark:border-slate-800">
               <div class="text-5xl mb-4 opacity-50">📭</div>
               <div class="text-lg font-medium text-slate-500">抱歉，该日期下暂无任何已支付订单的数据记录</div>
            </div>
            <div v-else class="flex-1 relative border border-slate-200 dark:border-slate-700 rounded-xl bg-white dark:bg-slate-800 shadow-inner overflow-hidden flex flex-col p-4">
              <div ref="chartRef" class="w-full flex-1 min-h-[400px]"></div>
              
              <!-- Summary Card Float -->
              <div class="absolute top-4 right-4 bg-white/90 dark:bg-slate-800/90 backdrop-blur-md p-3 rounded-xl border border-slate-200 dark:border-slate-700 shadow-lg text-sm z-10">
                <div class="text-slate-500 mb-1 font-medium">当日总营业额</div>
                <div class="text-primary font-bold text-xl">¥ {{ totalDailySales.toFixed(2) }}</div>
              </div>
              
              <!-- Tooltip Hint -->
              <div class="absolute bottom-4 left-1/2 -translate-x-1/2 bg-slate-800/80 text-white text-xs px-4 py-2 rounded-full backdrop-blur-sm pointer-events-none shadow-lg font-medium flex items-center gap-2 z-10">
                <span>💡</span> 点击图表图形区块，可查看这部分数据的详细数值说明
              </div>
            </div>
          </div>
        </div>
        
        <div class="px-6 py-4 border-t border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800/50 flex justify-between items-center">
           <div>
             <button v-if="overviewStep === 3" @click="overviewStep = 2" class="px-6 py-2.5 border-2 border-slate-300 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-xl font-bold transition-colors">重新设置选项</button>
           </div>
           <button @click="closeOverviewModal" class="px-8 py-2.5 bg-slate-800 hover:bg-slate-900 text-white dark:bg-slate-700 dark:hover:bg-slate-600 rounded-xl font-bold shadow-md transition-colors active:scale-95">完成概览</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, shallowRef } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import * as echarts from 'echarts'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const orders = ref([])

// 获取订单列表
const fetchOrders = async () => {
  try {
    console.log('开始获取订单列表')
    console.log('API 基础地址:', request.defaults.baseURL)
    // 管理员端获取所有订单，使用分页接口，第一页，获取100条记录
    const response = await request.get('/orders/page?page=1&size=100')
    console.log('获取订单列表成功:', response)
    // MyBatis-Plus 分页返回格式可能是 {records: [...]} 或直接是 [...]
    if (response.records) {
      orders.value = response.records.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    } else if (Array.isArray(response)) {
      orders.value = response.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    } else {
      console.error('意外的响应格式:', response)
      orders.value = []
    }
    console.log('订单数据已更新，当前订单数量:', orders.value.length)
  } catch (error) {
    console.error('获取订单列表失败:', error)
    console.error('错误详情:', error.message)
    console.error('错误堆栈:', error.stack)
    // 使用模拟数据作为 fallback
    orders.value = [
      {
        id: 1,
        orderNo: '202603221510098BFE9C89',
        userId: 1,
        totalAmount: 68.00,
        status: 0,
        createTime: '2026-03-22 15:10:09',
        updateTime: '2026-03-22 15:10:09'
      },
      {
        id: 2,
        orderNo: '20260322152048CE78DC1E',
        userId: 1,
        totalAmount: 156.00,
        status: 0,
        createTime: '2026-03-22 15:20:48',
        updateTime: '2026-03-22 15:20:48'
      }
    ]
    console.log('使用模拟数据作为 fallback')
  }
}

// 查看详细相关状态和逻辑
const showOrderDetailsModal = ref(false)
const currentOrder = ref({})
const isLoadingDetails = ref(false)

const viewOrderDetails = async (order) => {
  currentOrder.value = { ...order } // 先使用列表中的基本数据渲染
  showOrderDetailsModal.value = true
  isLoadingDetails.value = true
  
  try {
    const response = await request.get(`/orders/${order.orderNo}`)
    currentOrder.value = response
  } catch (error) {
    console.error('获取订单详细失败:', error)
  } finally {
    isLoadingDetails.value = false
  }
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待付款',
    1: '待接单',
    2: '制作中',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知状态'
}

// === 订单概览与数据可视化 (Echarts) ===
const showOverviewModal = ref(false)
const overviewStep = ref(1)
const overviewDate = ref(new Date().toISOString().split('T')[0]) // 默认今天
const overviewDimension = ref('dish') // 'dish' | 'category'
const overviewChartType = ref('pie') // 'pie' | 'bar'
const chartRef = ref(null)
const chartInstance = shallowRef(null)

const isGeneratingChart = ref(false)
const hasChartData = ref(false)
const totalDailySales = ref(0)
const aggregatedData = ref([])

// 品类信息缓存池，用于将dish关联到category
const dishCategoryMap = ref({}) 

const openOverviewModal = () => {
  overviewStep.value = 1
  showOverviewModal.value = true
}

const closeOverviewModal = () => {
  showOverviewModal.value = false
  if (chartInstance.value) {
    chartInstance.value.dispose()
    chartInstance.value = null
  }
}

const nextStep = async (step) => {
  overviewStep.value = step
  if (step === 3) {
    await fetchAndRenderChart()
  }
}

const setChartType = (type) => {
  overviewChartType.value = type
  renderChartOptions()
}

// 为了将菜品归类到“品类”，我们需要预先获取菜品与品类的映射表
const fetchDishCategoryMap = async () => {
  if (Object.keys(dishCategoryMap.value).length > 0) return; // 已经加载过
  try {
    // 先获取分类表映射 id => name
    const catRes = await request.get('/category/list')
    const categoryIdToNameMap = {}
    catRes.forEach(cat => {
      categoryIdToNameMap[cat.id] = cat.name
    })

    // 再获取所有菜品
    const response = await request.get('/dish/list')
    response.forEach(dish => {
      // 把 菜品名 => 分类名 缓存在一个字典里供聚合统计使用
      dishCategoryMap.value[dish.name] = categoryIdToNameMap[dish.categoryId] || '其它分类'
    })
  } catch (error) {
    console.error('获取菜品字典失败', error)
  }
}

const fetchAndRenderChart = async () => {
  isGeneratingChart.value = true
  hasChartData.value = false
  
  try {
    // 只有选择按品类统计时才需要加载品类字典
    if (overviewDimension.value === 'category') {
      await fetchDishCategoryMap()
    }
    
    // 调用我们刚写的后端按日期查询所有关联明细的接口
    const res = await request.get(`/orders/daily-details?date=${overviewDate.value}`)
    const orders = res
    
    // 数据处理与计算聚合
    let total = 0
    const summaryMap = {}
    
    orders.forEach(order => {
      // 过滤掉已取消的订单（假设4是取消）
      if (order.status === 4) return;
      
      if (order.orderDetails && order.orderDetails.length > 0) {
        order.orderDetails.forEach(item => {
          const itemTotal = item.unitPrice * item.quantity
          total += itemTotal
          
          let groupKey = ''
          if (overviewDimension.value === 'dish') {
            groupKey = item.dishName
          } else {
             // 尝试从缓存获取其品类名
             groupKey = dishCategoryMap.value[item.dishName] || '其它分类'
          }
          
          if (!summaryMap[groupKey]) summaryMap[groupKey] = 0
          summaryMap[groupKey] += itemTotal
        })
      }
    })
    
    totalDailySales.value = total
    hasChartData.value = total > 0
    
    // 必须在这里先关闭 loading 骨架，否则 Vue 的 v-else 不会渲染，导致 chartRef 还没挂载到 DOM
    isGeneratingChart.value = false
    
    if (hasChartData.value) {
       // 将汇总的 map 转成 echarts 识别的 array格式
       aggregatedData.value = Object.keys(summaryMap).map(key => ({
         name: key,
         value: Number(summaryMap[key].toFixed(2)) // Echarts中value用于饼状图
       })).sort((a,b) => b.value - a.value) // 按销售额降序
       
       // DOM渲染完毕后初始化ECharts
       await nextTick()
       initEcharts()
    }
  } catch (err) {
    console.error('获取统计数据失败:', err)
  } finally {
    isGeneratingChart.value = false
  }
}

const initEcharts = () => {
  if (!chartRef.value) return;
  
  if (!chartInstance.value) {
    chartInstance.value = echarts.init(chartRef.value)
    
    // 交互响应：点击分区
    chartInstance.value.on('click', (params) => {
      alert(`【详细统计】\n${params.name} 的总销售额为：¥${params.value}\n占当日总营业额的比重约为：${((params.value / totalDailySales.value) * 100).toFixed(1)}%`)
    })
    
    // 处理尺寸自适应
    window.addEventListener('resize', () => {
      if (chartInstance.value) chartInstance.value.resize()
    })
  }
  
  renderChartOptions()
}

const renderChartOptions = () => {
  if (!chartInstance.value) return;
  
  const titleText = overviewDimension.value === 'dish' ? '各单品销售额占比情况' : '各大品类销售额占比结构'
  const dataNames = aggregatedData.value.map(item => item.name)
  const dataValues = aggregatedData.value.map(item => item.value)
  
  let option = {}
  
  if (overviewChartType.value === 'pie') {
    option = {
      title: { text: titleText, left: 'center', top: 10, textStyle: { color: '#334155', fontWeight: 'bold' } },
      tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: ¥{c} ({d}%)' },
      legend: { type: 'scroll', orient: 'vertical', right: 10, top: 40, bottom: 20 },
      series: [
        {
          name: '销售额',
          type: 'pie',
          radius: ['40%', '70%'],
          itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
          data: aggregatedData.value,
          emphasis: {
            label: { show: true, fontSize: 18, fontWeight: 'bold' },
            itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.3)' }
          }
        }
      ]
    }
  } else {
    // Bar
    option = {
      title: { text: titleText, left: 'center', top: 10, textStyle: { color: '#334155', fontWeight: 'bold' } },
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, formatter: '{b} <br/> 销售额: ¥{c}' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: dataNames, axisLabel: { interval: 0, rotate: 30 } },
      yAxis: { type: 'value', name: '金额 (¥)' },
      series: [
        {
          name: '销售额',
          type: 'bar',
          barWidth: '50%',
          data: dataValues,
          itemStyle: { 
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#0ea5e9' },
              { offset: 1, color: '#14b8a6' }
            ]),
            borderRadius: [6, 6, 0, 0] 
          }
        }
      ]
    }
  }
  
  // 使用刚指定的配置项和数据显示图表。
  chartInstance.value.setOption(option, true)
}

onMounted(() => {
  fetchOrders()
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
/* 导入Google字体 */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Manrope:wght@500;600;700;800&display=swap');

body {
  font-family: 'Inter', sans-serif;
}

h1, h2, h3 {
  font-family: 'Manrope', sans-serif;
}

/* 渐入小动画 */
.fade-in {
  animation: fadeIn 0.4s ease-out forwards;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>