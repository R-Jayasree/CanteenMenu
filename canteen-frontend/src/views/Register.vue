<template>
  <div>
    <h2>Register</h2>
    <form @submit.prevent="register">
      <div>
        <label>Email:</label>
        <input type="email" v-model="email" required />
      </div>
      <div>
        <label>Password:</label>
        <input type="password" v-model="password" required />
      </div>
      <div>
        <label>Role:</label>
        <select v-model="role" required>
          <option value="CUSTOMER">Customer</option>
          <option value="STAFF">Staff</option>
        </select>
      </div>
      <button type="submit">Register</button>
    </form>

    <div v-if="success" style="color:green">✅ Successfully registered. Please login.</div>
    <div v-if="error" style="color:red">{{ error }}</div>
  </div>
</template>

<script>
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

      // Temporary check (no backend)
      if (this.email.trim().toLowerCase() === 'abc@gmail.com') {
        this.error = 'User already exists.'
        return
      }

      // Simulate successful registration
      this.success = true
      this.error = ''
      console.log('Registered user:', this.email, this.password, this.role)
    },
  },
}
</script>
