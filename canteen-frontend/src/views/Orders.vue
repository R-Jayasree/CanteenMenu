<template>
  <div>
    <h2>My Orders</h2>

    <div>
      <h3>Place New Order</h3>
      <form @submit.prevent="placeOrder">
        <label for="dish">Dish:</label>
        <select v-model="newOrder.menuItemId" id="dish" required>
          <option value="" disabled>Select a dish</option>
          <option v-for="dish in menuItems" :key="dish.id" :value="dish.id">
            {{ dish.name }}
          </option>
        </select>

        <label for="quantity">Quantity:</label>
        <input
          type="number"
          id="quantity"
          v-model.number="newOrder.quantity"
          min="1"
          required
        />

        <button type="submit">Place Order</button>
      </form>
    </div>

    <div v-if="loading">Loading orders...</div>

    <div v-else>
      <div v-if="orders.length === 0">No orders placed yet.</div>

      <div v-for="order in orders" :key="order.id" class="order-card">
        <p><strong>Dish:</strong> {{ getDishName(order.menuItemId) }}</p>
        <p><strong>Quantity:</strong> {{ order.quantity }}</p>
        <p><strong>Status:</strong> {{ order.status }}</p>
        <p><strong>Ordered on:</strong> {{ formatDate(order.orderedAt) }}</p>
        <p><strong>Dish Price:</strong> ₹{{ getDishPrice(order.menuItemId).toFixed(2) }}</p>
        <p><strong>Total:</strong> ₹{{ (getDishPrice(order.menuItemId) * order.quantity).toFixed(2) }}</p>

        <button
          v-if="order.status === 'PENDING'"
          @click="cancelOrder(order.id)"
        >
          Cancel Order
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      orders: [],
      menuItems: [],
      loading: false,
      newOrder: {
        menuItemId: '',
        quantity: 1,
      },
    }
  },
  methods: {
    async fetchOrders() {
      this.loading = true
      const userId = localStorage.getItem('userId')
      try {
        const res = await axios.get(`/api/orders/user/${userId}`)
        this.orders = res.data
      } catch (error) {
        alert('Failed to fetch orders')
      }
      this.loading = false
    },
    async fetchMenuItems() {
      try {
        const res = await axios.get('/api/menu')
        this.menuItems = res.data
      } catch (error) {
        alert('Failed to fetch menu')
      }
    },
    getDishName(id) {
      const dish = this.menuItems.find((d) => d.id === id)
      return dish ? dish.name : 'Unknown'
    },
    getDishPrice(id) {
  const dish = this.menuItems.find(d => d.id === id)
  return dish ? dish.price : 0
},
    async cancelOrder(id) {
      try {
        await axios.put(`/api/orders/${id}`, { status: 'CANCELLED' })
        this.fetchOrders()
      } catch (error) {
        alert('Failed to cancel order')
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleString()
    },
    async placeOrder() {
      if (!this.newOrder.menuItemId || this.newOrder.quantity < 1) {
        alert('Please select a dish and specify quantity')
        return
      }
      const userId = localStorage.getItem('userId')
      const orderPayload = {
        userId: userId,
        menuItemId: this.newOrder.menuItemId,
        quantity: this.newOrder.quantity,
        status: 'PENDING',
        orderedAt: new Date().toISOString(),
      }
      try {
        await axios.post('/api/orders', orderPayload)
        this.newOrder.menuItemId = ''
        this.newOrder.quantity = 1
        this.fetchOrders()
      } catch (error) {
        alert('Failed to place order')
      }
    },
  },
  async mounted() {
    await this.fetchMenuItems()
    await this.fetchOrders()
  },
}
</script>

<style>
.order-card {
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 10px;
  margin-bottom: 15px;
}

form {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-top: 10px;
}

input,
select {
  margin-top: 5px;
  margin-bottom: 10px;
  padding: 5px;
  width: 200px;
}

button {
  margin-top: 10px;
  padding: 7px 14px;
}
</style>
