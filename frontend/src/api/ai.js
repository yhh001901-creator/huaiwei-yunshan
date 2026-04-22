import request from '../utils/request'

// 获取系统配置（包含 API Key）
export const getSystemConfig = () => {
  return request.get('/system/config')
}

// 保存系统配置
export const saveSystemConfig = (data) => {
  return request.post('/system/save', data)
}

// 触发 AI 分析订单
export const analyzeOrders = () => {
  return request.get('/ai/analyze-orders')
}
