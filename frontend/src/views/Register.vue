<template>
  <div class="auth-container">
    <div class="card auth-card">
      <h2>Create an Account</h2>
      <p class="subtitle">Join Canteen Delight today to enjoy hot meals.</p>
      
      <form @submit.prevent="register">
        <div class="form-group">
          <label for="reg-email">Email Address</label>
          <input 
            id="reg-email" 
            type="email" 
            v-model="email" 
            placeholder="name@canteen.com" 
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="reg-password">Password</label>
          <input 
            id="reg-password" 
            type="password" 
            v-model="password" 
            placeholder="Min 6 characters" 
            required 
          />
        </div>

        <div class="form-group">
          <label for="reg-role">Your Role</label>
          <select id="reg-role" v-model="role" required>
            <option value="CUSTOMER">Customer (Order & Rate)</option>
            <option value="STAFF">Kitchen Staff (Manage Orders)</option>
            <option value="ADMIN">Administrator (Full Access)</option>
          </select>
        </div>
        
        <button type="submit" class="btn-block">Sign Up</button>
      </form>
      
      <div v-if="success" class="success-msg">✅ Successfully registered. Please <router-link to="/login">login here</router-link>.</div>
      <div v-if="error" class="error-msg">⚠️ {{ error }}</div>
      
      <p class="login-link">
        Already have an account? 
        <router-link to="/login">Login here</router-link>
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
      role: 'CUSTOMER',
      success: false,
      error: '',
    }
  },
  methods: {
    async register() {
      this.success = false
      this.error = ''

      try {
        await axios.post('/api/public/register', {
          email: this.email,
          password: this.password,
          role: this.role
        })
        
        this.success = true
        this.email = ''
        this.password = ''
      } catch (err) {
        console.error('Registration failed:', err)
        this.error = err.response?.data?.message || 'Registration failed. User might already exist.'
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

.success-msg {
  color: var(--success);
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
  padding: 0.85rem;
  border-radius: var(--border-radius-sm);
  margin-top: 1.5rem;
  font-size: 0.9rem;
  font-weight: 600;
  text-align: left;
}

.success-msg a {
  text-decoration: underline;
  color: var(--success);
  font-weight: 700;
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

.login-link {
  margin-top: 2rem;
  font-size: 0.925rem;
  color: var(--text-secondary);
}

.login-link a {
  font-weight: 600;
  color: var(--primary);
}
</style>
