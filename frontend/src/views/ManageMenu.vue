<template>
  <div class="manage-menu-container">
    <div class="header-section">
      <h2>Manage Canteen Menu</h2>
      <p class="subtitle">Add, edit, or remove daily dishes from the digital canteen menu catalog.</p>
    </div>

    <div class="layout-grid">
      <!-- Add/Edit form Card -->
      <div class="card form-card">
        <h3>{{ isEditing ? 'Edit Menu Item' : 'Add New Dish' }}</h3>
        <p class="section-desc">Provide item specifications including price and active date.</p>
        
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="name">Dish Name</label>
            <input id="name" v-model="form.name" placeholder="e.g., Paneer Butter Masala" required />
          </div>
          
          <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" v-model="form.description" rows="3" placeholder="Describe the ingredients, taste, or portion size..." required></textarea>
          </div>
          
          <div class="form-group">
            <label for="imageUrl">Image URL</label>
            <input id="imageUrl" v-model="form.imageUrl" placeholder="https://images.unsplash.com/... or leave blank" />
          </div>

          <div class="form-row">
            <div class="form-group flex-1">
              <label for="price">Price (INR)</label>
              <input id="price" type="number" v-model.number="form.price" min="0" step="0.01" placeholder="0.00" required />
            </div>
            
            <div class="form-group flex-1">
              <label for="date">Active Date</label>
              <input id="date" type="date" v-model="form.date" required />
            </div>
          </div>
          
          <div class="action-buttons">
            <button type="submit">{{ isEditing ? 'Save Changes' : 'Add to Menu' }}</button>
            <button type="button" v-if="isEditing" class="btn-secondary" @click="resetForm">Cancel</button>
          </div>
        </form>
      </div>

      <!-- Menu Items List Card -->
      <div class="card list-card">
        <h3>Current Menu Catalog</h3>
        <p class="section-desc">View and manage all items currently stored in the database.</p>
        
        <div v-if="loading" class="loading-state">Loading menu items...</div>
        <div v-else-if="menu.length === 0" class="empty-state">No items found in the menu. Add one to get started!</div>
        
        <div v-else class="items-table-wrapper">
          <table class="items-table">
            <thead>
              <tr>
                <th>Dish</th>
                <th>Price</th>
                <th>Scheduled Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in menu" :key="item.id">
                <td>
                  <div class="dish-info">
                    <img :src="item.imageUrl || defaultImage" alt="" class="dish-thumbnail" />
                    <div>
                      <span class="dish-title">{{ item.name }}</span>
                      <span class="dish-desc">{{ truncate(item.description) }}</span>
                    </div>
                  </div>
                </td>
                <td class="price-cell">₹{{ item.price.toFixed(2) }}</td>
                <td class="date-cell"><span class="status-badge preparing">{{ item.date }}</span></td>
                <td>
                  <div class="table-actions">
                    <button class="btn-secondary btn-icon" @click="startEdit(item)" title="Edit item">✏️</button>
                    <button class="btn-danger btn-icon" @click="deleteItem(item.id)" title="Delete item">🗑️</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
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
      menu: [],
      loading: false,
      isEditing: false,
      editId: null,
      form: {
        name: '',
        description: '',
        imageUrl: '',
        price: '',
        date: new Date().toISOString().slice(0, 10)
      },
      defaultImage: 'https://images.unsplash.com/photo-1498837167922-ddd27525d352?auto=format&fit=crop&q=80&w=150'
    }
  },
  methods: {
    async fetchMenu() {
      this.loading = true
      try {
        const res = await axios.get('/api/menu')
        this.menu = res.data
      } catch (err) {
        console.error('Failed to fetch menu:', err)
        alert('Could not load the menu items.')
      } finally {
        this.loading = false
      }
    },
    async submitForm() {
      // Ensure image placeholder is nice if empty
      if (!this.form.imageUrl) {
        this.form.imageUrl = 'https://images.unsplash.com/photo-1498837167922-ddd27525d352?auto=format&fit=crop&q=80&w=400'
      }

      try {
        if (this.isEditing) {
          await axios.put(`/api/menu/${this.editId}`, this.form)
        } else {
          await axios.post('/api/menu', this.form)
        }
        this.resetForm()
        this.fetchMenu()
      } catch (err) {
        console.error('Form submission failed:', err)
        alert('Failed to save the dish. Please check inputs.')
      }
    },
    startEdit(item) {
      this.isEditing = true
      this.editId = item.id
      this.form = {
        name: item.name,
        description: item.description,
        imageUrl: item.imageUrl,
        price: item.price,
        date: item.date
      }
    },
    async deleteItem(id) {
      if (confirm('Are you sure you want to delete this menu item?')) {
        try {
          await axios.delete(`/api/menu/${id}`)
          this.fetchMenu()
          if (this.isEditing && this.editId === id) {
            this.resetForm()
          }
        } catch (err) {
          console.error('Delete failed:', err)
          alert('Could not delete the item.')
        }
      }
    },
    resetForm() {
      this.isEditing = false
      this.editId = null
      this.form = {
        name: '',
        description: '',
        imageUrl: '',
        price: '',
        date: new Date().toISOString().slice(0, 10)
      }
    },
    truncate(str, length = 60) {
      if (!str) return ''
      return str.length > length ? str.slice(0, length) + '...' : str
    }
  },
  mounted() {
    this.fetchMenu()
  }
}
</script>

<style scoped>
.manage-menu-container {
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
  grid-template-columns: 420px 1fr;
  gap: 2rem;
  align-items: start;
}

.section-desc {
  font-size: 0.9rem;
  margin-bottom: 1.5rem;
}

.form-row {
  display: flex;
  gap: 1rem;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.items-table-wrapper {
  overflow-x: auto;
  border-radius: var(--border-radius-sm);
  border: 1px solid var(--card-border);
}

.items-table {
  width: 100%;
  border-collapse: collapse;
}

.items-table th {
  font-size: 0.8rem;
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

.dish-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.dish-thumbnail {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 6px;
  margin: 0;
}

.dish-title {
  display: block;
  font-weight: 600;
  font-size: 0.975rem;
  color: var(--text-primary);
}

.dish-desc {
  font-size: 0.8rem;
  color: var(--text-secondary);
}

.price-cell {
  font-weight: 700;
  color: var(--primary);
  white-space: nowrap;
}

.table-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-icon {
  padding: 0.4rem 0.6rem;
  font-size: 0.85rem;
  box-shadow: none;
  border-radius: 6px;
}

.loading-state, .empty-state {
  text-align: center;
  padding: 3rem 1.5rem;
  color: var(--text-secondary);
}

@media (max-width: 1024px) {
  .layout-grid {
    grid-template-columns: 1fr;
  }
}
</style>
