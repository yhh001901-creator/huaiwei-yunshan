import request from '../utils/request'

// 创建订单
export const createOrder = (orderData) => {
  return request({
    method: 'POST',
    url: '/orders',
    data: orderData
  })
}

// 提交订单
export const submitOrder = (orderData) => {
  return request({
    method: 'POST',
    url: '/orders',
    data: orderData
  })
}
