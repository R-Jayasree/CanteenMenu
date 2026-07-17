import { createRouter, createWebHistory } from 'vue-router'

import Home from './views/Home.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import Menu from './views/Menu.vue'
import Orders from './views/Orders.vue'
import StaffViewOrders from './views/StaffViewOrders.vue'
import ManageMenu from './views/ManageMenu.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/menu', name: 'Menu', component: Menu },
  { path: '/orders', name: 'Orders', component: Orders },
  { path: '/admin', name: 'AdminDashboard', component: ManageMenu },
  { path: '/staff/orders', name: 'StaffViewOrders', component: StaffViewOrders },
  { path: '/staff/menu', name: 'StaffMenuItems', component: ManageMenu },
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

  if (publicPages.includes(to.name)) {
    return next()
  }

  if (!token) {
    return next('/login')
  }

  // Admin route protection
  if (to.path.startsWith('/admin') && role !== 'ADMIN') {
    return next('/')
  }

  // Staff route protection (allowed for STAFF and ADMIN)
  if (to.path.startsWith('/staff') && role !== 'STAFF' && role !== 'ADMIN') {
    return next('/')
  }

  next()
})

export default router

