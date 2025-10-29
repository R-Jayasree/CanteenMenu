<template>
  <div>
    <nav>
      <div class="nav-left">
        <h1 class="app-name">Canteen Delight</h1>
      </div>
      <div class="nav-right">
        <router-link to="/">Home</router-link>
        <router-link to="/menu">Menu</router-link>
        <router-link v-if="isAuthenticated && userRole === 'CUSTOMER'" to="/orders">My Orders</router-link>
        <router-link v-if="userRole === 'ADMIN'" to="/admin">Admin Dashboard</router-link>
        <router-link v-if="userRole === 'STAFF'" to="/staff/orders">Manage Orders</router-link>
        <router-link v-if="userRole === 'STAFF'" to="/staff/menu">Manage Menu</router-link>
        <router-link v-if="!isAuthenticated" to="/login">Login</router-link>
        <router-link v-if="!isAuthenticated" to="/register">Register</router-link>
        <button v-if="isAuthenticated" @click="logout">Logout</button>
      </div>
    </nav>

    <router-view />
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

<style>
nav {
  padding: 10px;
  background-color: #222;
  color: white;
}

nav a {
  color: white;
  margin-right: 10px;
  text-decoration: none;
}
button {
  margin-left: 10px;
}
</style>
