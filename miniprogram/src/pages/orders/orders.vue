<template>
  <view class="container">
    <!-- 顶部装饰条 -->
    <view class="header-bg"></view>
    <view class="header">
      <text class="page-title">我的订单</text>
      <text class="header-subtitle">查看您的历史订单</text>
    </view>

    <view v-if="loading" class="loading-container">
      <view class="loading-spinner"></view>
      <text class="loading-text">加载中...</text>
    </view>

    <view v-else-if="error" class="state-container">
      <view class="state-icon error">⚠️</view>
      <text class="state-title">加载失败</text>
      <text class="state-desc">请检查网络后重试</text>
      <button class="primary-btn" @tap="fetchOrders">
        <text>重新加载</text>
      </button>
    </view>

    <view v-else-if="orders.length === 0" class="state-container">
      <view class="state-icon empty">📋</view>
      <text class="state-title">暂无订单</text>
      <text class="state-desc">快去下单吧~</text>
      <button class="secondary-btn" @tap="goToOrder">去点餐</button>
    </view>

    <view v-else class="order-content">
      <!-- 订单统计 -->
      <view class="order-stats">
        <view class="stat-item">
          <text class="stat-num">{{ orders.length }}</text>
          <text class="stat-label">全部订单</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-num">{{ completedCount }}</text>
          <text class="stat-label">已完成</text>
        </view>
      </view>

      <!-- 订单列表 -->
      <view class="order-list">
        <view
          v-for="order in orders"
          :key="order.id"
          class="order-card"
          :class="'card-' + order.status"
          @tap="showOrderDetail(order)"
        >
          <!-- 卡片顶部 -->
          <view class="card-header">
            <view class="header-left">
              <view class="table-tag">
                <text class="table-icon">🪑</text>
                <text class="table-text">{{ order.tableNo || '外带' }}</text>
              </view>
            </view>
            <view class="status-badge" :class="'status-' + order.status">
              <view class="status-dot"></view>
              <text class="status-text">{{ getStatusText(order.status) }}</text>
            </view>
          </view>

          <!-- 卡片主体 -->
          <view class="card-body">
            <view class="order-info">
              <text class="order-no-label">订单号</text>
              <text class="order-no">{{ order.orderNo }}</text>
            </view>
            <view class="time-info">
              <text class="time-icon">🕐</text>
              <text class="time-text">{{ formatTime(order.createTime) }}</text>
            </view>
          </view>

          <!-- 卡片底部 -->
          <view class="card-footer">
            <view class="amount-section">
              <text class="amount-label">实付金额</text>
              <text class="amount-value">¥{{ parseFloat(order.totalAmount).toFixed(2) }}</text>
            </view>
            <view class="action-section">
              <text class="view-detail">查看详情</text>
              <text class="arrow-icon">›</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 订单详情弹窗 -->
    <view v-if="showDetailModal" class="modal-overlay" @tap="showDetailModal = false">
      <view class="detail-modal" @tap.stop>
        <!-- 弹窗头部 -->
        <view class="modal-header">
          <text class="modal-title">订单详情</text>
          <view class="header-actions">
            <view class="status-badge small" :class="'status-' + selectedOrder?.status">
              <text class="status-text">{{ getStatusText(selectedOrder?.status) }}</text>
            </view>
            <button class="close-btn" @tap="showDetailModal = false">
              <text>✕</text>
            </button>
          </view>
        </view>

        <!-- 订单基本信息 -->
        <view class="basic-info">
          <view class="info-item">
            <text class="info-icon">🔢</text>
            <view class="info-content">
              <text class="info-label">订单编号</text>
              <text class="info-value">{{ selectedOrder?.orderNo }}</text>
            </view>
          </view>
          <view class="info-item">
            <text class="info-icon">🪑</text>
            <view class="info-content">
              <text class="info-label">桌号/类型</text>
              <text class="info-value">{{ selectedOrder?.tableNo || '外带' }}</text>
            </view>
          </view>
          <view class="info-item">
            <text class="info-icon">🕐</text>
            <view class="info-content">
              <text class="info-label">下单时间</text>
              <text class="info-value">{{ selectedOrder?.createTime }}</text>
            </view>
          </view>
        </view>

        <!-- 菜品明细 -->
        <scroll-view scroll-y class="detail-body">
          <view class="detail-section">
            <view class="section-header">
              <text class="section-icon">🍽️</text>
              <text class="section-title">菜品明细</text>
              <text class="section-count">共{{ orderDetails.length }}件</text>
            </view>

            <view class="dish-list">
              <view v-for="(item, index) in orderDetails" :key="index" class="dish-item">
                <view class="dish-left">
                  <text class="dish-name">{{ item.dishName }}</text>
                  <view class="dish-meta">
                    <text class="dish-price">¥{{ item.unitPrice }}</text>
                    <text class="dish-multiply">×</text>
                    <text class="dish-qty">{{ item.quantity }}</text>
                  </view>
                </view>
                <text class="dish-total">¥{{ (item.unitPrice * item.quantity).toFixed(2) }}</text>
              </view>
            </view>
          </view>
        </scroll-view>

        <!-- 底部金额 -->
        <view class="detail-footer">
          <view class="footer-left">
            <text class="total-label">合计</text>
          </view>
          <view class="footer-right">
            <text class="total-symbol">¥</text>
            <text class="total-value">{{ parseFloat(selectedOrder?.totalAmount || 0).toFixed(2) }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { get } from '../../utils/request'

const loading = ref(true)
const error = ref(false)
const orders = ref([])
const showDetailModal = ref(false)
const selectedOrder = ref(null)
const orderDetails = ref([])

// 计算已完成订单数量
const completedCount = computed(() => {
  return orders.value.filter(o => o.status === 3).length
})

function getStatusText(status) {
  const map = { 0: '待付款', 1: '待接单', 2: '制作中', 3: '已完成', 4: '已取消' }
  return map[status] || '未知'
}

function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  const hour = date.getHours().toString().padStart(2, '0')
  const minute = date.getMinutes().toString().padStart(2, '0')
  return `${month}-${day} ${hour}:${minute}`
}

function goToOrder() {
  uni.switchTab({ url: '/pages/index/index' })
}

async function fetchOrders() {
  loading.value = true
  error.value = false
  try {
    const data = await get('/api/orders/list')
    orders.value = (data || []).sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
  } catch (e) {
    console.error('获取订单失败:', e)
    error.value = true
  } finally {
    loading.value = false
  }
}

async function showOrderDetail(order) {
  selectedOrder.value = order
  showDetailModal.value = true
  try {
    const data = await get('/api/orders/' + order.orderNo)
    orderDetails.value = data?.orderDetails || []
  } catch (e) {
    console.error('获取订单详情失败:', e)
    orderDetails.value = []
  }
}

onShow(() => {
  fetchOrders()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #F5F6FA;
  padding-bottom: calc(32rpx + env(safe-area-inset-bottom));
}

/* 顶部区域 */
.header-bg {
  background: linear-gradient(135deg, #FF6B35 0%, #FF8F5C 100%);
  height: 240rpx;
  border-radius: 0 0 40rpx 40rpx;
}

.header {
  margin-top: -200rpx;
  padding: 32rpx 32rpx 48rpx;
}

.page-title {
  font-size: 44rpx;
  font-weight: 800;
  color: white;
  display: block;
  letter-spacing: 2rpx;
}

.header-subtitle {
  font-size: 26rpx;
  color: rgba(255,255,255,0.85);
  margin-top: 8rpx;
  display: block;
}

/* 状态容器 */
.state-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;
}

.state-icon {
  width: 160rpx;
  height: 160rpx;
  border-radius: 80rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 80rpx;
  margin-bottom: 32rpx;
}

.state-icon.empty {
  background: linear-gradient(135deg, #FFF5F0 0%, #FFE8DC 100%);
}

.state-icon.error {
  background: linear-gradient(135deg, #FEF2F2 0%, #FEE2E2 100%);
}

.state-title {
  font-size: 36rpx;
  font-weight: 700;
  color: #1F2937;
  margin-bottom: 12rpx;
}

.state-desc {
  font-size: 28rpx;
  color: #9CA3AF;
  margin-bottom: 40rpx;
}

/* 按钮样式 */
.primary-btn {
  background: linear-gradient(135deg, #FF6B35 0%, #FF8F5C 100%);
  color: white;
  padding: 24rpx 64rpx;
  border-radius: 50rpx;
  font-size: 30rpx;
  font-weight: 600;
  border: none;
  box-shadow: 0 8rpx 24rpx rgba(255, 107, 53, 0.3);
}

.secondary-btn {
  background: white;
  color: #FF6B35;
  padding: 24rpx 64rpx;
  border-radius: 50rpx;
  font-size: 30rpx;
  font-weight: 600;
  border: 2rpx solid #FF6B35;
}

/* 加载动画 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.loading-spinner {
  width: 80rpx;
  height: 80rpx;
  border: 6rpx solid #FFE8DC;
  border-top-color: #FF6B35;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 24rpx;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-text {
  font-size: 28rpx;
  color: #9CA3AF;
}

/* 内容区域 */
.order-content {
  padding: 0 24rpx;
}

/* 统计卡片 */
.order-stats {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-num {
  font-size: 48rpx;
  font-weight: 800;
  color: #FF6B35;
}

.stat-label {
  font-size: 24rpx;
  color: #9CA3AF;
  margin-top: 8rpx;
}

.stat-divider {
  width: 1rpx;
  height: 60rpx;
  background: #E5E7EB;
  margin: 0 40rpx;
}

/* 订单列表 */
.order-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.order-card {
  background: white;
  border-radius: 24rpx;
  padding: 28rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
  position: relative;
  overflow: hidden;
}

.order-card::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 6rpx;
}

.card-0::before { background: #EF4444; }
.card-1::before { background: #F59E0B; }
.card-2::before { background: #3B82F6; }
.card-3::before { background: #10B981; }
.card-4::before { background: #9CA3AF; }

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.table-tag {
  display: flex;
  align-items: center;
  background: #FFF7F4;
  padding: 10rpx 20rpx;
  border-radius: 12rpx;
}

.table-icon {
  font-size: 24rpx;
  margin-right: 8rpx;
}

.table-text {
  font-size: 26rpx;
  color: #FF6B35;
  font-weight: 600;
}

/* 状态标签 */
.status-badge {
  display: flex;
  align-items: center;
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  gap: 8rpx;
}

.status-badge.small {
  padding: 6rpx 14rpx;
}

.status-dot {
  width: 8rpx;
  height: 8rpx;
  border-radius: 50%;
}

.status-0 { background: #FEE2E2; }
.status-0 .status-dot { background: #EF4444; }
.status-0 .status-text { color: #DC2626; }

.status-1 { background: #FEF3C7; }
.status-1 .status-dot { background: #F59E0B; }
.status-1 .status-text { color: #D97706; }

.status-2 { background: #DBEAFE; }
.status-2 .status-dot { background: #3B82F6; }
.status-2 .status-text { color: #2563EB; }

.status-3 { background: #D1FAE5; }
.status-3 .status-dot { background: #10B981; }
.status-3 .status-text { color: #059669; }

.status-4 { background: #F1F5F9; }
.status-4 .status-dot { background: #9CA3AF; }
.status-4 .status-text { color: #6B7280; }

.status-text {
  font-size: 24rpx;
  font-weight: 600;
}

/* 卡片主体 */
.card-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-top: 1rpx dashed #E5E7EB;
  border-bottom: 1rpx dashed #E5E7EB;
  margin-bottom: 20rpx;
}

.order-info {
  display: flex;
  flex-direction: column;
}

.order-no-label {
  font-size: 22rpx;
  color: #9CA3AF;
  margin-bottom: 4rpx;
}

.order-no {
  font-size: 26rpx;
  color: #374151;
  font-weight: 600;
  letter-spacing: 1rpx;
}

.time-info {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.time-icon {
  font-size: 24rpx;
}

.time-text {
  font-size: 24rpx;
  color: #9CA3AF;
}

/* 卡片底部 */
.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.amount-section {
  display: flex;
  flex-direction: column;
}

.amount-label {
  font-size: 22rpx;
  color: #9CA3AF;
  margin-bottom: 4rpx;
}

.amount-value {
  font-size: 40rpx;
  color: #FF6B35;
  font-weight: 800;
}

.action-section {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.view-detail {
  font-size: 26rpx;
  color: #FF6B35;
  font-weight: 500;
}

.arrow-icon {
  font-size: 32rpx;
  color: #FF6B35;
  font-weight: bold;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.6);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  z-index: 100;
  backdrop-filter: blur(4rpx);
}

.detail-modal {
  background: white;
  width: 100%;
  border-radius: 40rpx 40rpx 0 0;
  max-height: 88vh;
  display: flex;
  flex-direction: column;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
    opacity: 0.8;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx;
  border-bottom: 1rpx solid #F3F4F6;
  flex-shrink: 0;
}

.modal-title {
  font-size: 36rpx;
  font-weight: 800;
  color: #1F2937;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.close-btn {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: #F5F6FA;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6B7280;
  font-size: 32rpx;
}

/* 基本信息 */
.basic-info {
  padding: 24rpx 32rpx;
  background: #FAFAFA;
  display: flex;
  gap: 24rpx;
  flex-shrink: 0;
}

.info-item {
  flex: 1;
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
}

.info-icon {
  font-size: 32rpx;
  margin-top: 4rpx;
}

.info-content {
  display: flex;
  flex-direction: column;
}

.info-label {
  font-size: 22rpx;
  color: #9CA3AF;
  margin-bottom: 4rpx;
}

.info-value {
  font-size: 24rpx;
  color: #374151;
  font-weight: 600;
}

/* 详情内容 */
.detail-body {
  flex: 1;
  overflow-y: auto;
}

.detail-section {
  padding: 24rpx 32rpx;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-icon {
  font-size: 32rpx;
  margin-right: 12rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 700;
  color: #1F2937;
}

.section-count {
  font-size: 24rpx;
  color: #9CA3AF;
  margin-left: 12rpx;
}

/* 菜品列表 */
.dish-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.dish-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  background: linear-gradient(135deg, #FFF9F6 0%, #FFF5F0 100%);
  border-radius: 20rpx;
}

.dish-left {
  flex: 1;
  min-width: 0;
}

.dish-name {
  font-size: 30rpx;
  color: #1F2937;
  font-weight: 600;
  display: block;
  margin-bottom: 8rpx;
}

.dish-meta {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.dish-price {
  font-size: 26rpx;
  color: #6B7280;
}

.dish-multiply {
  font-size: 24rpx;
  color: #D1D5DB;
}

.dish-qty {
  font-size: 26rpx;
  color: #6B7280;
}

.dish-total {
  font-size: 32rpx;
  color: #FF6B35;
  font-weight: 700;
  flex-shrink: 0;
  margin-left: 24rpx;
}

/* 底部金额 */
.detail-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx;
  padding-bottom: calc(32rpx + env(safe-area-inset-bottom));
  background: linear-gradient(135deg, #FF6B35 0%, #FF8F5C 100%);
  border-radius: 40rpx 40rpx 0 0;
  flex-shrink: 0;
}

.footer-left {
  display: flex;
  align-items: center;
}

.total-label {
  font-size: 32rpx;
  color: rgba(255,255,255,0.9);
  font-weight: 500;
}

.footer-right {
  display: flex;
  align-items: baseline;
}

.total-symbol {
  font-size: 28rpx;
  color: white;
  font-weight: 600;
  margin-right: 4rpx;
}

.total-value {
  font-size: 52rpx;
  color: white;
  font-weight: 800;
}
</style>
