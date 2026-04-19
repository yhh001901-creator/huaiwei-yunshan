import axios from 'axios'

// 根据环境设置 baseURL
// 开发环境：使用代理 /api
// 生产环境（APK）：使用服务器公网地址
const baseURL = import.meta.env.DEV
  ? '/api'
  : 'http://47.116.30.247:8083/api'

const request = axios.create({
  baseURL: baseURL,
  timeout: 120000
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    } else if (res.code === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/#/login'
      return Promise.reject(new Error(res.msg || '登录已过期'))
    } else {
      console.error(res.msg || '未知错误')
      return Promise.reject(new Error(res.msg || '未知错误'))
    }
  },
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/#/login'
    }
    console.error('网络请求失败，请稍后重试')
    return Promise.reject(error)
  }
)

export default request
