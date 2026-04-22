const BASE_URL = 'http://127.0.0.1:8083'

export const imageBaseURL = 'http://127.0.0.1:8083'

export function request(options) {
  const { url, method = 'GET', data = {}, header = {} } = options
  const token = uni.getStorageSync('token')

  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}${url}`,
      method,
      data,
      timeout: 10000,
      header: {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data.data)
          } else {
            uni.showToast({ title: res.data.message || '请求失败', icon: 'none' })
            reject(new Error(res.data.message))
          }
        } else if (res.statusCode === 401) {
          uni.removeStorageSync('token')
          uni.removeStorageSync('userInfo')
          uni.redirectTo({ url: '/pages/login/login' })
          reject(new Error('未登录'))
        } else {
          reject(new Error(`HTTP ${res.statusCode}`))
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络异常', icon: 'none' })
        reject(err)
      }
    })
  })
}

export const get = (url, data, header) => request({ url, method: 'GET', data, header })
export const post = (url, data, header) => request({ url, method: 'POST', data, header })
export const put = (url, data, header) => request({ url, method: 'PUT', data, header })
export const del = (url, data, header) => request({ url, method: 'DELETE', data, header })
