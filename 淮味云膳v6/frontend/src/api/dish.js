import request from '../utils/request'

// 获取所有菜品列表
export const listDishes = () => {
  return request({
    method: 'GET',
    url: '/dish/list'
  })
}
