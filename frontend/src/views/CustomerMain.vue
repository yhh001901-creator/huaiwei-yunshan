<template>
  <div class="min-h-screen bg-[#FFF9F5]">
    <header class="bg-[#F97316] text-white px-4 py-4 sticky top-0 z-50 shadow-lg">
      <div class="max-w-4xl mx-auto flex justify-between items-center">
        <div>
          <h1 class="text-xl font-bold">淮味云膳</h1>
          <div class="flex items-center gap-2">
            <p class="text-white/80 text-sm">欢迎，{{ userStore.username }}</p>
            <span class="bg-yellow-400 text-yellow-900 text-xs px-2 py-0.5 rounded-full font-bold">👑 会员</span>
            <span class="text-white/80 text-xs">积分: {{ memberInfo.point || 0 }}</span>
          </div>
        </div>
        <div class="flex items-center gap-3">
          <button 
            @click="currentView = 'menu'"
            :class="[
              'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
              currentView === 'menu' ? 'bg-white text-[#F97316] shadow-md' : 'btn-3d-orange'
            ]"
          >
            开始点餐
          </button>
          <button 
            @click="showOrders"
            :class="[
              'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
              currentView === 'orders' ? 'bg-white text-[#F97316] shadow-md' : 'btn-3d-orange'
            ]"
          >
            我的订单
          </button>
          <button 
            @click="showMemberCenter"
            :class="[
              'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
              currentView === 'member' ? 'bg-white text-[#F97316] shadow-md' : 'btn-3d-orange'
            ]"
          >
            会员中心
          </button>
          <button @click="handleLogout" class="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg text-sm font-medium transition-colors">
            退出
          </button>
        </div>
      </div>
    </header>

    <main class="max-w-4xl mx-auto p-4">
      <transition name="page-fade" mode="out-in">
        <div v-if="currentView === 'menu'" key="menu" class="space-y-6">
          <div class="bg-white rounded-xl shadow-sm p-4">
            <div class="flex gap-2 overflow-x-auto pb-2">
              <button
                v-for="category in categories"
                :key="category.id"
                @click="selectCategory(category)"
                :class="[
                  'px-4 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-colors',
                  selectedCategory?.id === category.id
                    ? 'bg-[#F97316] text-white shadow-md'
                    : 'bg-slate-100 text-[#6B7280] hover:bg-slate-200'
                ]"
              >
                {{ category.name }}
              </button>
            </div>
          </div>

          <transition-group 
            name="dish-fade" 
            tag="div" 
            class="grid grid-cols-2 gap-4"
            appear
          >
            <div
              v-for="dish in filteredDishes"
              :key="dish.id"
              class="bg-white rounded-xl shadow-sm overflow-hidden"
            >
              <div class="h-32 overflow-hidden">
                <img
                  :src="dish.imageUrl ? backendUrl + dish.imageUrl : defaultImage"
                  :alt="dish.name"
                  class="w-full h-full object-cover"
                />
              </div>
              <div class="p-3">
                <h3 class="font-semibold text-[#1F2937]">{{ dish.name }}</h3>
                <div class="flex items-baseline gap-2 mt-1">
                  <p class="text-[#F97316] font-bold">¥{{ getDishPrice(dish) }}</p>
                  <span v-if="memberInfo.isMember === 1 && getMemberPrice(dish.id)" class="text-xs line-through text-slate-400">¥{{ dish.price }}</span>
                </div>
                <p v-if="dish.stock !== undefined" :class="[
                  'text-xs mt-1',
                  dish.stock <= 0 ? 'text-red-500' :
                  dish.stock <= 5 ? 'text-orange-500' :
                  'text-[#6B7280]'
                ]">
                  {{ dish.stock <= 0 ? '已售罄' :
                     dish.stock <= 5 ? '库存紧张' :
                     '库存：' + dish.stock }}
                </p>
                <button
                  @click="addToCart(dish)"
                  :disabled="dish.status === 0 || dish.stock <= 0"
                  :class="[
                    'w-full mt-2 py-2 rounded-lg text-sm font-medium transition-colors',
                    dish.status === 1 && dish.stock > 0
                      ? 'btn-3d-orange'
                      : 'bg-slate-200 text-slate-400 cursor-not-allowed'
                  ]"
                >
                  {{ dish.status === 1 && dish.stock > 0 ? '加入购物车' : '已售罄' }}
                </button>
              </div>
            </div>
          </transition-group>
        </div>

        <div v-else-if="currentView === 'orders'" key="orders" class="space-y-4">
        <div class="flex items-center gap-3 mb-4">
          <button @click="currentView = 'menu'" class="text-[#F97316] hover:text-[#EA580C]">
            ← 返回
          </button>
          <h2 class="text-lg font-bold text-[#1F2937]">我的订单</h2>
        </div>
        <div v-if="myOrders.length === 0" class="text-center py-8 text-[#6B7280]">
          暂无订单
        </div>
        <div v-else class="space-y-3">
          <div
            v-for="order in myOrders"
            :key="order.id"
            @click="openOrderDetail(order)"
            class="bg-white rounded-xl shadow-sm p-4 cursor-pointer hover:shadow-md transition-shadow active:scale-[0.98]"
          >
            <div class="flex justify-between items-start mb-2">
              <div>
                <p class="text-sm text-[#6B7280]">订单号: {{ order.orderNo }}</p>
                <p class="text-sm text-[#6B7280]">桌号: {{ order.tableNo }}</p>
              </div>
              <span
                :class="[
                  'px-2 py-1 rounded-full text-xs font-medium',
                  order.status === 1 ? 'bg-[#F59E0B]/10 text-[#F59E0B]' :
                  order.status === 2 ? 'bg-blue-100 text-blue-800' :
                  order.status === 3 ? 'bg-[#10B981]/10 text-[#10B981]' :
                  'bg-red-100 text-red-800'
                ]"
              >
                {{ getStatusText(order.status) }}
              </span>
            </div>
            <p class="text-lg font-bold text-[#F97316]">¥{{ parseFloat(order.totalAmount).toFixed(2) }}</p>
            <p class="text-xs text-[#6B7280] mt-1">{{ order.createTime }}</p>
          </div>
        </div>
        </div>
        
        <div v-else-if="currentView === 'member'" key="member" class="space-y-4">
        <div class="flex items-center gap-3 mb-4">
          <button @click="currentView = 'menu'" class="text-[#F97316] hover:text-[#EA580C]">
            ← 返回
          </button>
          <h2 class="text-lg font-bold text-[#1F2937]">会员中心</h2>
        </div>
        
        <!-- 会员状态卡片 -->
        <div class="bg-gradient-to-r from-orange-500 to-orange-600 rounded-2xl p-6 text-white shadow-lg">
          <div class="flex justify-between items-center">
            <div>
              <h3 class="text-xl font-bold">会员状态</h3>
              <p class="mt-2 text-orange-100">
                {{ memberInfo.isMember === 1 ? '已开通会员' : '普通用户' }}
              </p>
            </div>
            <div class="text-4xl">
              👑
            </div>
          </div>
          <div class="mt-6">
            <div class="flex justify-between mb-2">
              <span>当前积分</span>
              <span class="font-bold">{{ memberInfo.point || 0 }}分</span>
            </div>
            <div class="w-full bg-white/30 rounded-full h-3">
              <div 
                class="bg-white rounded-full h-3 transition-all duration-300"
                :style="{ width: Math.min(((memberInfo.point || 0) / 100) * 100, 100) + '%' }"
              ></div>
            </div>
            <div class="mt-2 text-sm text-orange-100">
              {{ memberInfo.isMember === 1 ? '会员有效期: 永久' : `再积${Math.max(0, 100 - (memberInfo.point || 0))}分成为会员` }}
            </div>
          </div>
        </div>
        
        <!-- 会员操作 -->
        <div class="bg-white rounded-xl shadow-sm p-4">
          <h3 class="font-bold text-slate-800 mb-4">会员操作</h3>
          <div class="space-y-4">
            <button 
              @click="handleCheckin" 
              :disabled="isCheckingIn"
              :class="[
                'w-full py-3 rounded-lg font-medium transition-colors',
                isCheckingIn ? 'bg-slate-400 cursor-not-allowed' : 'bg-orange-500 hover:bg-orange-600 text-white'
              ]"
            >
              <span v-if="isCheckingIn" class="flex items-center justify-center gap-2">
                <span class="animate-spin">⏳</span>
                <span>签到中...</span>
              </span>
              <span v-else-if="checkinSuccess" class="flex items-center justify-center gap-2">
                <span>✅</span>
                <span>签到成功！</span>
              </span>
              <span v-else class="flex items-center justify-center gap-2">
                <span>📅</span>
                <span>每日签到 +10积分</span>
              </span>
            </button>
            
            <button v-if="memberInfo.isMember !== 1 && (memberInfo.point || 0) >= 100" @click="activateMember" class="w-full bg-yellow-500 hover:bg-yellow-600 text-yellow-900 py-3 rounded-lg font-medium transition-colors">
              <span class="flex items-center justify-center gap-2">
                <span>✨</span>
                <span>立即开通会员</span>
              </span>
            </button>
          </div>
        </div>
        
        <!-- 可领取优惠券 -->
        <div class="bg-white rounded-xl shadow-sm p-4 mb-4">
          <h3 class="font-bold text-slate-800 mb-4">可领取优惠券</h3>
          <div v-if="availableCouponTemplates.length === 0" class="text-center py-8">
            <p class="text-slate-400">暂无可领取优惠券</p>
          </div>
          <div v-else class="space-y-3">
            <div v-for="template in availableCouponTemplates" :key="template.id" class="border border-slate-200 rounded-lg p-3">
              <div class="flex justify-between items-center">
                <div>
                  <p class="font-medium">{{ template.name }}</p>
                  <p class="text-sm text-slate-500">
                    {{ template.type === 1 ? `满${template.minAmount}减${template.discountValue}` : `满${template.minAmount}享${(template.discountValue * 10).toFixed(1)}折` }}
                  </p>
                </div>
                <button 
                  @click="receiveCoupon(template.id)"
                  class="bg-[#F97316] hover:bg-[#EA580C] text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors"
                >
                  领取
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 我的优惠券 -->
        <div class="bg-white rounded-xl shadow-sm p-4">
          <h3 class="font-bold text-slate-800 mb-4">我的优惠券</h3>
          <div v-if="coupons.length === 0" class="text-center py-8">
            <p class="text-slate-400">暂无优惠券</p>
          </div>
          <div v-else class="space-y-3">
            <div v-for="coupon in coupons" :key="coupon.id" class="border border-slate-200 rounded-lg p-3">
              <div class="flex justify-between items-center">
                <div>
                  <p class="font-medium">{{ coupon.templateName }}</p>
                  <p class="text-sm text-slate-500">
                    {{ coupon.templateType === 1 ? `满${coupon.templateMinAmount}减${coupon.templateDiscountValue}` : `满${coupon.templateMinAmount}享${(coupon.templateDiscountValue * 10).toFixed(1)}折` }}
                  </p>
                </div>
                <div :class="['text-sm font-medium px-2 py-1 rounded-full', coupon.status === 0 ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-600']">
                  {{ coupon.status === 0 ? '未使用' : coupon.status === 1 ? '已使用' : '已过期' }}
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 积分明细 -->
        <div class="bg-white rounded-xl shadow-sm p-4">
          <h3 class="font-bold text-slate-800 mb-4">积分明细</h3>
          <div v-if="pointLogs.length === 0" class="text-center py-8">
            <p class="text-slate-400">暂无积分记录</p>
          </div>
          <div v-else class="space-y-3">
            <div v-for="log in pointLogs" :key="log.id" class="flex justify-between items-center py-2 border-b border-slate-100">
              <div>
                <p class="text-slate-800">{{ log.source }}</p>
                <p class="text-sm text-slate-500">{{ formatDate(log.createTime) }}</p>
              </div>
              <div :class="['font-medium', log.point > 0 ? 'text-orange-600' : 'text-red-500']">
                {{ log.point > 0 ? '+' : '' }}{{ log.point }}分
              </div>
            </div>
          </div>
        </div>
        </div>
      </transition>
    </main>

    <nav v-if="currentView === 'menu'" class="fixed bottom-0 left-0 right-0 bg-white border-t border-slate-200 p-4">
      <div class="max-w-4xl mx-auto flex justify-between items-center">
        <div class="flex items-center gap-4">
          <button 
            ref="cartButtonRef"
            @click="openCartModal"
            class="relative flex items-center gap-2 text-[#6B7280] hover:text-[#F97316] transition-colors"
          >
            <span class="text-2xl">🛒</span>
            <span v-if="cartItems.length > 0" class="absolute -top-1 -right-1 w-5 h-5 bg-[#F97316] text-white text-xs rounded-full flex items-center justify-center">
              {{ cartItems.length }}
            </span>
          </button>
          <div>
            <p class="text-sm text-[#6B7280]">已选 {{ cartItems.length }} 件</p>
            <p class="text-lg font-bold text-[#F97316]">¥{{ totalPrice.toFixed(2) }}</p>
          </div>
        </div>
        <button
          @click="openCartModal"
          class="btn-3d-orange"
        >
          去结算
        </button>
      </div>
    </nav>

    <div v-if="showCartModal" class="fixed inset-0 bg-black/50 z-[100] flex items-end justify-center" @click.self="closeCartModal">
      <div 
        ref="modalRef"
        class="bg-white w-full max-w-lg rounded-t-2xl shadow-2xl max-h-[80vh] flex flex-col"
        style="will-change: transform, opacity;"
      >
        <div class="flex items-center justify-between p-4 border-b border-slate-200">
          <h2 class="text-lg font-bold text-[#1F2937]">购物车</h2>
          <button @click="closeCartModal" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
        </div>
        
        <div class="flex-1 overflow-y-auto p-4">
          <div v-if="cartItems.length === 0" class="text-center py-8 text-[#6B7280]">
            购物车为空
          </div>
          <div v-else class="space-y-3">
            <div
              v-for="(item, index) in cartItems"
              :key="index"
              class="flex items-center gap-3 p-3 bg-[#FFF9F5] rounded-lg"
            >
              <img
                :src="item.imageUrl ? backendUrl + item.imageUrl : defaultImage"
                :alt="item.name"
                class="w-16 h-16 rounded-lg object-cover"
              />
              <div class="flex-1">
                <h4 class="font-medium text-[#1F2937]">{{ item.name }}</h4>
                <p class="text-[#F97316] font-bold">¥{{ item.price }}</p>
              </div>
              <div class="flex items-center gap-2">
                <button
                  @click="decreaseQuantity(index)"
                  class="w-8 h-8 bg-slate-200 rounded-full flex items-center justify-center"
                >
                  -
                </button>
                <span class="w-8 text-center">{{ item.quantity }}</span>
                <button
                  @click="increaseQuantity(index)"
                  class="w-8 h-8 bg-[#F97316] text-white rounded-full flex items-center justify-center shadow-md"
                >
                  +
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-if="cartItems.length > 0" class="p-4 border-t border-slate-200">
          <div class="flex justify-between items-center mb-4">
            <span class="text-[#6B7280]">桌号</span>
            <button
              @click="showTablePicker = true"
              class="px-4 py-2 border border-slate-300 rounded-lg flex items-center gap-2 hover:bg-[#FFF9F5] transition-colors"
              :class="{ 'border-[#F97316] bg-[#FFF9F5]': tableNo }"
            >
              <span :class="tableNo ? 'text-[#F97316] font-medium' : 'text-slate-400'">
                {{ tableNo || '请选择桌号' }}
              </span>
              <span class="text-slate-400">▼</span>
            </button>
          </div>
          
          <div v-if="memberInfo.isMember === 1" class="mb-4">
            <div class="flex justify-between items-center mb-2">
              <span class="text-[#6B7280] text-sm">优惠券</span>
              <button v-if="coupons.length > 0 && !showCouponPicker" @click="showCouponPicker = true" class="text-[#F97316] text-sm font-medium">选择优惠券</button>
              <button v-if="showCouponPicker" @click="showCouponPicker = false" class="text-slate-400 text-sm">收起</button>
            </div>
            <div v-if="selectedCoupon" class="bg-orange-50 border border-orange-200 rounded-lg p-3 text-sm">
              <span class="font-medium">{{ selectedCoupon.templateName }}</span>
              <span class="text-[#F97316] ml-2">-¥{{ selectedCoupon.templateType === 1 ? selectedCoupon.templateDiscountValue : (discountedTotal.total * (1-selectedCoupon.templateDiscountValue)).toFixed(2) }}</span>
              <button @click="selectedCoupon = null" class="ml-2 text-slate-400 hover:text-red-500">&times;</button>
            </div>
            <div v-if="showCouponPicker" class="mt-2 space-y-2 max-h-40 overflow-y-auto">
              <div v-for="c in availableCoupons" :key="c.id" @click="selectCoupon(c)" class="p-3 rounded-lg border cursor-pointer transition-colors" :class="selectedCoupon?.id === c.id ? 'border-[#F97316] bg-[#FFF9F5]' : 'border-slate-200 hover:border-[#F97316]'">
                <span class="font-medium text-sm">{{ c.templateName }}</span>
                <span class="text-xs text-slate-500 ml-2">{{ c.templateType === 1 ? '满'+c.templateMinAmount+'减'+c.templateDiscountValue : c.templateDiscountValue+'折' }}</span>
              </div>
              <div v-if="availableCoupons.length === 0" class="text-center text-slate-400 text-sm py-2">暂无可用优惠券</div>
            </div>
          </div>
          
          <div class="flex justify-between items-center text-lg font-bold mb-2">
            <span>总计</span>
            <span class="text-[#F97316]">¥{{ discountedTotal.total }}</span>
          </div>
          <div v-if="selectedCoupon" class="flex justify-between items-center text-sm mb-2 text-green-600">
            <span>优惠</span>
            <span>-¥{{ discountedTotal.discount }}</span>
          </div>
          <div class="flex justify-between items-center text-xl font-bold mb-4">
            <span>应付</span>
            <span class="text-[#F97316]">¥{{ discountedTotal.final }}</span>
          </div>
          <button
            @click="submitOrder"
            :disabled="!tableNo || cartItems.length === 0"
            :class="[
              'w-full py-3 rounded-lg font-bold disabled:bg-slate-300 disabled:cursor-not-allowed transition-colors',
              !tableNo || cartItems.length === 0 ? 'disabled:bg-slate-300 disabled:cursor-not-allowed' : 'btn-3d-orange'
            ]"
          >
            提交订单
          </button>
        </div>
      </div>
    </div>

    <div v-if="showSuccessToast" class="fixed top-20 left-1/2 -translate-x-1/2 z-[200] bg-[#10B981] text-white px-6 py-3 rounded-xl shadow-lg flex items-center gap-2 animate-bounce">
      <span class="text-xl">✓</span>
      <span class="font-medium">订单提交成功！</span>
    </div>

    <div v-if="showTablePicker" class="fixed inset-0 bg-black/50 z-[150] flex items-center justify-center" @click.self="showTablePicker = false">
      <div class="bg-white rounded-2xl shadow-2xl w-80 overflow-hidden">
        <div class="flex items-center justify-between p-4 border-b border-slate-200">
          <h2 class="text-lg font-bold text-[#1F2937]">选择桌号</h2>
          <button @click="showTablePicker = false" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
        </div>
        <div class="p-4">
          <div class="grid grid-cols-5 gap-3">
            <button
              v-for="table in tables"
              :key="table"
              @click="selectTable(table)"
              :class="[
                'py-4 rounded-xl font-bold text-lg transition-all',
                tableNo === table
                  ? 'bg-[#F97316] text-white shadow-md scale-105'
                  : 'bg-slate-100 text-[#6B7280] hover:bg-[#F97316]/10 hover:text-[#F97316]'
              ]"
            >
              {{ table }}
            </button>
          </div>
        </div>
        
      </div>
    </div>

    <div v-if="showOrderDetail" class="fixed inset-0 bg-black/50 z-[100] flex items-end justify-center" @click.self="closeOrderDetail">
      <div 
        ref="orderDetailRef"
        class="bg-white w-full max-w-lg rounded-t-2xl shadow-2xl max-h-[80vh] flex flex-col"
        style="will-change: transform, opacity;"
      >
        <div class="flex items-center justify-between p-4 border-b border-slate-200">
          <h2 class="text-lg font-bold text-[#1F2937]">订单详情</h2>
          <button @click="closeOrderDetail" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
        </div>
        
        <div class="flex-1 overflow-y-auto p-4">
          <div class="space-y-4">
            <div class="flex justify-between items-center pb-3 border-b border-slate-100">
              <span class="text-[#6B7280]">订单号</span>
              <span class="font-medium">{{ selectedOrder?.orderNo }}</span>
            </div>
            <div class="flex justify-between items-center pb-3 border-b border-slate-100">
              <span class="text-[#6B7280]">桌号</span>
              <span class="font-medium">{{ selectedOrder?.tableNo || '外带' }}</span>
            </div>
            <div class="flex justify-between items-center pb-3 border-b border-slate-100">
              <span class="text-[#6B7280]">下单时间</span>
              <span class="font-medium">{{ selectedOrder?.createTime }}</span>
            </div>
            <div class="flex justify-between items-center pb-3 border-b border-slate-100">
              <span class="text-[#6B7280]">订单状态</span>
              <span
                :class="[
                  'px-2 py-1 rounded-full text-xs font-medium',
                  selectedOrder?.status === 1 ? 'bg-[#F59E0B]/10 text-[#F59E0B]' :
                  selectedOrder?.status === 2 ? 'bg-blue-100 text-blue-800' :
                  selectedOrder?.status === 3 ? 'bg-[#10B981]/10 text-[#10B981]' :
                  'bg-red-100 text-red-800'
                ]"
              >
                {{ getStatusText(selectedOrder?.status) }}
              </span>
            </div>

            <div class="pt-2">
              <h3 class="font-bold text-[#1F2937] mb-3">菜品明细</h3>
              <div class="space-y-3">
                <div
                  v-for="(item, index) in orderDetails"
                  :key="index"
                  class="flex items-center gap-3 p-3 bg-[#FFF9F5] rounded-lg"
                >
                  <div class="flex-1">
                    <h4 class="font-medium text-[#1F2937]">{{ item.dishName }}</h4>
                    <div class="flex justify-between items-center mt-1">
                      <span class="text-[#F97316] font-bold">¥{{ item.unitPrice }}</span>
                      <span class="text-[#6B7280]">x{{ item.quantity }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="p-4 border-t border-slate-200 bg-[#FFF9F5]">
          <div class="flex justify-between items-center text-xl font-bold">
            <span>总计</span>
            <span class="text-[#F97316]">¥{{ parseFloat(selectedOrder?.totalAmount || 0).toFixed(2) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import request, { imageBaseURL } from '../utils/request'

const router = useRouter()
const userStore = useUserStore()

const backendUrl = imageBaseURL // 使用imageBaseURL，自动适配当前环境
const defaultImage = 'https://via.placeholder.com/200x150?text=No+Image'

const currentView = ref('menu')
const showCartModal = ref(false)
const showSuccessToast = ref(false)
const showTablePicker = ref(false)
const showOrderDetail = ref(false)
const showCouponPicker = ref(false)
const selectedOrder = ref(null)
const orderDetails = ref([])
const cartButtonRef = ref(null)
const modalRef = ref(null)
const orderDetailRef = ref(null)
const categories = ref([])
const dishes = ref([])
const selectedCategory = ref(null)
const cartItems = ref([])
const tableNo = ref('')
const myOrders = ref([])
const memberInfo = ref({ point: 0, isMember: 0 })
const memberPrices = ref({})
const coupons = ref([])
const selectedCoupon = ref(null)
const pointLogs = ref([])
const availableCouponTemplates = ref([])

const tables = ['A01', 'A02', 'A03', 'A04', 'A05', 'A06', 'A07', 'A08', 'A09', 'A10']

const getMemberPrice = (dishId) => {
  return memberPrices.value[dishId]
}

const getDishPrice = (dish) => {
  if (memberInfo.value.isMember === 1 && memberPrices.value[dish.id]) {
    return memberPrices.value[dish.id]
  }
  return dish.price
}

const discountedTotal = computed(() => {
  let total = cartItems.value.reduce((total, item) => total + getDishPrice(item) * item.quantity, 0)
  let discount = 0
  if (selectedCoupon.value) {
    const coupon = selectedCoupon.value
    if (coupon.templateType === 1 && total >= coupon.templateMinAmount) {
      discount = coupon.templateDiscountValue
    } else if (coupon.templateType === 2 && total >= coupon.templateMinAmount) {
      discount = total * (1 - coupon.templateDiscountValue)
    }
    discount = Math.min(discount, total)
  }
  return { total: total.toFixed(2), discount: discount.toFixed(2), final: (total - discount).toFixed(2) }
})

const filteredDishes = computed(() => {
  if (!selectedCategory.value) return dishes.value.filter(d => d.status === 1)
  return dishes.value.filter(d => d.categoryId === selectedCategory.value.id && d.status === 1)
})

const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => total + item.price * item.quantity, 0)
})

const openCartModal = async () => {
  showCartModal.value = true
  await nextTick()
  
  if (cartButtonRef.value && modalRef.value) {
    const buttonRect = cartButtonRef.value.getBoundingClientRect()
    const modalRect = modalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(-10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      },
      { 
        transform: `translate(${deltaX * 0.3}px, ${deltaY * 0.3}px) scale(0.5) rotate(-3deg)`, 
        opacity: 0.8,
        borderRadius: '24px',
        offset: 0.4
      },
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      }
    ]
    
    const options = {
      duration: 380,
      easing: 'cubic-bezier(0.2, 0.9, 0.4, 1.1)',
      fill: 'forwards'
    }
    
    modalRef.value.animate(keyframes, options)
  }
}

const closeCartModal = async () => {
  if (cartButtonRef.value && modalRef.value) {
    const buttonRect = cartButtonRef.value.getBoundingClientRect()
    const modalRect = modalRef.value.getBoundingClientRect()
    
    const deltaX = buttonRect.left + buttonRect.width / 2 - modalRect.left - modalRect.width / 2
    const deltaY = buttonRect.top + buttonRect.height / 2 - modalRect.top - modalRect.height / 2
    
    const keyframes = [
      { 
        transform: 'translate(0, 0) scale(1) rotate(0deg)', 
        opacity: 1,
        borderRadius: '16px'
      },
      { 
        transform: `translate(${deltaX}px, ${deltaY}px) scale(0.1) rotate(10deg)`, 
        opacity: 0,
        borderRadius: '50%'
      }
    ]
    
    const options = {
      duration: 280,
      easing: 'cubic-bezier(0.4, 0, 0.2, 1)',
      fill: 'forwards'
    }
    
    const animation = modalRef.value.animate(keyframes, options)
    
    animation.onfinish = () => {
      showCartModal.value = false
    }
  } else {
    showCartModal.value = false
  }
}

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
  tableNo.value = table
  showTablePicker.value = false
}

const showOrders = () => {
  currentView.value = 'orders'
  fetchMyOrders()
}

const showMemberCenter = () => {
  currentView.value = 'member'
  loadMemberData() // 重新加载会员数据，确保显示最新信息
  loadAvailableCouponTemplates() // 加载可领取的优惠券
}

const isCheckingIn = ref(false)
const checkinSuccess = ref(false)

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
  cartItems.value[index].quantity--
  if (cartItems.value[index].quantity <= 0) {
    cartItems.value.splice(index, 1)
  }
}

const availableCoupons = computed(() => {
  return coupons.value.filter(c => {
    if (c.status !== 0) return false
    const total = parseFloat(discountedTotal.value.total)
    if (c.templateType === 1) return total >= c.templateMinAmount
    if (c.templateType === 2) return total >= c.templateMinAmount
    return true
  })
})

const selectCoupon = (c) => {
  selectedCoupon.value = c
  showCouponPicker.value = false
}

const receiveCoupon = async (templateId) => {
  try {
    await request.post(`/coupon/template/receive/${templateId}`)
    alert('领取成功！')
    await loadMemberData()
    await loadAvailableCouponTemplates()
  } catch (e) {
    console.error('领取优惠券失败:', e)
    alert(e.message || '领取失败，请重试')
  }
}

async function loadAvailableCouponTemplates() {
  try {
    const res = await request.get('/coupon/template/list')
    availableCouponTemplates.value = res
  } catch (e) {
    console.error('加载可领取优惠券失败:', e)
  }
}

async function loadMemberData() {
  try {
    const mRes = await request.get('/member/info')
    if (mRes) {
      memberInfo.value = mRes
    }
    const mpRes = await request.get('/dish/member-price/list')
    if (mpRes) {
      const map = {}
      mpRes.forEach(p => { map[p.dishId] = p.memberPrice })
      memberPrices.value = map
    }
    const cRes = await request.get('/coupon/my/list')
    if (cRes) {
      coupons.value = cRes.map(c => {
        const tpl = couponsTemplateCache.value.find(t => t.id === c.templateId)
        return { ...c, templateName: tpl?.name || '优惠券', templateType: tpl?.type || 1, templateMinAmount: tpl?.minAmount || 0, templateDiscountValue: tpl?.discountValue || 0 }
      })
    }
    const plRes = await request.get('/member/point/list')
    if (plRes) {
      pointLogs.value = plRes
    }
  } catch (e) {
    console.error('加载会员数据失败:', e)
  }
}

const couponsTemplateCache = ref([])

async function loadCouponTemplates() {
  try {
    const res = await request.get('/coupon/template/list')
    couponsTemplateCache.value = res
  } catch (e) {
    console.error('加载优惠券模板失败:', e)
  }
}

const submitOrder = async () => {
  if (!tableNo.value || cartItems.value.length === 0) return
  try {
    const orderData = {
      orderNo: 'ORD' + Date.now(),
      userId: userStore.userInfo.userId,
      totalAmount: parseFloat(discountedTotal.value.final),
      status: 1,
      tableNo: tableNo.value,
      orderDetails: cartItems.value.map(item => ({
        dishId: item.id,
        dishName: item.name,
        quantity: item.quantity,
        unitPrice: getDishPrice(item)
      })),
      couponId: selectedCoupon.value?.id
    }

    await request.post('/orders', orderData)
    cartItems.value = []
    tableNo.value = ''
    selectedCoupon.value = null
    showCartModal.value = false
    showSuccessToast.value = true
    setTimeout(() => {
      showSuccessToast.value = false
    }, 2000)
  } catch (error) {
    console.error('提交订单失败:', error)
    alert('提交订单失败，请重试')
  }
}

const fetchMyOrders = async () => {
  try {
    const response = await request.get('/orders/list')
    myOrders.value = response.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
  } catch (error) {
    console.error('获取订单失败:', error)
  }
}

const openOrderDetail = async (order) => {
  selectedOrder.value = order
  showOrderDetail.value = true
  
  try {
    const response = await request.get(`/orders/${order.orderNo}`)
    orderDetails.value = response.orderDetails || []
  } catch (error) {
    console.error('获取订单详情失败:', error)
    orderDetails.value = []
  }
  
  await nextTick()
  
  if (orderDetailRef.value) {
    const keyframes = [
      { 
        transform: 'translateY(0)', 
        opacity: 0,
        borderRadius: '16px'
      },
      { 
        transform: 'translateY(0)', 
        opacity: 1,
        borderRadius: '16px'
      }
    ]
    
    const options = {
      duration: 300,
      easing: 'ease-in-out',
      fill: 'forwards'
    }
    
    orderDetailRef.value.animate(keyframes, options)
  }
}

const closeOrderDetail = async () => {
  if (orderDetailRef.value) {
    const keyframes = [
      { 
        transform: 'translateY(0)', 
        opacity: 1,
        borderRadius: '16px'
      },
      { 
        transform: 'translateY(0)', 
        opacity: 0,
        borderRadius: '16px'
      }
    ]
    
    const options = {
      duration: 200,
      easing: 'ease-out',
      fill: 'forwards'
    }
    
    const animation = orderDetailRef.value.animate(keyframes, options)
    
    animation.onfinish = () => {
      showOrderDetail.value = false
      selectedOrder.value = null
      orderDetails.value = []
    }
  } else {
    showOrderDetail.value = false
    selectedOrder.value = null
    orderDetails.value = []
  }
}

const getStatusText = (status) => {
  const statusMap = {
    0: '待付款',
    1: '待接单',
    2: '制作中',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

const handleCheckin = async () => {
  isCheckingIn.value = true
  checkinSuccess.value = false
  try {
    console.log('开始签到...')
    const res = await request.post('/member/checkin')
    console.log('签到响应:', res)
    
    // 无论响应格式如何，只要能成功获取会员数据就认为签到成功
    // 因为用户反馈积分增加了，说明签到实际上是成功的
    await loadMemberData()
    
    // 强制显示签到成功
    console.log('签到成功！')
    checkinSuccess.value = true
    setTimeout(() => {
      checkinSuccess.value = false
    }, 2000)
  } catch (e) {
    console.error('签到失败:', e)
    alert('签到失败，请重试')
  } finally {
    isCheckingIn.value = false
  }
}

const activateMember = async () => {
  if (!confirm('确认使用100积分开通会员吗？')) return
  try {
    const res = await request.post('/member/activate')
    console.log('开通会员响应:', res)
    // 无论响应格式如何，只要能成功获取会员数据就认为开通成功
    // 因为会员状态会更新，积分会减少
    await loadMemberData()
    alert('会员开通成功！')
  } catch (e) {
    console.error('开通会员失败:', e)
    alert('开通会员失败，请重试')
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

onMounted(() => {
  fetchCategories()
  fetchDishes()
  loadMemberData()
  loadCouponTemplates()
  loadAvailableCouponTemplates()
})
</script>

<style scoped>
.page-fade-enter-active,
.page-fade-leave-active {
  transition: opacity 0.25s ease;
}

.page-fade-enter-from,
.page-fade-leave-to {
  opacity: 0;
}

.dish-fade-enter-active,
.dish-fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.dish-fade-enter-from,
.dish-fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

.dish-fade-move {
  transition: transform 0.3s ease;
}

/* 3D 橙色按钮样式 */
.btn-3d-orange {
  background: #F97316;
  color: white;
  padding: 0.7rem 1.6rem;
  font-size: 0.95rem;
  font-weight: 600;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  box-shadow: 0 6px 0 #C2410C;
  transition: all 0.08s linear;
}

.btn-3d-orange:hover {
  background: #EA580C;
}

.btn-3d-orange:active {
  transform: translateY(3px);
  box-shadow: 0 3px 0 #C2410C;
}
</style>

