<template>
  <div class="orders-view-container">
    <div class="header-section">
      <h2>My Order History</h2>
      <p class="subtitle">Place new orders for today's menu and track your active dishes.</p>
    </div>

    <div class="layout-grid">
      <!-- Place order card -->
      <div class="card form-card">
        <h3>Place a New Order</h3>
        <p class="section-desc">Select from today's available menu items and specify quantity.</p>

        <form @submit.prevent="placeOrder">
          <div class="form-group">
            <label for="dish-select">Select Dish</label>
            <select id="dish-select" v-model="newOrder.menuItemId" required>
              <option value="" disabled>Choose a dish...</option>
              <option v-for="dish in menuItems" :key="dish.id" :value="dish.id">
                {{ dish.name }} - ₹{{ dish.price.toFixed(2) }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="order-quantity">Quantity</label>
            <input 
              id="order-quantity" 
              type="number" 
              v-model.number="newOrder.quantity" 
              min="1" 
              required 
            />
          </div>

          <div class="order-summary" v-if="selectedDish">
            <p><strong>Subtotal:</strong> ₹{{ (selectedDish.price * newOrder.quantity).toFixed(2) }}</p>
          </div>

          <button type="submit" class="btn-block" :disabled="!newOrder.menuItemId">Place Order</button>
        </form>
      </div>

      <!-- Orders history card -->
      <div class="card list-card">
        <h3>Your Orders</h3>
        <p class="section-desc">Track preparation progress or cancel pending items.</p>

        <div v-if="loading" class="loading-state">Loading your orders...</div>
        <div v-else-if="orders.length === 0" class="empty-state">
          <div class="empty-icon">🍽️</div>
          <h3>No Orders Yet</h3>
          <p>You haven't placed any orders today. Order some food to satisfy your cravings!</p>
        </div>

        <div v-else class="orders-list">
          <div v-for="order in orders" :key="order.id" class="order-item-card">
            <div class="order-item-header">
              <div>
                <span class="order-id-label">Order #{{ order.id.slice(-6) }}</span>
                <span class="order-item-date">{{ formatDate(order.orderedAt) }}</span>
              </div>
              <span :class="['status-badge', order.status.toLowerCase()]">{{ order.status }}</span>
            </div>

            <div class="order-item-details">
              <div class="detail-row">
                <span class="dish-title">{{ getDishName(order.menuItemId) }}</span>
                <span class="dish-qty">{{ order.quantity }}x</span>
              </div>
              <div class="detail-row meta-row">
                <span>Unit Price: ₹{{ getDishPrice(order.menuItemId).toFixed(2) }}</span>
                <span class="item-total">Total: ₹{{ (getDishPrice(order.menuItemId) * order.quantity).toFixed(2) }}</span>
              </div>
            </div>

            <div class="order-item-actions" v-if="order.status === 'PENDING'">
              <button class="btn-danger btn-sm" @click="cancelOrder(order.id)">Cancel Order</button>
            </div>
          </div>
        </div>
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
        quantity: 1
      }
    }
  },
  computed: {
    selectedDish() {
      if (!this.newOrder.menuItemId) return null
      return this.menuItems.find(d => d.id === this.newOrder.menuItemId)
    }
  },
  methods: {
    async fetchInitialData() {
      this.loading = true
      try {
        await this.fetchMenuItems()
        await this.fetchOrders()
      } catch (err) {
        console.error('Failed to load initial data:', err)
      } finally {
        this.loading = false
      }
    },
    async fetchOrders() {
      const userId = localStorage.getItem('userId')
      if (!userId) return
      const res = await axios.get(`/api/orders/user/${userId}`)
      // Sort orders descending
      this.orders = res.data.sort((a, b) => new Date(b.orderedAt) - new Date(a.orderedAt))
    },
    async fetchMenuItems() {
      // Fetch only today's menu so users can only order active items
      const today = new Date().toISOString().slice(0, 10)
      const res = await axios.get(`/api/menu/date/${today}`)
      this.menuItems = res.data
    },
    getDishName(id) {
      const dish = this.menuItems.find(d => d.id === id)
      return dish ? dish.name : 'Unknown Dish'
    },
    getDishPrice(id) {
      const dish = this.menuItems.find(d => d.id === id)
      return dish ? dish.price : 0
    },
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString([], { hour: '2-digit', minute: '2-digit', month: 'short', day: 'numeric' })
    },
    async placeOrder() {
      if (!this.newOrder.menuItemId || this.newOrder.quantity < 1) {
        alert('Please select a dish and quantity.')
        return
      }

      const userId = localStorage.getItem('userId')
      const payload = {
        userId: userId,
        menuItemId: this.newOrder.menuItemId,
        quantity: this.newOrder.quantity,
        status: 'PENDING',
        orderedAt: new Date().toISOString()
      }

      try {
        await axios.post('/api/orders', payload)
        this.newOrder.menuItemId = ''
        this.newOrder.quantity = 1
        this.fetchOrders()
      } catch (err) {
        console.error('Place order failed:', err)
        alert('Failed to place the order.')
      }
    },
    async cancelOrder(id) {
      if (confirm('Are you sure you want to cancel this order?')) {
        try {
          const order = this.orders.find(o => o.id === id)
          if (!order) return
          
          const payload = {
            ...order,
            status: 'CANCELLED'
          }
          await axios.put(`/api/orders/${id}`, payload)
          this.fetchOrders()
        } catch (err) {
          console.error('Cancel order failed:', err)
          alert('Could not cancel this order.')
        }
      }
    }
  },
  mounted() {
    this.fetchInitialData()
  }
}
</script>

<style scoped>
.orders-view-container {
  max-width: 1200px;
  margin: 0 auto;
}

.header-section {
  margin-bottom: 2rem;
}

.subtitle {
  font-size: 1.1rem;
  opacity: 0.85;
}

.layout-grid {
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 2rem;
  align-items: start;
}

.section-desc {
  font-size: 0.9rem;
  margin-bottom: 1.5rem;
}

.order-summary {
  background: var(--input-bg);
  border: 1px solid var(--navbar-border);
  padding: 1rem;
  border-radius: var(--border-radius-sm);
  margin-bottom: 1.5rem;
  text-align: right;
  font-size: 1.1rem;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.order-item-card {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid var(--card-border);
  border-radius: var(--border-radius-sm);
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  transition: var(--transition-smooth);
}

.order-item-card:hover {
  border-color: rgba(99, 102, 241, 0.2);
  background: rgba(99, 102, 241, 0.02);
}

.order-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px dashed var(--card-border);
  padding-bottom: 0.75rem;
}

.order-id-label {
  font-weight: 700;
  font-size: 0.95rem;
  margin-right: 0.75rem;
  color: var(--text-primary);
}

.order-item-date {
  font-size: 0.8rem;
  color: var(--text-secondary);
}

.order-item-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dish-title {
  font-weight: 600;
  font-size: 1.1rem;
  color: var(--text-primary);
}

.dish-qty {
  font-weight: 700;
  background: var(--input-border);
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
  font-size: 0.85rem;
}

.meta-row {
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.item-total {
  font-weight: 800;
  color: var(--primary);
  font-size: 1.05rem;
}

.order-item-actions {
  display: flex;
  justify-content: flex-end;
}

.btn-sm {
  padding: 0.4rem 1rem;
  font-size: 0.85rem;
}

.loading-state, .empty-state {
  text-align: center;
  padding: 5rem 1.5rem;
  color: var(--text-secondary);
}

.empty-icon {
  font-size: 3.5rem;
  margin-bottom: 1rem;
}

@media (max-width: 1024px) {
  .layout-grid {
    grid-template-columns: 1fr;
  }
}
</style>
