<template>
  <div class="flex h-screen overflow-hidden text-on-background">
    <aside class="h-full w-[15%] fixed left-0 top-0 bg-slate-100 dark:bg-slate-800 flex flex-col py-8 z-50">
      <div class="px-6 mb-10">
        <h1 class="text-xl font-bold text-teal-900 dark:text-teal-100 mb-1">淮味云膳</h1>
        <p class="text-xs text-on-surface-variant font-medium opacity-70">收银员端</p>
      </div>
      <nav class="flex-1 space-y-2">
        <router-link 
          to="/cashier"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/cashier' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">🍽️</span>
          <span>点餐收银</span>
        </router-link>
        <router-link 
          to="/orders"
          class="flex items-center gap-4 px-6 py-4 text-slate-600 dark:text-slate-400 font-manrope text-sm font-medium hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors active:scale-95 duration-200"
          :class="{ 'bg-teal-100 dark:bg-teal-900/40 text-teal-900 dark:text-teal-100 border-l-4 border-teal-700': $route.path === '/orders' }"
        >
          <span class="w-6 h-6 flex items-center justify-center">📋</span>
          <span>订单管理</span>
        </router-link>
      </nav>
      <div class="px-6 mt-auto">
        <div class="flex items-center gap-3 py-3 px-4 bg-surface-container rounded-xl">
          <div class="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
            <span class="text-primary font-semibold">{{ userStore.username?.charAt(0) || '收' }}</span>
          </div>
          <div class="flex-1">
            <p class="text-sm font-medium">{{ userStore.username }}</p>
            <p class="text-xs text-on-surface-variant">{{ userStore.roleName }}</p>
          </div>
          <button @click="handleLogout" class="text-red-500 hover:text-red-600 text-sm">退出</button>
        </div>
      </div>
    </aside>

    <main class="h-full w-[85%] ml-[15%] bg-slate-50 dark:bg-slate-900 p-8 overflow-y-auto">
      <header class="mb-8 flex justify-between items-start">
        <div>
          <h2 class="text-2xl font-bold text-on-surface">点餐收银</h2>
          <p class="text-sm text-on-surface-variant mt-1">为顾客点餐并结账</p>
        </div>
        <div class="flex items-center gap-2">
          <input v-model="memberPhone" @keyup.enter="searchMember" type="text" placeholder="输入会员手机号" class="px-3 py-2 border border-slate-300 rounded-lg text-sm w-48 focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none">
          <button @click="searchMember" class="bg-teal-600 hover:bg-teal-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">查询</button>
          <button v-if="currentMember" @click="clearMember" class="text-red-500 hover:text-red-600 text-sm">清除</button>
        </div>
      </header>

      <div v-if="currentMember" class="mb-6 bg-teal-50 dark:bg-teal-900/20 rounded-xl p-4 flex items-center gap-4">
        <span class="text-2xl">👑</span>
        <div>
          <p class="font-semibold text-teal-900 dark:text-teal-100">{{ currentMember.nickname || currentMember.phone }}</p>
          <p class="text-sm text-teal-700 dark:text-teal-300">积分: {{ currentMember.point }} | 会员有效期至: {{ currentMember.memberExpire || '永久' }}</p>
        </div>
      </div>

      <div class="grid grid-cols-3 gap-6">
        <div class="col-span-2 space-y-6">
          <div class="bg-white rounded-xl shadow-sm p-4">
            <div class="flex gap-2 overflow-x-auto pb-2">
              <button
                v-for="category in categories"
                :key="category.id"
                @click="selectCategory(category)"
                :class="[
                  'px-4 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-colors',
                  selectedCategory?.id === category.id
                    ? 'bg-teal-600 text-white'
                    : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                ]"
              >
                {{ category.name }}
              </button>
            </div>
          </div>

          <div class="grid grid-cols-3 gap-4">
            <div
              v-for="dish in filteredDishes"
              :key="dish.id"
              :class="[
                'bg-white rounded-xl shadow-sm overflow-hidden transition-all',
                dish.status === 1 ? 'cursor-pointer hover:shadow-md' : 'opacity-60'
              ]"
              @click="dish.status === 1 && addToCart(dish)"
            >
              <div class="h-32 overflow-hidden relative">
                <img
                  :src="dish.imageUrl ? backendUrl + dish.imageUrl : defaultImage"
                  :alt="dish.name"
                  class="w-full h-full object-cover"
                />
                <div v-if="dish.status === 0" class="absolute inset-0 bg-black/40 flex items-center justify-center">
                  <span class="text-white font-bold">售罄</span>
                </div>
              </div>
              <div class="p-3">
                <h3 class="font-semibold text-slate-800 truncate">{{ dish.name }}</h3>
                <div class="flex justify-between items-center mt-2">
                  <div class="flex items-baseline gap-2">
                    <span class="text-teal-600 font-bold">¥{{ getDishPrice(dish) }}</span>
                    <span v-if="currentMember?.isMember === 1 && memberPrices[dish.id]" class="text-xs line-through text-slate-400">¥{{ dish.price }}</span>
                  </div>
                  <button
                    v-if="dish.status === 1"
                    @click="addToCart(dish)"
                    class="w-8 h-8 bg-teal-600 text-white rounded-full flex items-center justify-center hover:bg-teal-700"
                  >
                    +
                  </button>
                </div>
                <p class="text-xs text-slate-400 mt-1">库存: {{ dish.stock }}</p>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-sm p-4 h-fit sticky top-8">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-bold text-slate-800">当前订单</h3>
            <button
              @click="showTableModal = true"
              class="flex items-center gap-2 bg-teal-50 px-3 py-1.5 rounded-lg border border-teal-200 hover:bg-teal-100 transition-colors"
            >
              <span class="text-teal-700 text-sm">桌号:</span>
              <span class="text-teal-700 font-bold">{{ currentTable || '未选' }}</span>
            </button>
          </div>

          <div class="space-y-3 max-h-80 overflow-y-auto">
            <div v-if="cartItems.length === 0" class="text-center py-8 text-slate-400">
              点击菜品添加到订单
            </div>
            <div
              v-for="(item, index) in cartItems"
              :key="index"
              class="flex items-center gap-3 p-2 bg-slate-50 rounded-lg"
            >
              <img
                :src="item.imageUrl ? backendUrl + item.imageUrl : defaultImage"
                :alt="item.name"
                class="w-12 h-12 rounded-lg object-cover"
              />
              <div class="flex-1">
                <h4 class="text-sm font-medium text-slate-800">{{ item.name }}</h4>
                <div class="flex items-center gap-2 mt-1">
                  <button
                    @click="decreaseQuantity(index)"
                    class="w-6 h-6 bg-slate-200 rounded flex items-center justify-center text-sm"
                  >
                    -
                  </button>
                  <span class="text-sm">{{ item.quantity }}</span>
                  <button
                    @click="increaseQuantity(index)"
                    class="w-6 h-6 bg-teal-600 text-white rounded flex items-center justify-center text-sm"
                  >
                    +
                  </button>
                </div>
              </div>
              <div class="flex items-baseline gap-2">
                  <span class="text-teal-600 font-bold">¥{{ (getDishPrice(item) * item.quantity).toFixed(2) }}</span>
                </div>
            </div>
          </div>

          <div class="mt-4 pt-4 border-t border-slate-200">
            <div v-if="memberCoupons.length > 0" class="mb-3">
              <div class="flex justify-between items-center mb-2">
                <span class="text-slate-500 text-sm">优惠券</span>
                <button v-if="!showCouponPicker" @click="showCouponPicker = true" class="text-teal-600 text-sm font-medium">选择</button>
                <button v-if="showCouponPicker" @click="showCouponPicker = false" class="text-slate-400 text-sm">收起</button>
              </div>
              <div v-if="selectedCoupon" class="bg-teal-50 rounded-lg p-2 text-sm flex justify-between items-center">
                <span class="font-medium">{{ selectedCoupon.templateName }}</span>
                <span class="text-teal-600">-¥{{ cashierTotal.discount }}</span>
                <button @click="selectedCoupon = null" class="text-slate-400">&times;</button>
              </div>
              <div v-if="showCouponPicker" class="space-y-1 max-h-32 overflow-y-auto">
                <div v-for="c in availableCoupons" :key="c.id" @click="selectCoupon(c)" class="p-2 rounded border cursor-pointer text-sm" :class="selectedCoupon?.id === c.id ? 'border-teal-500 bg-teal-50' : 'border-slate-200 hover:border-teal-500'">
                  {{ c.templateName }}
                </div>
              </div>
            </div>
            <div class="flex justify-between items-center text-lg font-bold mb-1">
              <span>总计</span>
              <span class="text-teal-600">¥{{ cashierTotal.total }}</span>
            </div>
            <div v-if="selectedCoupon" class="flex justify-between items-center text-sm text-green-600 mb-1">
              <span>优惠</span>
              <span>-¥{{ cashierTotal.discount }}</span>
            </div>
            <div class="flex justify-between items-center text-xl font-bold mb-4">
              <span>应付</span>
              <span class="text-teal-600">¥{{ cashierTotal.final }}</span>
            </div>
            <button
              @click="checkout"
              :disabled="cartItems.length === 0 || !currentTable"
              class="w-full py-3 bg-green-600 text-white rounded-lg font-bold hover:bg-green-700 disabled:bg-slate-300 disabled:cursor-not-allowed transition-colors"
            >
              💳 结账
            </button>
          </div>
        </div>
      </div>
    </main>

    <div v-if="showTableModal" class="fixed inset-0 bg-black/60 z-[100] flex items-center justify-center">
      <div class="bg-white rounded-2xl shadow-2xl w-[500px] overflow-hidden">
        <div class="px-6 py-4 border-b border-slate-200 flex justify-between items-center">
          <h2 class="text-xl font-bold text-slate-800">选择桌号</h2>
          <button @click="showTableModal = false" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
        </div>
        <div class="p-6">
          <div class="grid grid-cols-5 gap-3">
            <button
              v-for="table in tables"
              :key="table"
              @click="selectTable(table)"
              :class="[
                'py-3 rounded-xl font-bold text-lg transition-all',
                currentTable === table
                  ? 'bg-teal-600 text-white shadow-md'
                  : 'bg-slate-100 text-slate-700 hover:bg-teal-100'
              ]"
            >
              {{ table }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import request, { imageBaseURL } from '../utils/request'

const router = useRouter()
const userStore = useUserStore()

const backendUrl = imageBaseURL
const defaultImage = 'https://via.placeholder.com/200x150?text=No+Image'

const categories = ref([])
const dishes = ref([])
const selectedCategory = ref(null)
const cartItems = ref([])
const currentTable = ref('')
const showTableModal = ref(false)
const showSuccess = ref(false)
const memberPhone = ref('')
const currentMember = ref(null)
const memberPrices = ref({})
const memberCoupons = ref([])
const selectedCoupon = ref(null)
const showMemberSearch = ref(false)

const tables = ref([
  'A01', 'A02', 'A03', 'A04', 'A05', 'A06', 'A07', 'A08', 'A09', 'A10',
  'B01', 'B02', 'B03', 'B04', 'B05',
  'V01', 'V02', 'V03'
])

const filteredDishes = computed(() => {
  if (!selectedCategory.value) return dishes.value
  return dishes.value.filter(d => d.categoryId === selectedCategory.value.id)
})

const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => total + item.price * item.quantity, 0)
})

const fetchCategories = async () => {
  try {
    const response = await request.get('/category/list')
    categories.value = response
    if (categories.value.length > 0) {
      selectedCategory.value = categories.value[0]
    }
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

const fetchDishes = async () => {
  try {
    const response = await request.get('/dish/list')
    dishes.value = response
  } catch (error) {
    console.error('获取菜品失败:', error)
  }
}

const selectCategory = (category) => {
  selectedCategory.value = category
}

const selectTable = (table) => {
  currentTable.value = table
  showTableModal.value = false
}

const addToCart = (dish) => {
  const existing = cartItems.value.find(item => item.id === dish.id)
  if (existing) {
    existing.quantity++
  } else {
    cartItems.value.push({ ...dish, quantity: 1 })
  }
}

const increaseQuantity = (index) => {
  cartItems.value[index].quantity++
}

const decreaseQuantity = (index) => {
  if (cartItems.value[index].quantity > 1) {
    cartItems.value[index].quantity--
  } else {
    cartItems.value.splice(index, 1)
  }
}

const checkout = async () => {
  if (cartItems.value.length === 0 || !currentTable.value) return

  try {
    const orderData = {
      orderNo: 'ORD' + Date.now(),
      userId: userStore.userInfo?.userId || 1,
      totalAmount: parseFloat(cashierTotal.value.total),
      status: 1,
      tableNo: currentTable.value,
      orderDetails: cartItems.value.map(item => ({
        dishId: item.id,
        dishName: item.name,
        quantity: item.quantity,
        unitPrice: getDishPrice(item)
      }))
    }

    await request.post('/orders', orderData)
    showSuccess.value = true
    setTimeout(() => { showSuccess.value = false }, 2000)
    cartItems.value = []
    currentTable.value = ''
  } catch (error) {
    console.error('结账失败:', error)
    alert('结账失败，请重试')
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

async function searchMember() {
  if (!memberPhone.value) return
  try {
    const users = await request.get('/user/list')
    const user = users.find(u => u.phone === memberPhone.value)
    if (user) {
      const mRes = await request.get('/member/info')
      if (mRes && mRes.userId === user.id) {
        currentMember.value = mRes
        const mpRes = await request.get('/dish/member-price/list')
        if (mpRes) {
          const map = {}
          mpRes.forEach(p => { map[p.dishId] = p.memberPrice })
          memberPrices.value = map
        }
        const cRes = await request.get('/coupon/my/list')
        if (cRes) {
          const tplRes = await request.get('/coupon/template/list')
          memberCoupons.value = cRes.map(c => {
            const tpl = tplRes.find(t => t.id === c.templateId)
            return { ...c, templateName: tpl?.name || '优惠券', templateType: tpl?.type || 1, templateMinAmount: tpl?.minAmount || 0, templateDiscountValue: tpl?.discountValue || 0 }
          })
        }
      }
    } else {
      currentMember.value = null
    }
  } catch (e) {
    console.error('查询会员失败:', e)
  }
}

function clearMember() {
  currentMember.value = null
  memberPhone.value = ''
  memberPrices.value = {}
  memberCoupons.value = []
  selectedCoupon.value = null
}

function getDishPrice(dish) {
  if (currentMember.value?.isMember === 1 && memberPrices.value[dish.id]) {
    return memberPrices.value[dish.id]
  }
  return dish.price
}

const cashierTotal = computed(() => {
  let total = cartItems.value.reduce((sum, item) => sum + getDishPrice(item) * item.quantity, 0)
  let discount = 0
  if (selectedCoupon.value) {
    const c = selectedCoupon.value
    if (c.templateType === 1 && total >= c.templateMinAmount) discount = c.templateDiscountValue
    else if (c.templateType === 2 && total >= c.templateMinAmount) discount = total * (1 - c.templateDiscountValue)
    discount = Math.min(discount, total)
  }
  return { total: total.toFixed(2), discount: discount.toFixed(2), final: (total - discount).toFixed(2) }
})

const availableCoupons = computed(() => {
  return memberCoupons.value.filter(c => {
    if (c.status !== 0) return false
    const total = parseFloat(cashierTotal.value.total)
    return total >= (c.templateMinAmount || 0)
  })
})

function selectCoupon(c) {
  selectedCoupon.value = c
}

onMounted(() => {
  fetchCategories()
  fetchDishes()
})
</script>
