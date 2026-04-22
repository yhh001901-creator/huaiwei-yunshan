import { reactive, computed } from 'vue'

const CART_STORAGE_KEY = 'cart_items'

const state = reactive({
  items: JSON.parse(uni.getStorageSync(CART_STORAGE_KEY) || '[]'),
  tableNo: uni.getStorageSync('cart_table_no') || ''
})

function persist() {
  uni.setStorageSync(CART_STORAGE_KEY, JSON.stringify(state.items))
  uni.setStorageSync('cart_table_no', state.tableNo)
}

export function useCartStore() {
  const totalQuantity = computed(() => state.items.reduce((t, item) => t + item.quantity, 0))
  const totalPrice = computed(() => state.items.reduce((t, item) => t + item.price * item.quantity, 0))
  const itemCount = computed(() => state.items.length)

  function addItem(dish) {
    const existing = state.items.find(item => item.id === dish.id)
    if (existing) {
      if (existing.quantity < dish.stock) {
        existing.quantity++
      } else {
        uni.showToast({ title: '已达到库存上限', icon: 'none' })
        return
      }
    } else {
      state.items.push({
        id: dish.id,
        name: dish.name,
        price: dish.price,
        imageUrl: dish.imageUrl,
        quantity: 1,
        stock: dish.stock
      })
    }
    persist()
  }

  function removeItem(index) {
    state.items.splice(index, 1)
    persist()
  }

  function increaseQuantity(index) {
    const item = state.items[index]
    if (item.quantity < item.stock) {
      item.quantity++
      persist()
    } else {
      uni.showToast({ title: '已达到库存上限', icon: 'none' })
    }
  }

  function decreaseQuantity(index) {
    if (state.items[index].quantity > 1) {
      state.items[index].quantity--
    } else {
      removeItem(index)
      return
    }
    persist()
  }

  function clearCart() {
    state.items = []
    state.tableNo = ''
    persist()
  }

  function setTableNo(table) {
    state.tableNo = table
    persist()
  }

  function getOrderDetails() {
    return state.items.map(item => ({
      dishId: item.id,
      dishName: item.name,
      quantity: item.quantity,
      unitPrice: item.price
    }))
  }

  return {
    items: state.items,
    tableNo: state.tableNo,
    totalQuantity,
    totalPrice,
    itemCount,
    addItem,
    removeItem,
    increaseQuantity,
    decreaseQuantity,
    clearCart,
    setTableNo,
    getOrderDetails
  }
}
