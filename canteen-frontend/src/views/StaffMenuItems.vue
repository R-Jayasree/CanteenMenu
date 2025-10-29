<template>
  <div>
    <h2>Staff Menu Management</h2>

    <!-- Add new menu item form -->
    <form @submit.prevent="addMenuItem">
      <h3>Add New Menu Item</h3>
      <div>
        <label>Name:</label>
        <input v-model="newItem.name" required />
      </div>
      <div>
        <label>Description:</label>
        <textarea v-model="newItem.description" required></textarea>
      </div>
      <div>
        <label>Image URL:</label>
        <input v-model="newItem.imageUrl" />
      </div>
      <label>Price (INR):</label>
        <input type="number" v-model.number="newItem.price" required min="0" step="0.01" />
      <div>
        <label>Date (YYYY-MM-DD):</label>
        <input type="date" v-model="newItem.date" required />
      </div>
      <button type="submit">Add Item</button>
    </form>

    <!-- Existing menu items -->
    <h3>Existing Menu Items</h3>
    <div v-if="menu.length === 0">No menu items found.</div>
    <div class="menu-grid">
  <div v-for="item in menu" :key="item.id" class="menu-item">
    <h4>{{ item.name }}</h4>
    <img :src="item.imageUrl" alt="" width="150" />
    <p>{{ item.description }}</p>
    <p><strong>Date:</strong> {{ item.date }}</p>
    <button @click="editItem(item)">Edit</button>
    <button @click="deleteItem(item.id)">Delete</button>
  </div>
</div>


    <!-- Edit modal -->
    <div v-if="showEditModal" class="modal">
      <div class="modal-content">
        <h3>Edit Menu Item</h3>
        <form @submit.prevent="updateMenuItem">
          <div>
            <label>Name:</label>
            <input v-model="editItemData.name" required />
          </div>
          <div>
            <label>Description:</label>
            <textarea v-model="editItemData.description" required></textarea>
          </div>
          <div>
            <label>Image URL:</label>
            <input v-model="editItemData.imageUrl" />
          </div>
          <div>
        <label>Price (INR):</label>
        <input type="number" v-model.number="newItem.price" required min="0" step="0.01" />
        </div>
          <div>
            <label>Date (YYYY-MM-DD):</label>
            <input type="date" v-model="editItemData.date" required />
          </div>
        <div class="button-group">
        <button type="submit">Update Item</button>
        <button type="button" @click="closeEditModal">Cancel</button>
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
      newItem: {
        name: '',
        description: '',
        imageUrl: '',
        date: ''
      },
      showEditModal: false,
      editItemData: null
    }
  },
  methods: {
    async fetchMenu() {
      try {
        const res = await axios.get('/api/menu')
        this.menu = res.data
      } catch (error) {
        alert('Failed to fetch menu items.')
      }
    },
    async addMenuItem() {
      try {
        await axios.post('/api/menu', this.newItem)
        this.newItem = { name: '', description: '', imageUrl: '', date: '' }
        this.fetchMenu()
      } catch (error) {
        alert('Failed to add menu item.')
      }
    },
    editItem(item) {
      this.editItemData = { ...item }
      this.showEditModal = true
    },
    closeEditModal() {
      this.showEditModal = false
      this.editItemData = null
    },
    async updateMenuItem() {
      try {
        await axios.put(`/api/menu/${this.editItemData.id}`, this.editItemData)
        this.closeEditModal()
        this.fetchMenu()
      } catch (error) {
        alert('Failed to update menu item.')
      }
    },
    async deleteItem(id) {
      if (confirm('Are you sure you want to delete this menu item?')) {
        try {
          await axios.delete(`/api/menu/${id}`)
          this.fetchMenu()
        } catch (error) {
          alert('Failed to delete menu item.')
        }
      }
    }
  },
  mounted() {
    this.fetchMenu()
  }
}
</script>

<style>
.menu-item {
  border: 1px solid #ddd;
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 6px;
}
.modal {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 6px;
  width: 350px;
}
label {
  display: block;
  margin-top: 10px;
}
input, textarea {
  width: 100%;
  padding: 5px;
  margin-top: 5px;
}
button {
  margin-top: 10px;
  margin-right: 10px;
}
.button-group {
  display: flex;
  gap: 10px;        /* Space between buttons */
  margin-top: 10px; /* Optional spacing above buttons */
}

.button-group button {
  flex: 1;          /* Make buttons equal width (optional) */
  padding: 8px 12px;
  cursor: pointer;
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
  background: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: box-shadow 0.2s ease;
}

.menu-item:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
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
