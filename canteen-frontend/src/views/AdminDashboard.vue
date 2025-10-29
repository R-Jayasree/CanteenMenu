<template>
  <div>
    <h2>Admin Dashboard - Manage Menu</h2>

    <form @submit.prevent="addDish">
      <div>
        <input v-model="newDish.name" placeholder="Dish name" required />
      </div>
      <div>
        <textarea v-model="newDish.description" placeholder="Description" required></textarea>
      </div>
      <div>
        <input v-model="newDish.imageUrl" placeholder="Image URL" />
      </div>
      <div>
        <input v-model="newDish.date" type="date" required />
      </div>
      <button type="submit">Add Dish</button>
    </form>

    <h3>Existing Menu Items</h3>
    <div v-for="dish in menu" :key="dish.id" class="dish-card">
      <h4>{{ dish.name }}</h4>
      <p>{{ dish.description }}</p>
      <img :src="dish.imageUrl" alt="" width="150" />
      <p>Date: {{ dish.date }}</p>
      <button @click="editDish(dish)">Edit</button>
      <button @click="deleteDish(dish.id)">Delete</button>
    </div>

    <div v-if="showEditModal" class="modal">
      <div class="modal-content">
        <h3>Edit Dish</h3>
        <input v-model="editData.name" placeholder="Name" />
        <textarea v-model="editData.description" placeholder="Description"></textarea>
        <input v-model="editData.imageUrl" placeholder="Image URL" />
        <input v-model="editData.date" type="date" />
        <button @click="updateDish">Save</button>
        <button @click="cancelEdit">Cancel</button>
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
      newDish: { name: '', description: '', imageUrl: '', date: '' },
      showEditModal: false,
      editData: null,
    }
  },
  methods: {
    async fetchMenu() {
      const res = await axios.get('/api/menu')
      this.menu = res.data
    },
    async addDish() {
      await axios.post('/api/menu', this.newDish)
      this.newDish = { name: '', description: '', imageUrl: '', date: '' }
      this.fetchMenu()
    },
    editDish(dish) {
      this.editData = { ...dish }
      this.showEditModal = true
    },
    cancelEdit() {
      this.showEditModal = false
      this.editData = null
    },
    async updateDish() {
      await axios.put(`/api/menu/${this.editData.id}`, this.editData)
      this.showEditModal = false
      this.editData = null
      this.fetchMenu()
    },
    async deleteDish(id) {
      await axios.delete(`/api/menu/${id}`)
      this.fetchMenu()
    },
  },
  mounted() {
    this.fetchMenu()
  },
}
</script>

<style>
.dish-card {
  border: 1px solid #ddd;
  margin-bottom: 10px;
  padding: 8px;
  border-radius: 6px;
}
.modal {
  position: fixed;
  background: rgba(0,0,0,0.4);
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 6px;
  width: 320px;
}
</style>
