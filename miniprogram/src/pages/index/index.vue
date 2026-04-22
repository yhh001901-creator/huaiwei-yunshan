<template>
  <view class="container">
    <view class="header">
      <view class="header-left">
        <text class="header-title">淮味云膳</text>
        <text class="header-subtitle">欢迎，{{ username }}</text>
      </view>
    </view>

    <view v-if="loading" class="loading-container">
      <text>加载中...</text>
    </view>

    <view v-else-if="error" class="error-container">
      <text class="error-text">加载失败</text>
      <button class="retry-btn" @tap="loadData">重试</button>
    </view>

    <view v-else class="content">
      <view class="category-bar">
        <scroll-view scroll-x class="category-scroll" :show-scrollbar="false">
          <view class="category-list">
            <view
              v-for="cat in categories"
              :key="cat.id"
              class="category-item"
              :class="{ active: selectedCategory?.id === cat.id }"
              @tap="selectCategory(cat)"
            >
              <text class="category-text">{{ cat.name }}</text>
            </view>
          </view>
        </scroll-view>
      </view>

      <view v-if="filteredDishes.length === 0" class="empty-container">
        <text class="empty-text">暂无菜品</text>
      </view>

      <view v-else class="dish-grid">
        <view v-for="dish in filteredDishes" :key="dish.id" class="dish-card">
          <image
            class="dish-image"
            :src="getImageUrl(dish)"
            mode="aspectFill"
            lazy-load
            @error="onImageError(dish)"
          ></image>
          <view class="dish-info">
            <text class="dish-name">{{ dish.name }}</text>
            <text class="dish-price">¥{{ dish.price }}</text>
            <text class="dish-stock" :class="dish.stock <= 0 ? 'out' : dish.stock <= 5 ? 'low' : ''">
              {{ dish.stock <= 0 ? '已售罄' : dish.stock <= 5 ? '库存紧张' : '库存：' + dish.stock }}
            </text>
            <button
              class="add-btn"
              :class="{ disabled: dish.status === 0 || dish.stock <= 0 }"
              :disabled="dish.status === 0 || dish.stock <= 0"
              @tap="addToCart(dish)"
            >
              <text>{{ dish.status === 1 && dish.stock > 0 ? '加入购物车' : '已售罄' }}</text>
            </button>
          </view>
        </view>
      </view>
    </view>

    <view v-if="showCart" class="modal-overlay" @tap="showCart = false">
      <view class="modal" @tap.stop>
        <view class="modal-header">
          <text class="modal-title">购物车</text>
          <button class="modal-close" @tap="showCart = false">&times;</button>
        </view>
        <scroll-view scroll-y class="modal-body">
          <view v-if="cartItems.length === 0" class="empty-cart">
            <text>购物车为空</text>
          </view>
          <view v-for="(item, index) in cartItems" :key="index" class="cart-item">
            <image
              class="cart-item-img"
              :src="item._imageLoaded ? item.imageUrl : placeholderImage"
              mode="aspectFill"
              @error="onCartItemImageError(index)"
              @load="onCartItemImageLoad(index)"
            ></image>
            <view class="cart-item-info">
              <text class="cart-item-name">{{ item.name }}</text>
              <text class="cart-item-price">¥{{ item.price }}</text>
            </view>
            <view class="cart-item-qty">
              <view class="qty-btn" @tap="cartStore.decreaseQuantity(index)">
                <text>-</text>
              </view>
              <text class="qty-num">{{ item.quantity }}</text>
              <view class="qty-btn plus" @tap="cartStore.increaseQuantity(index)">
                <text>+</text>
              </view>
            </view>
          </view>
        </scroll-view>
        <view v-if="cartItems.length > 0" class="modal-footer">
          <view class="table-selector" @tap="showTablePicker = true">
            <text :class="tableNo ? 'selected' : 'placeholder'">{{ tableNo || '请选择桌号' }}</text>
            <text class="arrow">▼</text>
          </view>
          <view class="total-row">
            <text>总计</text>
            <text class="total-amount">¥{{ cartTotalPrice.toFixed(2) }}</text>
          </view>
          <button class="submit-btn" :disabled="!tableNo" @tap="submitOrder">提交订单</button>
        </view>
      </view>
    </view>

    <view v-if="showTablePicker" class="modal-overlay" @tap="showTablePicker = false">
      <view class="table-modal" @tap.stop>
        <view class="modal-header">
          <text class="modal-title">选择桌号</text>
          <button class="modal-close" @tap="showTablePicker = false">&times;</button>
        </view>
        <view class="table-grid">
          <view
            v-for="t in tables"
            :key="t"
            class="table-btn"
            :class="{ active: tableNo === t }"
            @tap="selectTable(t)"
          >
            <text>{{ t }}</text>
          </view>
        </view>
      </view>
    </view>

    <view v-if="cartItemCount > 0" class="cart-bar" :class="{ active: showCart }">
      <view class="cart-left" @tap="showCart = true">
        <text class="cart-icon">🛒</text>
        <view class="cart-badge">
          <text>{{ cartItemCount }}</text>
        </view>
        <view class="cart-info">
          <text class="cart-count">已选 {{ cartItemCount }} 件</text>
          <text class="cart-total">¥{{ cartTotalPrice.toFixed(2) }}</text>
        </view>
      </view>
      <button class="checkout-btn" @tap="showCart = true">去结算</button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '../../stores/user'
import { useCartStore } from '../../stores/cart'
import { get, post, imageBaseURL } from '../../utils/request'

const userStore = useUserStore()
const cartStore = useCartStore()

const username = computed(() => userStore.username || '')
const loading = ref(true)
const error = ref(false)

const categories = ref([])
const dishes = ref([])
const selectedCategory = ref(null)

const showCart = ref(false)
const showTablePicker = ref(false)

const tables = ['A01', 'A02', 'A03', 'A04', 'A05', 'A06', 'A07', 'A08', 'A09', 'A10']

const cartItems = computed(() => cartStore.items)
const cartItemCount = computed(() => cartStore.totalQuantity)
const cartTotalPrice = computed(() => cartStore.totalPrice)
const tableNo = computed(() => cartStore.tableNo)

const filteredDishes = computed(() => {
  if (!selectedCategory.value) return dishes.value.filter(d => d.status === 1)
  return dishes.value.filter(d => d.categoryId === selectedCategory.value.id && d.status === 1)
})

const placeholderImage = '/static/placeholder.svg'

function getImageUrl(dish) {
  if (!dish.imageUrl) return placeholderImage
  const url = dish.imageUrl.startsWith('/') ? dish.imageUrl : '/' + dish.imageUrl
  return imageBaseURL + url
}

function onImageError(dish) {
  dish._imageError = true
}

function onCartItemImageError(index) {
  cartStore.items[index]._imageLoaded = false
}

function onCartItemImageLoad(index) {
  cartStore.items[index]._imageLoaded = true
}

async function loadData() {
  loading.value = true
  error.value = false
  try {
    const [catData, dishData] = await Promise.all([
      get('/api/category/list'),
      get('/api/dish/list')
    ])
    categories.value = catData || []
    dishes.value = (dishData || []).map(d => ({
      ...d,
      _imageError: false
    }))
    if (categories.value.length > 0) {
      selectedCategory.value = categories.value[0]
    }
  } catch (e) {
    console.error('加载数据失败:', e)
    error.value = true
  } finally {
    loading.value = false
  }
}

function selectCategory(cat) {
  selectedCategory.value = cat
}

function addToCart(dish) {
  cartStore.addItem(dish)
  uni.showToast({ title: '已加入购物车', icon: 'success', duration: 1000 })
}

function selectTable(t) {
  cartStore.setTableNo(t)
  showTablePicker.value = false
}

async function submitOrder() {
  if (!tableNo.value || cartItems.value.length === 0) return
  try {
    const orderData = {
      userId: userStore.state.userInfo?.userId,
      totalAmount: cartTotalPrice.value,
      tableNo: tableNo.value,
      orderDetails: cartStore.getOrderDetails()
    }
    await post('/api/orders', orderData)
    cartStore.clearCart()
    showCart.value = false
    uni.showToast({ title: '订单提交成功！', icon: 'success' })
  } catch (e) {
    console.error('提交订单失败:', e)
    uni.showToast({ title: '提交订单失败', icon: 'none' })
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #FFF9F5;
  padding-bottom: env(safe-area-inset-bottom);
}

.header {
  background: #F97316;
  padding: 24rpx 24rpx;
  padding-top: calc(24rpx + env(safe-area-inset-top));
}

.header-title {
  font-size: 36rpx;
  font-weight: bold;
  color: white;
}

.header-subtitle {
  font-size: 24rpx;
  color: rgba(255,255,255,0.8);
}

.loading-container,
.error-container,
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.error-text,
.empty-text {
  font-size: 30rpx;
  color: #94A3B8;
  margin-bottom: 24rpx;
}

.retry-btn {
  background: #F97316;
  color: white;
  padding: 16rpx 48rpx;
  border-radius: 24rpx;
  font-size: 28rpx;
  border: none;
}

.category-bar {
  background: white;
  padding: 12rpx 16rpx;
  position: sticky;
  top: 0;
  z-index: 10;
}

.category-scroll {
  white-space: nowrap;
}

.category-list {
  display: inline-flex;
  gap: 12rpx;
}

.category-item {
  padding: 12rpx 24rpx;
  border-radius: 16rpx;
  background: #F1F5F9;
  white-space: nowrap;
  flex-shrink: 0;
  transition: all 0.2s;
}

.category-item:active {
  transform: scale(0.95);
}

.category-item.active {
  background: #F97316;
}

.category-text {
  font-size: 26rpx;
  color: #6B7280;
}

.category-item.active .category-text {
  color: white;
}

.dish-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16rpx;
  padding: 16rpx;
  padding-bottom: 180rpx;
}

.dish-card {
  background: white;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
}

.dish-card:active {
  transform: scale(0.98);
}

.dish-image {
  width: 100%;
  height: 240rpx;
  background: #F1F5F9;
}

.dish-info {
  padding: 16rpx;
}

.dish-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #1F2937;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dish-price {
  font-size: 32rpx;
  color: #F97316;
  font-weight: bold;
  margin-top: 8rpx;
  display: block;
}

.dish-stock {
  font-size: 22rpx;
  color: #6B7280;
  margin-top: 8rpx;
  display: block;
}

.dish-stock.low {
  color: #F97316;
}

.dish-stock.out {
  color: #EF4444;
}

.add-btn {
  width: 100%;
  padding: 12rpx;
  border-radius: 16rpx;
  background: #F97316;
  color: white;
  font-size: 26rpx;
  font-weight: 600;
  margin-top: 16rpx;
  border: none;
  line-height: 1.5;
}

.add-btn:active {
  opacity: 0.8;
}

.add-btn.disabled {
  background: #E2E8F0;
  color: #94A3B8;
}

.cart-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: white;
  border-top: 1rpx solid #E2E8F0;
  padding: 20rpx 24rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 50;
}

.cart-bar.active {
  display: none;
}

.cart-left {
  display: flex;
  align-items: center;
  position: relative;
  min-width: 0;
}

.cart-icon {
  font-size: 48rpx;
  margin-right: 12rpx;
}

.cart-badge {
  position: absolute;
  top: -4rpx;
  left: 24rpx;
  background: #EF4444;
  color: white;
  font-size: 18rpx;
  min-width: 32rpx;
  height: 32rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6rpx;
}

.cart-info {
  min-width: 0;
}

.cart-count {
  font-size: 22rpx;
  color: #6B7280;
  display: block;
}

.cart-total {
  font-size: 28rpx;
  color: #F97316;
  font-weight: bold;
  display: block;
}

.checkout-btn {
  background: #F97316;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 24rpx;
  font-size: 28rpx;
  font-weight: 600;
  border: none;
  flex-shrink: 0;
}

.checkout-btn:active {
  opacity: 0.8;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  z-index: 100;
}

.modal {
  background: white;
  width: 100%;
  border-radius: 32rpx 32rpx 0 0;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #E2E8F0;
}

.modal-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
}

.modal-close {
  background: transparent;
  border: none;
  font-size: 40rpx;
  color: #94A3B8;
  padding: 0;
  min-width: 88rpx;
  min-height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-body {
  flex: 1;
  padding: 24rpx;
  max-height: 50vh;
}

.empty-cart {
  text-align: center;
  padding: 80rpx 0;
  color: #94A3B8;
  font-size: 28rpx;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 20rpx;
  background: #FFF9F5;
  border-radius: 16rpx;
  margin-bottom: 12rpx;
}

.cart-item-img {
  width: 100rpx;
  height: 100rpx;
  border-radius: 12rpx;
  background: #F1F5F9;
  flex-shrink: 0;
}

.cart-item-info {
  flex: 1;
  min-width: 0;
}

.cart-item-name {
  font-size: 28rpx;
  color: #1F2937;
  font-weight: 500;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.cart-item-price {
  font-size: 28rpx;
  color: #F97316;
  font-weight: bold;
}

.cart-item-qty {
  display: flex;
  align-items: center;
  gap: 12rpx;
  flex-shrink: 0;
}

.qty-btn {
  width: 56rpx;
  height: 56rpx;
  border-radius: 50%;
  background: #E2E8F0;
  border: none;
  font-size: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.qty-btn:active {
  opacity: 0.7;
}

.qty-btn.plus {
  background: #F97316;
  color: white;
}

.qty-btn text {
  line-height: 1;
}

.qty-num {
  font-size: 28rpx;
  min-width: 40rpx;
  text-align: center;
  font-weight: 600;
}

.modal-footer {
  padding: 24rpx;
  padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
  border-top: 1rpx solid #E2E8F0;
}

.table-selector {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 24rpx;
  border: 1rpx solid #E2E8F0;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  font-size: 28rpx;
}

.table-selector .selected {
  color: #F97316;
  font-weight: 600;
}

.table-selector .placeholder {
  color: #94A3B8;
}

.total-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 32rpx;
  margin-bottom: 20rpx;
}

.total-amount {
  color: #F97316;
  font-weight: bold;
}

.submit-btn {
  width: 100%;
  padding: 24rpx;
  border-radius: 24rpx;
  background: #F97316;
  color: white;
  font-size: 32rpx;
  font-weight: bold;
  border: none;
}

.submit-btn[disabled] {
  background: #E2E8F0;
  color: #94A3B8;
}

.table-modal {
  background: white;
  width: 100%;
  border-radius: 32rpx 32rpx 0 0;
  padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
}

.table-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16rpx;
  padding: 24rpx;
}

.table-btn {
  padding: 20rpx 12rpx;
  border-radius: 16rpx;
  background: #F1F5F9;
  text-align: center;
  font-size: 28rpx;
}

.table-btn:active {
  opacity: 0.7;
}

.table-btn.active {
  background: #F97316;
  color: white;
}
</style>
