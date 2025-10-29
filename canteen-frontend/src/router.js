import { createRouter, createWebHistory } from 'vue-router'

import Home from './views/Home.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import Menu from './views/Menu.vue'
import Orders from './views/Orders.vue'
import AdminDashboard from './views/AdminDashboard.vue'
import StaffViewOrders from './views/StaffViewOrders.vue'
import StaffMenuItems from './views/StaffMenuItems.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/menu', name: 'Menu', component: Menu },
  { path: '/orders', name: 'Orders', component: Orders },
  { path: '/admin', name: 'AdminDashboard', component: AdminDashboard },
  { path: '/staff/orders', name: 'StaffViewOrders', component: StaffViewOrders },
  { path: '/staff/menu', name: 'StaffMenuItems', component: StaffMenuItems, meta: { requiresAuth: true, role: 'STAFF' }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// Navigation guard for protected routes
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  const publicPages = ['Login', 'Register', 'Home']
  const adminPages = ['AdminDashboard']
  const staffPages = ['StaffViewOrders']

  if (publicPages.includes(to.name)) {
    return next()
  }

  if (!token) {
    return next('/login')
  }

  if (adminPages.includes(to.name) && role !== 'ADMIN') {
    return next('/')
  }

  if (staffPages.includes(to.name) && role !== 'STAFF') {
    return next('/')
  }
  if (to.meta.requiresAuth) {
    if (!token) return next('/login')
    if (to.meta.role && to.meta.role !== role) return next('/')
  }

  next()
})

export default router
