<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-center mb-8">淮味云膳餐馆服务系统</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-xl font-semibold mb-4">系统功能</h2>
        <ul class="space-y-2">
          <li class="flex items-center gap-2">
            <span class="w-2 h-2 bg-green-500 rounded-full"></span>
            <span>菜品管理</span>
          </li>
          <li class="flex items-center gap-2">
            <span class="w-2 h-2 bg-blue-500 rounded-full"></span>
            <span>订单管理</span>
          </li>
          <li class="flex items-center gap-2">
            <span class="w-2 h-2 bg-purple-500 rounded-full"></span>
            <span>分类管理</span>
          </li>
        </ul>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-xl font-semibold mb-4">快速导航</h2>
        <div class="space-y-3">
          <router-link to="/pos" class="block bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 transition-colors text-center">
            进入POS系统
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// HomeView 组件逻辑
</script>