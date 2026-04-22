<template>
  <view class="container">
    <view class="header">
      <view class="header-inner">
        <view class="logo-circle">
          <text class="logo-icon">🍜</text>
        </view>
        <view class="title-area">
          <text class="title">淮味云膳</text>
          <text class="subtitle">餐馆服务系统</text>
        </view>
      </view>
    </view>

    <view class="form-area">
      <view class="tabs">
        <view
          class="tab"
          :class="{ active: !isRegister }"
          @tap="switchTab(false)"
        >
          <text class="tab-text">登录</text>
        </view>
        <view
          class="tab"
          :class="{ active: isRegister }"
          @tap="switchTab(true)"
        >
          <text class="tab-text">注册</text>
        </view>
      </view>

      <view class="form">
        <view class="input-group">
          <text class="label">用户名</text>
          <view class="input-wrap">
            <input
              class="input"
              :value="form.username"
              @input="onInputUsername"
              placeholder="请输入用户名"
              confirm-type="next"
            />
          </view>
        </view>

        <view class="input-group">
          <text class="label">密码</text>
          <view class="input-wrap">
            <input
              class="input"
              :value="form.password"
              @input="onInputPassword"
              type="password"
              placeholder="请输入密码"
              confirm-type="done"
            />
          </view>
        </view>

        <view v-if="isRegister" class="input-group">
          <text class="label">手机号</text>
          <view class="input-wrap">
            <input
              class="input"
              :value="form.phone"
              @input="onInputPhone"
              type="number"
              placeholder="请输入手机号"
              confirm-type="done"
            />
          </view>
        </view>

        <view v-if="errorMessage" class="error-msg">
          <text class="error-text">{{ errorMessage }}</text>
        </view>

        <view
          class="submit-btn"
          :class="{ disabled: isLoading }"
          @tap="handleSubmit"
        >
          <text class="btn-text">
            {{ isLoading ? (isRegister ? '注册中...' : '登录中...') : (isRegister ? '注册' : '登录') }}
          </text>
        </view>
      </view>

      <view v-if="!isRegister" class="test-accounts">
        <text class="section-title">快捷登录</text>
        <view class="account-grid">
          <view
            class="account-card"
            v-for="(acc, i) in testAccounts"
            :key="i"
            @tap="fillAccount(acc)"
          >
            <text class="account-icon">👤</text>
            <view class="account-text">
              <text class="account-role">{{ acc.role }}</text>
              <text class="account-info">{{ acc.username }} / 123456</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../../stores/user'

const userStore = useUserStore()

const isRegister = ref(false)
const isLoading = ref(false)
const errorMessage = ref('')

const form = ref({
  username: '',
  password: '',
  phone: ''
})

const testAccounts = [
  { username: 'admin', role: '管理员' },
  { username: 'cashier', role: '收银员' },
  { username: 'kitchen', role: '后厨' },
  { username: 'customer', role: '顾客' }
]

const switchTab = (val) => {
  isRegister.value = val
  errorMessage.value = ''
}

const onInputUsername = (e) => {
  form.value.username = e.detail.value
}

const onInputPassword = (e) => {
  form.value.password = e.detail.value
}

const onInputPhone = (e) => {
  form.value.phone = e.detail.value
}

const fillAccount = (acc) => {
  form.value.username = acc.username
  form.value.password = '123456'
}

const handleSubmit = () => {
  errorMessage.value = ''

  if (!form.value.username || !form.value.password) {
    errorMessage.value = '请填写完整信息'
    return
  }

  if (isRegister.value && !form.value.phone) {
    errorMessage.value = '请填写手机号'
    return
  }

  isLoading.value = true

  const loginData = {
    username: form.value.username,
    password: form.value.password
  }

  if (isRegister.value) {
    loginData.phone = form.value.phone
  }

  const handleLoginResult = (result) => {
    isLoading.value = false
    if (result.success) {
      uni.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => {
        uni.switchTab({ url: '/pages/index/index' })
      }, 500)
    } else {
      errorMessage.value = result.message
    }
  }

  if (isRegister.value) {
    userStore.register(loginData).then(handleLoginResult).catch(() => {
      isLoading.value = false
      errorMessage.value = '操作失败，请重试'
    })
  } else {
    userStore.login(loginData).then(handleLoginResult).catch(() => {
      isLoading.value = false
      errorMessage.value = '操作失败，请重试'
    })
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(180deg, #FFF9F5 0%, #FFF5EB 100%);
  padding: 0 32rpx;
  padding-bottom: calc(32rpx + env(safe-area-inset-bottom));
}

.header {
  padding-top: 80rpx;
  padding-bottom: 40rpx;
  text-align: center;
}

.header-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-circle {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #F97316, #FB923C);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
  box-shadow: 0 8rpx 24rpx rgba(249,115,22,0.3);
}

.logo-icon {
  font-size: 50rpx;
}

.title {
  font-size: 44rpx;
  font-weight: 800;
  color: #1F2937;
}

.subtitle {
  font-size: 26rpx;
  color: #94A3B8;
  margin-top: 4rpx;
}

.form-area {
  max-width: 680rpx;
  margin: 0 auto;
}

.tabs {
  display: flex;
  background: #F1F5F9;
  border-radius: 16rpx;
  padding: 6rpx;
  margin-bottom: 24rpx;
}

.tab {
  flex: 1;
  text-align: center;
  padding: 16rpx;
  border-radius: 12rpx;
}

.tab:active {
  opacity: 0.8;
}

.tab-text {
  font-size: 28rpx;
  color: #64748B;
  font-weight: 500;
}

.tab.active {
  background: #F97316;
  box-shadow: 0 4rpx 12rpx rgba(249,115,22,0.3);
}

.tab.active .tab-text {
  color: white;
}

.form {
  background: white;
  padding: 32rpx 24rpx;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
}

.input-group {
  margin-bottom: 24rpx;
}

.label {
  display: block;
  font-size: 26rpx;
  color: #334155;
  font-weight: 500;
  margin-bottom: 10rpx;
}

.input-wrap {
  width: 100%;
  background: #F8FAFC;
  border: 2rpx solid #E2E8F0;
  border-radius: 16rpx;
}

.input {
  width: 100%;
  padding: 22rpx 24rpx;
  font-size: 30rpx;
  box-sizing: border-box;
  border: none;
  background: transparent;
}

.error-msg {
  background: #FEF2F2;
  border-radius: 12rpx;
  padding: 18rpx 24rpx;
  margin-bottom: 24rpx;
}

.error-text {
  color: #DC2626;
  font-size: 26rpx;
}

.submit-btn {
  width: 100%;
  background: linear-gradient(135deg, #F97316, #FB923C);
  color: white;
  border: none;
  border-radius: 24rpx;
  font-size: 32rpx;
  font-weight: 600;
  padding: 26rpx;
  box-shadow: 0 8rpx 24rpx rgba(249,115,22,0.3);
  text-align: center;
}

.submit-btn:active {
  opacity: 0.85;
  transform: scale(0.98);
}

.submit-btn.disabled {
  opacity: 0.5;
}

.btn-text {
  font-size: 32rpx;
  color: white;
  font-weight: 600;
}

.test-accounts {
  margin-top: 32rpx;
}

.section-title {
  display: block;
  text-align: center;
  font-size: 24rpx;
  color: #94A3B8;
  margin-bottom: 16rpx;
  font-weight: 500;
}

.account-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12rpx;
}

.account-card {
  background: white;
  padding: 20rpx;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.account-card:active {
  background: #FFF5EB;
  transform: scale(0.97);
}

.account-icon {
  font-size: 32rpx;
}

.account-role {
  display: block;
  font-size: 26rpx;
  font-weight: 600;
  color: #1F2937;
}

.account-info {
  display: block;
  font-size: 20rpx;
  color: #94A3B8;
}
</style>
