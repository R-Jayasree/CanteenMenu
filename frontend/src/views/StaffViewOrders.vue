<template>
  <div class="staff-orders-container">
    <div class="header-section">
      <h2>Kitchen Order Dashboard</h2>
      <p class="subtitle">Track active orders, update preparation status, and coordinate deliveries.</p>
    </div>

    <!-- Status filter tabs -->
    <div class="filter-tabs">
      <button 
        v-for="tab in tabs" 
        :key="tab.value" 
        :class="['tab-btn', { active: activeTab === tab.value }]"
        @click="activeTab = tab.value"
      >
        {{ tab.label }}
        <span class="badge">{{ getOrderCountByTab(tab.value) }}</span>
      </button>
    </div>

    <div v-if="loading" class="loading-state">Loading kitchen orders...</div>
    <div v-else-if="filteredOrders.length === 0" class="empty-state">
      <div class="empty-icon">🍳</div>
      <h3>No Orders Found</h3>
      <p>There are no orders matching the selection at the moment.</p>
    </div>

    <div v-else class="orders-grid">
      <div v-for="order in filteredOrders" :key="order.id" class="card order-card">
        <div class="order-header">
          <div class="header-left">
            <span class="order-id">Order #{{ order.id.slice(-6) }}</span>
            <span :class="['status-badge', order.status.toLowerCase()]">{{ order.status }}</span>
          </div>
          <span class="order-time">{{ formatDate(order.orderedAt) }}</span>
        </div>

        <div class="order-body">
          <h3 class="dish-name">{{ getDishName(order.menuItemId) }}</h3>
          
          <div class="order-meta">
            <div class="meta-item">
              <span class="label">Quantity:</span>
              <span class="value">{{ order.quantity }}x</span>
            </div>
            <div class="meta-item">
              <span class="label">Total Price:</span>
              <span class="value price">₹{{ (getDishPrice(order.menuItemId) * order.quantity).toFixed(2) }}</span>
            </div>
            <div class="meta-item">
              <span class="label">Customer Email:</span>
              <span class="value email">{{ getCustomerEmail(order.userId) }}</span>
            </div>
          </div>
        </div>

        <div class="order-actions">
          <label>Update Status:</label>
          <select v-model="order.status" @change="updateStatus(order)" class="status-select">
            <option value="PENDING">Pending (New)</option>
            <option value="PREPARING">Preparing</option>
            <option value="READY">Ready for Pickup</option>
            <option value="DELIVERED">Delivered</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
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
      users: [], // To map customer emails
      loading: false,
      activeTab: 'active',
      tabs: [
        { label: 'Active Preparation', value: 'active' },
        { label: 'Pending approval', value: 'PENDING' },
        { label: 'Preparing', value: 'PREPARING' },
        { label: 'Ready', value: 'READY' },
        { label: 'Delivered', value: 'DELIVERED' },
        { label: 'Cancelled', value: 'CANCELLED' },
        { label: 'All Orders', value: 'all' }
      ]
    }
  },
  computed: {
    filteredOrders() {
      if (this.activeTab === 'all') {
        return this.orders
      }
      if (this.activeTab === 'active') {
        return this.orders.filter(o => ['PENDING', 'PREPARING', 'READY'].includes(o.status))
      }
      return this.orders.filter(o => o.status === this.activeTab)
    }
  },
  methods: {
    async fetchInitialData() {
      this.loading = true
      try {
        await Promise.all([
          this.fetchOrders(),
          this.fetchMenuItems()
        ])
      } catch (err) {
        console.error('Failed to load initial data:', err)
      } finally {
        this.loading = false
      }
    },
    async fetchOrders() {
      // Exposes the newly added GET /api/orders endpoint returning all orders
      const res = await axios.get('/api/orders')
      // Sort orders by timestamp descending
      this.orders = res.data.sort((a, b) => new Date(b.orderedAt) - new Date(a.orderedAt))
    },
    async fetchMenuItems() {
      const res = await axios.get('/api/menu')
      this.menuItems = res.data
    },
    getDishName(id) {
      const dish = this.menuItems.find(d => d.id === id)
      return dish ? dish.name : 'Loading dish...'
    },
    getDishPrice(id) {
      const dish = this.menuItems.find(d => d.id === id)
      return dish ? dish.price : 0
    },
    getCustomerEmail(userId) {
      // Simple placeholder mapping for demo since user info is seeded
      if (userId === 'u3') return 'customer@canteen.com'
      return 'User: ' + userId
    },
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString([], { hour: '2-digit', minute: '2-digit', month: 'short', day: 'numeric' })
    },
    getOrderCountByTab(tabValue) {
      if (tabValue === 'all') return this.orders.length
      if (tabValue === 'active') return this.orders.filter(o => ['PENDING', 'PREPARING', 'READY'].includes(o.status)).length
      return this.orders.filter(o => o.status === tabValue).length
    },
    async updateStatus(order) {
      try {
        await axios.put(`/api/orders/${order.id}`, order)
        this.fetchOrders()
      } catch (err) {
        console.error('Failed to update order status:', err)
        alert('Could not update status.')
      }
    }
  },
  mounted() {
    this.fetchInitialData()
  }
}
</script>

<style scoped>
.staff-orders-container {
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

.filter-tabs {
  display: flex;
  gap: 0.5rem;
  overflow-x: auto;
  padding-bottom: 0.75rem;
  margin-bottom: 2rem;
  border-bottom: 1px solid var(--card-border);
}

.tab-btn {
  background: transparent;
  border: 1px solid transparent;
  color: var(--text-secondary);
  box-shadow: none;
  font-size: 0.9rem;
  padding: 0.5rem 1rem;
  border-radius: var(--border-radius-sm);
  white-space: nowrap;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  transition: var(--transition-smooth);
}

.tab-btn:hover {
  background: rgba(99, 102, 241, 0.05);
  color: var(--text-primary);
  transform: none;
  box-shadow: none;
}

.tab-btn.active {
  background: rgba(99, 102, 241, 0.12);
  color: var(--primary);
  border-color: rgba(99, 102, 241, 0.2);
}

.tab-btn .badge {
  background: var(--navbar-border);
  padding: 0.1rem 0.5rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  color: var(--text-primary);
}

.tab-btn.active .badge {
  background: var(--primary);
  color: white;
}

.orders-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
}

.order-card {
  align-items: stretch;
  text-align: left;
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--card-border);
  margin-bottom: 1.25rem;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.order-id {
  font-weight: 700;
  font-size: 0.95rem;
}

.order-time {
  font-size: 0.8rem;
  color: var(--text-secondary);
}

.order-body {
  flex: 1;
}

.dish-name {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 1rem;
}

.order-meta {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.meta-item {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
}

.meta-item .label {
  color: var(--text-secondary);
}

.meta-item .value {
  font-weight: 600;
}

.meta-item .value.price {
  color: var(--primary);
  font-weight: 800;
}

.meta-item .value.email {
  font-size: 0.8rem;
}

.order-actions {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid var(--card-border);
}

.order-actions label {
  font-size: 0.8rem;
  font-weight: 700;
  text-transform: uppercase;
  color: var(--text-secondary);
}

.status-select {
  padding: 0.6rem 0.85rem;
  font-size: 0.9rem;
  margin: 0;
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
</style>
