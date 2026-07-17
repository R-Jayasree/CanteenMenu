<template>
  <div class="menu-container">
    <div class="header-section">
      <h2>Daily Canteen Menu</h2>
      <p class="subtitle">Freshly prepared, delicious meals available today. Browse, order, and review your favorites.</p>
    </div>

    <div v-if="loading" class="loading-state">
      <span class="loading-spinner">🍲</span>
      <p>Loading delicious options...</p>
    </div>
    
    <div v-else-if="menu.length === 0" class="empty-state">
      <div class="empty-icon">🍳</div>
      <h3>Nothing Cooked Yet!</h3>
      <p>There are no dishes scheduled for today. Check back soon or view tomorrow's menu.</p>
    </div>

    <div v-else class="menu-grid">
      <div v-for="item in menu" :key="item.id" class="menu-item-card">
        <img :src="item.imageUrl || defaultImage" alt="" class="menu-item-image" />
        
        <div class="menu-item-details">
          <h3>{{ item.name }}</h3>
          <p class="description">{{ item.description }}</p>
          
          <div class="menu-item-meta">
            <span class="menu-item-price">₹{{ item.price.toFixed(2) }}</span>
            <span class="menu-item-rating">⭐ {{ getAverageRating(item) }}</span>
          </div>
        </div>

        <div class="card-actions" v-if="isCustomer">
          <button class="btn-block" @click="openRateModal(item)">Rate & Review</button>
        </div>
      </div>
    </div>

    <!-- Rating Modal Dialog -->
    <div v-if="showRateModal" class="modal">
      <div class="modal-content">
        <h3>Rate {{ selectedItem?.name }}</h3>
        <p class="section-desc">Share your culinary experience with us!</p>
        
        <form @submit.prevent="submitRating">
          <div class="form-group">
            <label for="stars">Star Rating</label>
            <select id="stars" v-model.number="ratingStars">
              <option :value="5">⭐⭐⭐⭐⭐ 5 - Excellent</option>
              <option :value="4">⭐⭐⭐⭐ 4 - Very Good</option>
              <option :value="3">⭐⭐⭐ 3 - Good</option>
              <option :value="2">⭐⭐ 2 - Fair</option>
              <option :value="1">⭐ 1 - Poor</option>
            </select>
          </div>
          
          <div class="form-group">
            <label for="feedback">Your Review</label>
            <textarea id="feedback" v-model="feedback" rows="3" placeholder="Tell us what you loved or how we can improve..." required></textarea>
          </div>
          
          <div class="modal-actions">
            <button type="submit">Submit Review</button>
            <button type="button" class="btn-secondary" @click="closeModal">Cancel</button>
          </div>
        </form>
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
      loading: true,
      showRateModal: false,
      selectedItem: null,
      ratingStars: 5,
      feedback: '',
      defaultImage: 'https://images.unsplash.com/photo-1498837167922-ddd27525d352?auto=format&fit=crop&q=80&w=400'
    }
  },
  computed: {
    userRole() {
      return localStorage.getItem('role')
    },
    isCustomer() {
      return this.userRole === 'CUSTOMER'
    },
  },
  methods: {
    async fetchMenu() {
      this.loading = true
      try {
        const today = new Date().toISOString().slice(0, 10)
        // Fetch active daily menu from the Spring Boot API
        const res = await axios.get(`/api/menu/date/${today}`)
        this.menu = res.data
      } catch (err) {
        console.error('Failed to load menu:', err)
      } finally {
        this.loading = false
      }
    },
    getAverageRating(item) {
      if (!item.ratings || item.ratings.length === 0) return 'No reviews yet'
      const total = item.ratings.reduce((acc, r) => acc + r.stars, 0)
      return (total / item.ratings.length).toFixed(2) + ' / 5'
    },
    openRateModal(item) {
      this.selectedItem = item
      this.showRateModal = true
      this.ratingStars = 5
      this.feedback = ''
    },
    closeModal() {
      this.showRateModal = false
    },
    async submitRating() {
      try {
        const ratingPayload = {
          stars: this.ratingStars,
          userId: localStorage.getItem('userId') || 'anonymous',
          feedback: this.feedback,
          timestamp: new Date().toISOString(),
        }
        await axios.post(`/api/menu/${this.selectedItem.id}/rate`, ratingPayload)
        this.closeModal()
        this.fetchMenu()
      } catch (error) {
        console.error('Rating submission failed:', error)
        alert('Failed to submit rating. Please try again.')
      }
    },
  },
  mounted() {
    this.fetchMenu()
  },
}
</script>

<style scoped>
.menu-container {
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

.card-actions {
  margin-top: 1.25rem;
}

.section-desc {
  font-size: 0.875rem;
  margin-bottom: 1.25rem;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1.5rem;
}

.modal-actions button {
  flex: 1;
}

.loading-state, .empty-state {
  text-align: center;
  padding: 5rem 1.5rem;
  color: var(--text-secondary);
}

.loading-spinner {
  font-size: 3rem;
  display: inline-block;
  margin-bottom: 1rem;
  animation: spin 2s linear infinite;
}

.empty-icon {
  font-size: 3.5rem;
  margin-bottom: 1rem;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
