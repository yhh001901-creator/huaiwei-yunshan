<template>
  <div class="min-h-screen flex items-center justify-center font-sans">
    <!-- 主容器 - 铺满整个屏幕 -->
    <div class="w-full h-full min-h-screen bg-white p-8 md:p-12 lg:p-20">
      <!-- 顶部装饰 -->
      <div class="w-24 h-1 bg-gradient-to-r from-teal-400 to-emerald-500 rounded-full mx-auto mb-10 md:mb-12"></div>
      
      <!-- 标题区域 -->
      <div class="text-center mb-12 md:mb-16">
        <h1 class="text-4xl md:text-5xl font-bold bg-gradient-to-r from-teal-700 to-emerald-600 bg-clip-text text-transparent mb-4 tracking-tight">淮味云膳</h1>
        <p class="text-slate-600 text-lg md:text-xl">餐馆服务系统</p>
      </div>

      <!-- 表单容器 -->
      <div class="max-w-md mx-auto">
        <!-- 切换按钮 -->
        <div class="flex mb-8 bg-slate-100 rounded-lg p-1">
          <button
            @click="isRegister = false"
            :class="[
              'flex-1 py-3 px-4 rounded-lg font-medium transition-all duration-300 transform hover:translate-y-[-2px] active:translate-y-[1px]',
              isRegister ? 'bg-transparent text-slate-600' : 'bg-gradient-to-r from-teal-500 to-emerald-600 text-white shadow-md border-b-3 border-emerald-700'
            ]"
          >
            登录
          </button>
          <button
            @click="isRegister = true"
            :class="[
              'flex-1 py-3 px-4 rounded-lg font-medium transition-all duration-300 transform hover:translate-y-[-2px] active:translate-y-[1px]',
              isRegister ? 'bg-gradient-to-r from-teal-500 to-emerald-600 text-white shadow-md border-b-3 border-emerald-700' : 'bg-transparent text-slate-600'
            ]"
          >
            注册
          </button>
        </div>

        <!-- 登录表单 -->
        <form v-if="!isRegister" @submit.prevent="handleLogin" class="space-y-6">
          <!-- 用户名输入 -->
          <div class="relative">
            <label class="block text-sm font-medium text-slate-700 mb-2">用户名</label>
            <div class="relative group">
              <input
                v-model="loginForm.username"
                type="text"
                required
                class="w-full pl-4 pr-4 py-3 border border-slate-300 rounded-xl focus:ring-3 focus:ring-teal-500 focus:border-transparent transition-all duration-300 shadow-sm group-focus-within:shadow-md"
                placeholder="请输入用户名"
              />
            </div>
          </div>

          <!-- 密码输入 -->
          <div class="relative">
            <label class="block text-sm font-medium text-slate-700 mb-2">密码</label>
            <div class="relative group">
              <input
                v-model="loginForm.password"
                type="password"
                required
                class="w-full pl-4 pr-4 py-3 border border-slate-300 rounded-xl focus:ring-3 focus:ring-teal-500 focus:border-transparent transition-all duration-300 shadow-sm group-focus-within:shadow-md"
                placeholder="请输入密码"
              />
            </div>
          </div>

          <!-- 错误信息 -->
          <div v-if="errorMessage" class="bg-red-50 border-l-4 border-red-500 text-red-600 px-4 py-3 rounded-lg text-sm animate-fadeIn">
            <div class="flex items-center gap-2">
              <span>⚠️</span>
              <span>{{ errorMessage }}</span>
            </div>
          </div>

          <!-- 登录按钮 -->
          <button
            type="submit"
            :disabled="isLoading"
            class="w-full bg-gradient-to-r from-teal-500 to-emerald-600 hover:from-teal-600 hover:to-emerald-700 text-white font-semibold py-4 px-4 rounded-xl transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-3 shadow-lg hover:shadow-xl active:scale-98 transform hover:translate-y-[-2px] active:translate-y-[1px] border-b-4 border-emerald-700"
          >
            <span v-if="isLoading" class="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></span>
            {{ isLoading ? '登录中...' : '登录' }}
          </button>
        </form>

        <!-- 注册表单 -->
        <form v-else @submit.prevent="handleRegister" class="space-y-6">
          <!-- 用户名输入 -->
          <div class="relative">
            <label class="block text-sm font-medium text-slate-700 mb-2">用户名</label>
            <div class="relative group">
              <input
                v-model="registerForm.username"
                type="text"
                required
                class="w-full pl-4 pr-4 py-3 border border-slate-300 rounded-xl focus:ring-3 focus:ring-teal-500 focus:border-transparent transition-all duration-300 shadow-sm group-focus-within:shadow-md"
                placeholder="请输入用户名"
              />
            </div>
          </div>

          <!-- 密码输入 -->
          <div class="relative">
            <label class="block text-sm font-medium text-slate-700 mb-2">密码</label>
            <div class="relative group">
              <input
                v-model="registerForm.password"
                type="password"
                required
                class="w-full pl-4 pr-4 py-3 border border-slate-300 rounded-xl focus:ring-3 focus:ring-teal-500 focus:border-transparent transition-all duration-300 shadow-sm group-focus-within:shadow-md"
                placeholder="请输入密码"
              />
            </div>
          </div>

          <!-- 手机号输入 -->
          <div class="relative">
            <label class="block text-sm font-medium text-slate-700 mb-2">手机号</label>
            <div class="relative group">
              <input
                v-model="registerForm.phone"
                type="tel"
                required
                class="w-full pl-4 pr-4 py-3 border border-slate-300 rounded-xl focus:ring-3 focus:ring-teal-500 focus:border-transparent transition-all duration-300 shadow-sm group-focus-within:shadow-md"
                placeholder="请输入手机号"
              />
            </div>
          </div>

          <!-- 错误信息 -->
          <div v-if="errorMessage" class="bg-red-50 border-l-4 border-red-500 text-red-600 px-4 py-3 rounded-lg text-sm animate-fadeIn">
            <div class="flex items-center gap-2">
              <span>⚠️</span>
              <span>{{ errorMessage }}</span>
            </div>
          </div>

          <!-- 注册按钮 -->
          <button
            type="submit"
            :disabled="isLoading"
            class="w-full bg-gradient-to-r from-teal-500 to-emerald-600 hover:from-teal-600 hover:to-emerald-700 text-white font-semibold py-4 px-4 rounded-xl transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-3 shadow-lg hover:shadow-xl active:scale-98 transform hover:translate-y-[-2px] active:translate-y-[1px] border-b-4 border-emerald-700"
          >
            <span v-if="isLoading" class="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></span>
            {{ isLoading ? '注册中...' : '注册' }}
          </button>
        </form>

        <!-- 测试账号区域 -->
        <div v-if="!isRegister" class="mt-12 pt-8 border-t border-slate-100">
          <p class="text-sm text-slate-500 text-center mb-6 font-medium">测试账号</p>
          <div class="grid grid-cols-2 gap-4">
            <button @click="fillTestAccount('admin', '123456')" class="bg-gradient-to-r from-slate-50 to-slate-100 hover:from-slate-100 hover:to-slate-200 px-4 py-3 rounded-xl transition-all duration-300 shadow-md hover:shadow-lg transform hover:translate-y-[-2px] active:translate-y-[1px] border-b-3 border-slate-300">
              <span class="font-medium text-slate-800 block mb-1">管理员</span>
              <span class="text-slate-600 text-sm">admin / 123456</span>
            </button>
            <button @click="fillTestAccount('cashier', '123456')" class="bg-gradient-to-r from-slate-50 to-slate-100 hover:from-slate-100 hover:to-slate-200 px-4 py-3 rounded-xl transition-all duration-300 shadow-md hover:shadow-lg transform hover:translate-y-[-2px] active:translate-y-[1px] border-b-3 border-slate-300">
              <span class="font-medium text-slate-800 block mb-1">收银员</span>
              <span class="text-slate-600 text-sm">cashier / 123456</span>
            </button>
            <button @click="fillTestAccount('kitchen', '123456')" class="bg-gradient-to-r from-slate-50 to-slate-100 hover:from-slate-100 hover:to-slate-200 px-4 py-3 rounded-xl transition-all duration-300 shadow-md hover:shadow-lg transform hover:translate-y-[-2px] active:translate-y-[1px] border-b-3 border-slate-300">
              <span class="font-medium text-slate-800 block mb-1">后厨</span>
              <span class="text-slate-600 text-sm">kitchen / 123456</span>
            </button>
            <button @click="fillTestAccount('customer', '123456')" class="bg-gradient-to-r from-slate-50 to-slate-100 hover:from-slate-100 hover:to-slate-200 px-4 py-3 rounded-xl transition-all duration-300 shadow-md hover:shadow-lg transform hover:translate-y-[-2px] active:translate-y-[1px] border-b-3 border-slate-300">
              <span class="font-medium text-slate-800 block mb-1">顾客</span>
              <span class="text-slate-600 text-sm">customer / 123456</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const isRegister = ref(false)
const loginForm = ref({
  username: '',
  password: ''
})
const registerForm = ref({
  username: '',
  password: '',
  phone: ''
})

const isLoading = ref(false)
const errorMessage = ref('')

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    errorMessage.value = '请输入用户名和密码'
    return
  }

  isLoading.value = true
  errorMessage.value = ''

  try {
    const result = await userStore.login(loginForm.value)
    
    if (result.success) {
      const role = result.data.role
      switch (role) {
        case 0:
          router.push('/customer')
          break
        case 1:
          router.push('/admin')
          break
        case 2:
          router.push('/cashier')
          break
        case 3:
          router.push('/kitchen')
          break
        default:
          router.push('/customer')
      }
    } else {
      errorMessage.value = result.message
    }
  } catch (error) {
    errorMessage.value = '登录失败，请稍后重试'
  } finally {
    isLoading.value = false
  }
}

const handleRegister = async () => {
  if (!registerForm.value.username || !registerForm.value.password || !registerForm.value.phone) {
    errorMessage.value = '请填写所有必填字段'
    return
  }

  isLoading.value = true
  errorMessage.value = ''

  try {
    const result = await userStore.register(registerForm.value)
    
    if (result.success) {
      // 注册成功后跳转到顾客页面
      router.push('/customer')
    } else {
      errorMessage.value = result.message
    }
  } catch (error) {
    errorMessage.value = '注册失败，请稍后重试'
  } finally {
    isLoading.value = false
  }
}

const fillTestAccount = (username, password) => {
  loginForm.value.username = username
  loginForm.value.password = password
}
</script>

<style scoped>
/* 自定义动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fadeIn {
  animation: fadeIn 0.3s ease-out forwards;
}

/* 输入框聚焦效果 */
.group:focus-within input {
  box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
}

/* 按钮悬停效果 */
button:hover {
  transform: translateY(-2px);
}

button:active {
  transform: translateY(1px);
}
</style>