<template>
  <div class="app-container">
    <nav class="navbar">
      <div class="nav-left">
        <h1 class="app-name">Canteen Delight</h1>
      </div>
      <div class="nav-right">
        <router-link class="nav-link" to="/">Home</router-link>
        <router-link class="nav-link" to="/menu">Menu</router-link>
        <router-link class="nav-link" v-if="isAuthenticated && userRole === 'CUSTOMER'" to="/orders">My Orders</router-link>
        <router-link class="nav-link" v-if="userRole === 'ADMIN' || userRole === 'STAFF'" to="/staff/menu">Manage Menu</router-link>
        <router-link class="nav-link" v-if="userRole === 'ADMIN' || userRole === 'STAFF'" to="/staff/orders">Manage Orders</router-link>
        <router-link class="nav-link" v-if="!isAuthenticated" to="/login">Login</router-link>
        <router-link class="nav-link" v-if="!isAuthenticated" to="/register">Register</router-link>
        <button class="logout-btn" v-if="isAuthenticated" @click="logout">Logout</button>
      </div>
    </nav>

    <main>
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userRole: localStorage.getItem('role') || '',
      token: localStorage.getItem('token') || '',
    }
  },
  computed: {
    isAuthenticated() {
      return !!this.token
    },
  },
  methods: {
    logout() {
      localStorage.clear()
      this.userRole = ''
      this.token = ''
      this.$router.push('/login')
    },
  },
  watch: {
    '$route'(to, from) {
      this.token = localStorage.getItem('token') || ''
      this.userRole = localStorage.getItem('role') || ''
    },
  },
}
</script>

<style scoped>
/* Scoped styles kept minimal, deferring navbar, main and button design to style.css */
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>

