import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '../utils/request'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const userRole = computed(() => userInfo.value?.role ?? 0)
  const roleName = computed(() => userInfo.value?.roleName ?? '顾客')
  const username = computed(() => userInfo.value?.username ?? '')

  function setToken(newToken) {
    token.value = newToken
    if (newToken) {
      localStorage.setItem('token', newToken)
    } else {
      localStorage.removeItem('token')
    }
  }

  function setUserInfo(info) {
    userInfo.value = info
    if (info) {
      localStorage.setItem('userInfo', JSON.stringify(info))
    } else {
      localStorage.removeItem('userInfo')
    }
  }

  async function login(loginData) {
    try {
      const response = await request.post('/auth/login', loginData)
      setToken(response.token)
      setUserInfo({
        userId: response.userId,
        username: response.username,
        phone: response.phone,
        role: response.role,
        roleName: response.roleName
      })
      return { success: true, data: response }
    } catch (error) {
      return { success: false, message: error.message || '登录失败' }
    }
  }

  async function register(registerData) {
    try {
      console.log('Register data:', registerData)
      const response = await request.post('/auth/register', registerData)
      console.log('Register response:', response)
      setToken(response.token)
      setUserInfo({
        userId: response.userId,
        username: response.username,
        phone: response.phone,
        role: response.role,
        roleName: response.roleName
      })
      return { success: true, data: response }
    } catch (error) {
      console.error('Register error:', error)
      return { success: false, message: error.message || '注册失败' }
    }
  }

  async function logout() {
    try {
      await request.post('/auth/logout')
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      setToken('')
      setUserInfo(null)
    }
  }

  async function fetchUserInfo() {
    if (!token.value) return null
    try {
      const response = await request.get('/auth/info')
      setUserInfo({
        userId: response.userId,
        username: response.username,
        phone: response.phone,
        role: response.role,
        roleName: response.roleName
      })
      return response
    } catch (error) {
      setToken('')
      setUserInfo(null)
      return null
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    userRole,
    roleName,
    username,
    setToken,
    setUserInfo,
    login,
    register,
    logout,
    fetchUserInfo
  }
})
