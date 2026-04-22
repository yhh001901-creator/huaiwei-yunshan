<template>
  <view class="container">
    <view class="header">
      <view class="user-info">
        <image class="avatar" :src="memberInfo.avatar || '/static/placeholder.svg'"></image>
        <view class="info-text">
          <text class="nickname">{{ memberInfo.nickname || '点击设置昵称' }}</text>
          <text class="phone">{{ memberInfo.phone || '未绑定手机号' }}</text>
        </view>
      </view>
      <view class="member-badge" :class="memberInfo.isMember === 1 ? 'active' : ''">
        <text class="badge-text">{{ memberInfo.isMember === 1 ? '👑 会员' : '🔓 开通会员' }}</text>
      </view>
    </view>

    <view class="stats-card">
      <view class="stat-item">
        <text class="stat-value">{{ memberInfo.point || 0 }}</text>
        <text class="stat-label">积分</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item">
        <text class="stat-value">{{ coupons.length }}</text>
        <text class="stat-label">优惠券</text>
      </view>
    </view>

    <view class="action-section">
      <view class="action-card" @tap="doCheckin">
        <view class="action-icon">📅</view>
        <view class="action-text">
          <text class="action-title">每日签到</text>
          <text class="action-desc">每天签到得10积分</text>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-title">我的优惠券</view>
      <view v-if="coupons.length === 0" class="empty-hint">
        <text>暂无优惠券</text>
      </view>
      <view v-else class="coupon-list">
        <view v-for="c in coupons" :key="c.id" class="coupon-item" :class="c.status === 1 ? 'used' : ''">
          <view class="coupon-left">
            <text class="coupon-amount">¥{{ c.templateDiscountValue }}</text>
            <text class="coupon-condition">{{ c.templateMinAmount > 0 ? '满' + c.templateMinAmount + '可用' : '无门槛' }}</text>
          </view>
          <view class="coupon-right">
            <text class="coupon-name">{{ c.templateName }}</text>
            <text class="coupon-status" :class="c.status === 0 ? 'available' : 'used'">{{ c.status === 0 ? '未使用' : '已使用' }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-title">积分明细</view>
      <view v-if="pointLogs.length === 0" class="empty-hint">
        <text>暂无积分记录</text>
      </view>
      <view v-else class="point-list">
        <view v-for="log in pointLogs" :key="log.id" class="point-item">
          <view class="point-info">
            <text class="point-type">{{ getPointTypeText(log.type) }}</text>
            <text class="point-source">{{ log.source }}</text>
          </view>
          <text class="point-value" :class="log.point > 0 ? 'plus' : 'minus'">{{ log.point > 0 ? '+' : '' }}{{ log.point }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { get, post, imageBaseURL } from '../../utils/request'

const memberInfo = ref({
  point: 0,
  isMember: 0,
  nickname: '',
  phone: '',
  avatar: ''
})

const coupons = ref([])
const pointLogs = ref([])

function getPointTypeText(type) {
  const map = { 1: '消费获得', 2: '签到', 3: '兑换', 4: '管理员调整', 5: '生日特权' }
  return map[type] || '其他'
}

async function loadData() {
  try {
    const [memberRes, couponRes, pointRes] = await Promise.all([
      get('/api/member/info'),
      get('/api/coupon/my/list'),
      get('/api/member/point/list')
    ])
    if (memberRes) {
      memberInfo.value = memberRes
    }
    coupons.value = couponRes || []
    pointLogs.value = pointRes || []
  } catch (e) {
    console.error('加载会员数据失败:', e)
  }
}

async function doCheckin() {
  try {
    await post('/api/member/checkin', {})
    uni.showToast({ title: '签到成功 +10积分', icon: 'success' })
    loadData()
  } catch (e) {
    uni.showToast({ title: e.response?.data?.msg || '签到失败', icon: 'none' })
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #F8FAFC;
  padding: 24rpx;
  padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
}

.header {
  background: linear-gradient(135deg, #F97316, #FB923C);
  border-radius: 24rpx;
  padding: 40rpx 32rpx;
  margin-bottom: 24rpx;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 24rpx;
  margin-bottom: 32rpx;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: #FFF;
}

.info-text {
  flex: 1;
}

.nickname {
  font-size: 36rpx;
  font-weight: bold;
  color: #FFF;
  display: block;
}

.phone {
  font-size: 24rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 8rpx;
  display: block;
}

.member-badge {
  background: rgba(255,255,255,0.3);
  border-radius: 32rpx;
  padding: 16rpx 32rpx;
  text-align: center;
}

.member-badge.active {
  background: rgba(255,215,0,0.4);
}

.badge-text {
  color: #FFF;
  font-size: 28rpx;
  font-weight: 600;
}

.stats-card {
  background: #FFF;
  border-radius: 24rpx;
  padding: 40rpx 32rpx;
  display: flex;
  justify-content: space-around;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 48rpx;
  font-weight: bold;
  color: #F97316;
  display: block;
}

.stat-label {
  font-size: 24rpx;
  color: #94A3B8;
  margin-top: 8rpx;
  display: block;
}

.stat-divider {
  width: 2rpx;
  background: #E2E8F0;
}

.action-section {
  margin-bottom: 24rpx;
}

.action-card {
  background: #FFF;
  border-radius: 24rpx;
  padding: 32rpx;
  display: flex;
  align-items: center;
  gap: 24rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
}

.action-card:active {
  opacity: 0.8;
}

.action-icon {
  font-size: 48rpx;
}

.action-text {
  flex: 1;
}

.action-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #1F2937;
  display: block;
}

.action-desc {
  font-size: 24rpx;
  color: #94A3B8;
  margin-top: 8rpx;
  display: block;
}

.section {
  background: #FFF;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
  margin-bottom: 24rpx;
}

.empty-hint {
  text-align: center;
  padding: 60rpx 0;
  color: #94A3B8;
  font-size: 28rpx;
}

.coupon-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.coupon-item {
  display: flex;
  background: #FFF9F5;
  border-radius: 16rpx;
  overflow: hidden;
  border: 2rpx solid #FFE4D6;
}

.coupon-item.used {
  opacity: 0.5;
}

.coupon-left {
  background: #F97316;
  color: #FFF;
  padding: 32rpx 24rpx;
  text-align: center;
  min-width: 160rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.coupon-amount {
  font-size: 44rpx;
  font-weight: bold;
}

.coupon-condition {
  font-size: 20rpx;
  margin-top: 8rpx;
}

.coupon-right {
  flex: 1;
  padding: 24rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.coupon-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #1F2937;
  display: block;
}

.coupon-status {
  font-size: 22rpx;
  margin-top: 8rpx;
}

.coupon-status.available {
  color: #F97316;
}

.coupon-status.used {
  color: #94A3B8;
}

.point-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.point-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F1F5F9;
}

.point-info {
  flex: 1;
}

.point-type {
  font-size: 28rpx;
  color: #1F2937;
  font-weight: 500;
  display: block;
}

.point-source {
  font-size: 22rpx;
  color: #94A3B8;
  margin-top: 4rpx;
  display: block;
}

.point-value {
  font-size: 32rpx;
  font-weight: bold;
}

.point-value.plus {
  color: #F97316;
}

.point-value.minus {
  color: #EF4444;
}
</style>
