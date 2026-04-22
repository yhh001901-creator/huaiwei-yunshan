import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/customer',
    name: 'customer',
    component: () => import('../views/CustomerMain.vue'),
    meta: { requiresAuth: true, roles: [0, 1] }
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('../views/AdminMain.vue'),
    meta: { requiresAuth: true, roles: [1] }
  },
  {
    path: '/cashier',
    name: 'cashier',
    component: () => import('../views/CashierMain.vue'),
    meta: { requiresAuth: true, roles: [1, 2] }
  },
  {
    path: '/kitchen',
    name: 'kitchen',
    component: () => import('../views/KitchenMain.vue'),
    meta: { requiresAuth: true, roles: [1, 3] }
  },
  {
    path: '/kitchen/inventory',
    name: 'kitchen-inventory',
    component: () => import('../views/KitchenInventory.vue'),
    meta: { requiresAuth: true, roles: [1, 3] }
  },
  {
    path: '/pos',
    name: 'pos',
    component: () => import('../views/PosMain.vue'),
    meta: { requiresAuth: true, roles: [1, 2] }
  },
  {
    path: '/orders',
    name: 'orders',
    component: () => import('../views/OrdersView.vue'),
    meta: { requiresAuth: true, roles: [1, 2, 3] }
  },
  {
    path: '/settings',
    name: 'settings',
    component: () => import('../views/SettingsView.vue'),
    meta: { requiresAuth: true, roles: [1] }
  }
]

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn
  const userRole = userStore.userRole

  if (to.meta.requiresAuth === false) {
    next()
    return
  }

  if (!isLoggedIn) {
    next('/login')
    return
  }

  const allowedRoles = to.meta.roles
  if (allowedRoles && !allowedRoles.includes(userRole)) {
    switch (userRole) {
      case 0:
        next('/customer')
        break
      case 1:
        next('/admin')
        break
      case 2:
        next('/cashier')
        break
      case 3:
        next('/kitchen')
        break
      default:
        next('/login')
    }
    return
  }

  next()
})

export default router