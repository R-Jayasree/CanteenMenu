<template>
  <div class="auth-container">
    <div class="card auth-card">
      <h2>Welcome Back</h2>
      <p class="subtitle">Log in to order delicious canteen food.</p>
      
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="login-email">Email Address</label>
          <input 
            id="login-email" 
            type="email" 
            v-model="email" 
            placeholder="name@canteen.com" 
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="login-password">Password</label>
          <input 
            id="login-password" 
            type="password" 
            v-model="password" 
            placeholder="••••••••" 
            required 
          />
        </div>
        
        <button type="submit" class="btn-block">Sign In</button>
      </form>
      
      <div v-if="error" class="error-msg">⚠️ {{ error }}</div>
      
      <p class="register-link">
        Don't have an account? 
        <router-link to="/register">Create one here</router-link>
      </p>
    </div>
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
      this.error = ''
      try {
        const res = await axios.post('/api/public/authenticate', {
          email: this.email,
          password: this.password,
        })

        // Save token, role and userId to localStorage
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('role', res.data.role)
        localStorage.setItem('userId', res.data.userId)

        // Route to home or active menu
        this.$router.push('/')
      } catch (err) {
        console.error('Login failed:', err)
        this.error = err.response?.data || 'Invalid email or password'
      }
    },
  },
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  padding: 1.5rem;
}

.auth-card {
  width: 100%;
  max-width: 440px;
  text-align: center;
  padding: 3rem 2.5rem;
}

.auth-card h2 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.subtitle {
  font-size: 0.95rem;
  margin-bottom: 2.25rem;
}

.error-msg {
  color: var(--danger);
  background: rgba(244, 63, 94, 0.1);
  border: 1px solid rgba(244, 63, 94, 0.2);
  padding: 0.85rem;
  border-radius: var(--border-radius-sm);
  margin-top: 1.5rem;
  font-size: 0.9rem;
  font-weight: 600;
  text-align: left;
}

.register-link {
  margin-top: 2rem;
  font-size: 0.925rem;
  color: var(--text-secondary);
}

.register-link a {
  font-weight: 600;
  color: var(--primary);
}
</style>
