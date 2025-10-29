<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="login">
      <div>
        <label>Email:</label>
        <input type="email" v-model="email" required />
      </div>
      <div>
        <label>Password:</label>
        <input type="password" v-model="password" required />
      </div>
      <button type="submit">Login</button>
    </form>
    <div v-if="error" style="color:red">{{ error }}</div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      email: '',
      password: '',
      error: '',
    }
  },
  methods: {
    async login() {
      try {
        const res = await axios.post('/api/public/authenticate', {
          email: this.email,
          password: this.password,
        })

        // Save token and role to localStorage
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('role', res.data.role)
        localStorage.setItem('userId', res.data.userId) // For ratings/orders usage

        this.$router.push('/')
      } catch (error) {
        console.error('Login failed:', error)
        this.error = 'Invalid email or password'
      }
    },
  },
}
</script>
