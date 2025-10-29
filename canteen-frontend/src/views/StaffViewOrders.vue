<template>
  <div>
    <h2>Staff: Manage Orders</h2>
    <div v-if="loading">Loading orders...</div>
    <div v-else>
      <div v-if="orders.length === 0">No orders to manage.</div>
      <div v-for="order in orders" :key="order.id" class="order-card">
        <p><strong>Dish:</strong> {{ getDishName(order.menuItemId) }}</p>
        <p><strong>User ID:</strong> {{ order.userId }}</p>
        <p><strong>Quantity:</strong> {{ order.quantity }}</p>
        <p><strong>Status:</strong> {{ order.status }}</p>
        <p><strong>Ordered on:</strong> {{ formatDate(order.orderedAt) }}</p>

        <select v-model="order.status" @change="updateStatus(order)">
          <option>PENDING</option>
          <option>PREPARING</option>
          <option>READY</option>
          <option>DELIVERED</option>
          <option>CANCELLED</option>
        </select>
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
    }
  },
  methods: {
    async fetchOrders() {
      this.loading = true
      const res = await axios.get('/api/orders/status/PENDING')
      this.orders = res.data
      await this.fetchMenuItems()
      this.loading = false
    },
    async fetchMenuItems() {
      const res = await axios.get('/api/menu')
      this.menuItems = res.data
    },
    getDishName(id) {
      const dish = this.menuItems.find(d => d.id === id)
      return dish ? dish.name : 'Unknown'
    },
    formatDate(date) {
      return new Date(date).toLocaleString()
    },
    async updateStatus(order) {
      await axios.put(`/api/orders/${order.id}`, order)
      this.fetchOrders()
    },
  },
  mounted() {
    this.fetchOrders()
  },
}
</script>

<style>
.order-card {
  border: 1px solid #ddd;
  margin: 10px 0;
  padding: 10px;
  border-radius: 6px;
}
</style>
