import { reactive, computed } from 'vue'
import { request, post, get } from '../utils/request'

const state = reactive({
  token: uni.getStorageSync('token') || '',
  userInfo: JSON.parse(uni.getStorageSync('userInfo') || 'null')
})

const isLoggedIn = computed(() => !!state.token)
const username = computed(() => state.userInfo?.username || '')
const roleName = computed(() => {
  const roleMap = { '1': '管理员', '2': '收银员', '3': '后厨', '4': '顾客' }
  return roleMap[state.userInfo?.role] || '顾客'
})

function setToken(token) {
  state.token = token
  uni.setStorageSync('token', token)
}

function setUserInfo(userInfo) {
  state.userInfo = userInfo
  uni.setStorageSync('userInfo', JSON.stringify(userInfo))
}

function login(credentials) {
  return request({
    url: '/api/auth/login',
    method: 'POST',
    data: credentials
  }).then(res => {
    if (res.token && res.userInfo) {
      setToken(res.token)
      setUserInfo(res.userInfo)
      return { success: true }
    }
    return { success: false, message: res.message || '登录失败' }
  })
}

function register(credentials) {
  return request({
    url: '/api/auth/register',
    method: 'POST',
    data: credentials
  }).then(res => {
    return { success: true }
  }).catch(err => {
    return { success: false, message: err.message || '注册失败' }
  })
}

async function refreshUserInfo() {
  try {
    const data = await get('/api/user/profile')
    setUserInfo(data)
    return data
  } catch (e) {
    console.error('刷新用户信息失败:', e)
    return null
  }
}

async function updateProfile(userData) {
  try {
    const data = await request({
      url: '/api/user/profile',
      method: 'PUT',
      data: userData
    })
    setUserInfo({ ...state.userInfo, ...data })
    return { success: true }
  } catch (e) {
    return { success: false, message: e.message || '更新失败' }
  }
}

async function updatePassword(oldPassword, newPassword) {
  try {
    await request({
      url: '/api/user/password',
      method: 'PUT',
      data: { oldPassword, newPassword }
    })
    return { success: true }
  } catch (e) {
    return { success: false, message: e.message || '修改密码失败' }
  }
}

function logout() {
  post('/api/auth/logout').catch(() => {})
  state.token = ''
  state.userInfo = null
  uni.removeStorageSync('token')
  uni.removeStorageSync('userInfo')
  uni.removeStorageSync('cart_items')
  uni.removeStorageSync('cart_table_no')
}

export function useUserStore() {
  return {
    state,
    isLoggedIn,
    username,
    roleName,
    setToken,
    setUserInfo,
    login,
    register,
    logout,
    refreshUserInfo,
    updateProfile,
    updatePassword
  }
}
