<template>
  <view class="container">
    <!-- 头部个人信息 -->
    <view class="profile-header">
      <view class="avatar" @tap="showAvatarPicker">
        <text class="avatar-text">{{ username.charAt(0).toUpperCase() }}</text>
      </view>
      <view class="info-area">
        <text class="nickname">{{ username }}</text>
        <view class="role-badge">{{ roleName }}</view>
      </view>
      <view class="edit-btn" @tap="showEditModal = true">
        <text class="edit-icon">✏️</text>
      </view>
    </view>

    <!-- 统计卡片 -->
    <view class="stats-card">
      <view class="stat-item" @tap="goToOrders">
        <text class="stat-num">{{ orderCount }}</text>
        <text class="stat-label">我的订单</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item" @tap="goToMember">
        <text class="stat-num">{{ memberInfo.point || 0 }}</text>
        <text class="stat-label">积分</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item" @tap="goToMember">
        <text class="stat-num">{{ memberInfo.isMember === 1 ? 'VIP' : '-' }}</text>
        <text class="stat-label">会员</text>
      </view>
    </view>

    <!-- 会员中心入口 -->
    <view class="member-banner" @tap="goToMember">
      <text class="member-banner-text">👑 进入会员中心</text>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="section-title">账号设置</view>
      <view class="menu-group">
        <view class="menu-item" @tap="showEditModal = true">
          <view class="menu-left">
            <text class="menu-icon">👤</text>
            <text class="menu-text">修改资料</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @tap="showPasswordModal = true">
          <view class="menu-left">
            <text class="menu-icon">🔒</text>
            <text class="menu-text">修改密码</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>

    <view class="menu-section">
      <view class="section-title">其他</view>
      <view class="menu-group">
        <view class="menu-item" @tap="showAboutModal = true">
          <view class="menu-left">
            <text class="menu-icon">ℹ️</text>
            <text class="menu-text">关于我们</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @tap="handleLogout">
          <view class="menu-left">
            <text class="menu-icon">🚪</text>
            <text class="menu-text logout-text">退出登录</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 修改资料弹窗 -->
    <view v-if="showEditModal" class="modal-overlay" @tap="showEditModal = false">
      <view class="modal" @tap.stop>
        <view class="modal-header">
          <text class="modal-title">修改资料</text>
          <button class="modal-close" @tap="showEditModal = false">&times;</button>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">用户名</text>
            <input class="form-input" :value="editForm.username" @input="editForm.username = $event.detail.value" placeholder="请输入用户名" />
          </view>
          <view class="form-item">
            <text class="form-label">手机号</text>
            <input class="form-input" :value="editForm.phone" @input="editForm.phone = $event.detail.value" type="number" placeholder="请输入手机号" />
          </view>
        </view>
        <view class="modal-footer">
          <button class="btn-cancel" @tap="showEditModal = false">取消</button>
          <button class="btn-confirm" @tap="saveProfile">保存</button>
        </view>
      </view>
    </view>

    <!-- 修改密码弹窗 -->
    <view v-if="showPasswordModal" class="modal-overlay" @tap="showPasswordModal = false">
      <view class="modal" @tap.stop>
        <view class="modal-header">
          <text class="modal-title">修改密码</text>
          <button class="modal-close" @tap="showPasswordModal = false">&times;</button>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">旧密码</text>
            <input class="form-input" :value="pwdForm.oldPassword" @input="pwdForm.oldPassword = $event.detail.value" type="password" placeholder="请输入旧密码" />
          </view>
          <view class="form-item">
            <text class="form-label">新密码</text>
            <input class="form-input" :value="pwdForm.newPassword" @input="pwdForm.newPassword = $event.detail.value" type="password" placeholder="请输入新密码" />
          </view>
          <view class="form-item">
            <text class="form-label">确认新密码</text>
            <input class="form-input" :value="pwdForm.confirmPassword" @input="pwdForm.confirmPassword = $event.detail.value" type="password" placeholder="请再次输入新密码" />
          </view>
        </view>
        <view class="modal-footer">
          <button class="btn-cancel" @tap="showPasswordModal = false">取消</button>
          <button class="btn-confirm" @tap="savePassword">确认修改</button>
        </view>
      </view>
    </view>

    <!-- 关于我们弹窗 -->
    <view v-if="showAboutModal" class="modal-overlay" @tap="showAboutModal = false">
      <view class="modal" @tap.stop>
        <view class="modal-header">
          <text class="modal-title">关于我们</text>
          <button class="modal-close" @tap="showAboutModal = false">&times;</button>
        </view>
        <view class="modal-body about-body">
          <view class="about-logo">🍜</view>
          <text class="about-name">淮味云膳</text>
          <text class="about-version">版本 1.0.0</text>
          <view class="about-divider"></view>
          <text class="about-desc">淮味云膳是一个智能化的餐馆服务系统，提供在线点餐、订单管理、后厨管理等功能，致力于为餐厅提供高效便捷的数字化解决方案。</text>
          <view class="about-divider"></view>
          <view class="about-info">
            <text class="info-label">技术支持</text>
            <text class="info-value">淮味云膳团队</text>
          </view>
          <view class="about-info">
            <text class="info-label">联系方式</text>
            <text class="info-value">support@huaiweiyunshan.com</text>
          </view>
        </view>
        <view class="modal-footer single-btn">
          <button class="btn-confirm" @tap="showAboutModal = false">知道了</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '../../stores/user'
import { useCartStore } from '../../stores/cart'
import { get } from '../../utils/request'

const userStore = useUserStore()
const cartStore = useCartStore()

const username = computed(() => userStore.username || '用户')
const roleName = computed(() => userStore.roleName || '顾客')
const cartCount = computed(() => cartStore.totalQuantity)
const memberInfo = ref({ point: 0, isMember: 0, nickname: '', phone: '' })

const orderCount = ref(0)
const showEditModal = ref(false)
const showPasswordModal = ref(false)
const showAboutModal = ref(false)

const editForm = ref({ username: '', phone: '' })
const pwdForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })

async function fetchOrderCount() {
  try {
    const data = await get('/api/orders/list')
    orderCount.value = (data || []).length
  } catch (e) {
    orderCount.value = 0
  }
}

async function loadMemberInfo() {
  try {
    const data = await get('/api/member/info')
    if (data) {
      memberInfo.value = data
    }
  } catch (e) {
    memberInfo.value = { point: 0, isMember: 0 }
  }
}

function goToMember() {
  uni.navigateTo({ url: '/pages/member/member' })
}

function goToOrders() {
  uni.switchTab({ url: '/pages/orders/orders' })
}

function showStatsTip() {
  uni.showToast({ title: '功能开发中...', icon: 'none' })
}

function showAvatarPicker() {
  uni.showToast({ title: '头像功能开发中...', icon: 'none' })
}

function handleLogout() {
  uni.showModal({
    title: '退出登录',
    content: '确定要退出登录吗？',
    confirmColor: '#F97316',
    success: (res) => {
      if (res.confirm) {
        userStore.logout()
        uni.reLaunch({ url: '/pages/login/login' })
      }
    }
  })
}

function saveProfile() {
  if (!editForm.value.username && !editForm.value.phone) {
    uni.showToast({ title: '请填写要修改的内容', icon: 'none' })
    return
  }
  uni.showLoading({ title: '保存中...' })
  const data = {}
  if (editForm.value.username) data.username = editForm.value.username
  if (editForm.value.phone) data.phone = editForm.value.phone

  userStore.updateProfile(data).then(res => {
    uni.hideLoading()
    if (res.success) {
      showEditModal.value = false
      uni.showToast({ title: '修改成功', icon: 'success' })
    } else {
      uni.showToast({ title: res.message || '修改失败', icon: 'none' })
    }
  }).catch(() => {
    uni.hideLoading()
    uni.showToast({ title: '修改失败', icon: 'none' })
  })
}

function savePassword() {
  if (!pwdForm.value.oldPassword) {
    uni.showToast({ title: '请输入旧密码', icon: 'none' })
    return
  }
  if (!pwdForm.value.newPassword) {
    uni.showToast({ title: '请输入新密码', icon: 'none' })
    return
  }
  if (pwdForm.value.newPassword !== pwdForm.value.confirmPassword) {
    uni.showToast({ title: '两次密码不一致', icon: 'none' })
    return
  }
  if (pwdForm.value.newPassword.length < 6) {
    uni.showToast({ title: '密码不能少于6位', icon: 'none' })
    return
  }

  uni.showLoading({ title: '修改中...' })
  userStore.updatePassword(pwdForm.value.oldPassword, pwdForm.value.newPassword).then(res => {
    uni.hideLoading()
    if (res.success) {
      showPasswordModal.value = false
      pwdForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
      uni.showToast({ title: '密码修改成功', icon: 'success' })
    } else {
      uni.showToast({ title: res.message || '修改失败', icon: 'none' })
    }
  }).catch(() => {
    uni.hideLoading()
    uni.showToast({ title: '修改失败', icon: 'none' })
  })
}

function initEditForm() {
  editForm.value = {
    username: userStore.state.userInfo?.username || '',
    phone: userStore.state.userInfo?.phone || ''
  }
}

onShow(() => {
  fetchOrderCount()
  userStore.refreshUserInfo()
  loadMemberInfo()
})

onMounted(() => {
  initEditForm()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #F8FAFC;
  padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
}

.profile-header {
  background: linear-gradient(135deg, #F97316 0%, #FB923C 100%);
  padding: 48rpx 32rpx 40rpx;
  padding-top: calc(48rpx + env(safe-area-inset-top));
  display: flex;
  align-items: center;
  position: relative;
}

.avatar {
  width: 110rpx;
  height: 110rpx;
  border-radius: 50%;
  background: rgba(255,255,255,0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4rpx solid rgba(255,255,255,0.4);
  flex-shrink: 0;
}

.avatar:active {
  opacity: 0.8;
}

.avatar-text {
  font-size: 44rpx;
  color: white;
  font-weight: bold;
}

.info-area {
  flex: 1;
  margin-left: 20rpx;
  min-width: 0;
}

.nickname {
  font-size: 36rpx;
  color: white;
  font-weight: bold;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.role-badge {
  display: inline-block;
  font-size: 22rpx;
  color: rgba(255,255,255,0.9);
  margin-top: 8rpx;
  background: rgba(255,255,255,0.2);
  padding: 4rpx 18rpx;
  border-radius: 12rpx;
}

.edit-btn {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: rgba(255,255,255,0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.edit-btn:active {
  opacity: 0.7;
}

.edit-icon {
  font-size: 30rpx;
}

.stats-card {
  display: flex;
  align-items: center;
  background: white;
  margin: -20rpx 16rpx 0;
  border-radius: 20rpx;
  padding: 28rpx 0;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.06);
  position: relative;
  z-index: 1;
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-item:active {
  opacity: 0.7;
}

.stat-num {
  font-size: 36rpx;
  font-weight: bold;
  color: #F97316;
  display: block;
}

.stat-label {
  font-size: 22rpx;
  color: #94A3B8;
  margin-top: 4rpx;
  display: block;
}

.stat-divider {
  width: 1rpx;
  height: 44rpx;
  background: #E2E8F0;
}

.member-banner {
  margin: 0 24rpx 24rpx;
  background: linear-gradient(135deg, #F59E0B, #F97316);
  border-radius: 20rpx;
  padding: 32rpx;
  text-align: center;
  box-shadow: 0 8rpx 24rpx rgba(249, 115, 22, 0.2);
}

.member-banner-text {
  color: #FFF;
  font-size: 32rpx;
  font-weight: bold;
}

.menu-section {
  padding: 24rpx 16rpx 0;
}

.section-title {
  font-size: 24rpx;
  color: #94A3B8;
  padding: 0 8rpx 12rpx;
  font-weight: 500;
}

.menu-group {
  background: white;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.04);
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28rpx 24rpx;
}

.menu-item:active {
  background: #FFF5EB;
}

.menu-item + .menu-item {
  border-top: 1rpx solid #F1F5F9;
}

.menu-left {
  display: flex;
  align-items: center;
}

.menu-icon {
  font-size: 34rpx;
  margin-right: 20rpx;
}

.menu-text {
  font-size: 30rpx;
  color: #1F2937;
}

.logout-text {
  color: #EF4444;
}

.menu-arrow {
  font-size: 40rpx;
  color: #D1D5DB;
  line-height: 1;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  z-index: 100;
}

.modal {
  background: white;
  width: 100%;
  border-radius: 32rpx 32rpx 0 0;
  max-height: 85vh;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #E2E8F0;
  flex-shrink: 0;
}

.modal-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
}

.modal-close {
  background: transparent;
  border: none;
  font-size: 40rpx;
  color: #94A3B8;
  padding: 0;
  min-width: 88rpx;
  min-height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-body {
  padding: 32rpx 24rpx;
  max-height: 60vh;
  overflow-y: auto;
}

.about-body {
  text-align: center;
}

.about-logo {
  font-size: 80rpx;
  margin-bottom: 12rpx;
}

.about-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
}

.about-version {
  font-size: 24rpx;
  color: #94A3B8;
  margin-top: 4rpx;
  display: block;
}

.about-divider {
  width: 80rpx;
  height: 4rpx;
  background: linear-gradient(to right, #F97316, #FB923C);
  border-radius: 2rpx;
  margin: 24rpx auto;
}

.about-desc {
  font-size: 26rpx;
  color: #6B7280;
  line-height: 1.6;
  text-align: left;
  display: block;
}

.about-info {
  display: flex;
  justify-content: space-between;
  padding: 12rpx 0;
}

.info-label {
  font-size: 26rpx;
  color: #94A3B8;
}

.info-value {
  font-size: 26rpx;
  color: #1F2937;
}

.form-item {
  margin-bottom: 28rpx;
}

.form-label {
  display: block;
  font-size: 26rpx;
  color: #334155;
  font-weight: 500;
  margin-bottom: 12rpx;
}

.form-input {
  width: 100%;
  padding: 22rpx 24rpx;
  border: 2rpx solid #E2E8F0;
  border-radius: 16rpx;
  font-size: 28rpx;
  background: #F8FAFC;
  box-sizing: border-box;
}

.form-input:focus {
  border-color: #F97316;
}

.modal-footer {
  display: flex;
  gap: 16rpx;
  padding: 24rpx;
  padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
  border-top: 1rpx solid #E2E8F0;
  flex-shrink: 0;
}

.modal-footer.single-btn {
  justify-content: center;
}

.btn-cancel {
  flex: 1;
  padding: 24rpx;
  border-radius: 24rpx;
  background: #F1F5F9;
  color: #64748B;
  font-size: 30rpx;
  font-weight: 600;
  border: none;
}

.btn-cancel:active {
  opacity: 0.7;
}

.btn-confirm {
  flex: 1;
  padding: 24rpx;
  border-radius: 24rpx;
  background: linear-gradient(135deg, #F97316, #FB923C);
  color: white;
  font-size: 30rpx;
  font-weight: 600;
  border: none;
  box-shadow: 0 4rpx 12rpx rgba(249,115,22,0.3);
}

.btn-confirm:active {
  opacity: 0.85;
}

.modal-footer.single-btn .btn-confirm {
  max-width: 320rpx;
}
</style>
