<template>
  <div>
    <h2>Daily Menu</h2>
    <div v-if="loading">Loading menu...</div>
    <div v-else class="menu-grid">
    <div v-for="item in menu" :key="item.id" class="menu-item">
        <h3>{{ item.name }}</h3>
        <img :src="item.imageUrl" alt="" width="250" />
        <p>{{ item.description }}</p>
        <p><strong>Price:</strong> ₹{{ item.price.toFixed(2) }}</p>

        <p>Rating: {{ getAverageRating(item) }}</p>
        <button @click="openRateModal(item)" v-if="isCustomer">Rate this dish</button>
    </div>
    </div>


    <div v-if="showRateModal" class="modal">
      <div class="modal-content">
        <h3>Rate {{ selectedItem.name }}</h3>
        <label>Stars:</label>
        <select v-model.number="ratingStars">
          <option v-for="n in 5" :key="n" :value="n">{{ n }}</option>
        </select>
        <br />
        <label>Feedback:</label><br />
        <textarea v-model="feedback" rows="4"></textarea>
        <br />
        <button @click="submitRating">Submit</button>
        <button @click="closeModal">Cancel</button>
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
      const today = new Date().toISOString().slice(0, 10)
      const res = await axios.get(`/api/menu/date/${today}`)
      this.menu = res.data
      this.loading = false
    },
    getAverageRating(item) {
      if (!item.ratings || item.ratings.length === 0) return 'No ratings yet'
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
          userId: localStorage.getItem('userId'),
          feedback: this.feedback,
          timestamp: new Date().toISOString(),
        }
        await axios.post(`/api/menu/${this.selectedItem.id}/rate`, ratingPayload)
        this.closeModal()
        this.fetchMenu()
      } catch (error) {
        console.log('Rating submission failed:', error)
        alert('Failed to submit rating')
      }
    },
  },
  mounted() {
    this.fetchMenu()
  },
}
</script>

<style>
.menu-item {
  margin-bottom: 20px;
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 8px;
}
.modal {
  position: fixed;
  background: rgba(0, 0, 0, 0.4);
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 6px;
  width: 300px;
}
.menu-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.menu-item {
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  box-shadow: 0 2px 6px rgb(0 0 0 / 0.1);
  transition: transform 0.2s ease;
}

.menu-item:hover {
  transform: translateY(-5px);
}

.menu-item img {
  max-width: 100%;
  height: auto;
  border-radius: 6px;
  margin-bottom: 10px;
}

.menu-item button {
  margin-top: auto;
  padding: 8px 15px;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.menu-item button:hover {
  background-color: #0056b3;
}

</style>
