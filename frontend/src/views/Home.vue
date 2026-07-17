<template>
  <div class="home-container">
    <div class="hero-section">
      <h1 class="hero-title">Experience Canteen Delight</h1>
      <p class="hero-subtitle">
        Delicious meals, effortless ordering, and premium quality served fresh on campus daily.
      </p>
      
      <div v-if="!isAuthenticated" class="hero-actions">
        <router-link to="/login" class="btn btn-primary">Login to Account</router-link>
        <router-link to="/register" class="btn btn-secondary">Register Now</router-link>
      </div>
      <div v-else class="hero-actions">
        <router-link to="/menu" class="btn btn-primary">View Today's Menu</router-link>
      </div>
    </div>

    <div class="features-section">
      <div class="card feature-card">
        <div class="feature-icon">🍔</div>
        <h3>Fresh Meals</h3>
        <p>Prepared daily with high-quality ingredients by our expert culinary team.</p>
      </div>
      <div class="card feature-card">
        <div class="feature-icon">⚡</div>
        <h3>Fast Checkout</h3>
        <p>Skip the lines. Order ahead and pick up when your food is ready.</p>
      </div>
      <div class="card feature-card">
        <div class="feature-icon">⭐</div>
        <h3>Rate & Review</h3>
        <p>Share your honest feedback to help us continuously elevate dish quality.</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    isAuthenticated() {
      return !!localStorage.getItem('token')
    }
  }
}
</script>

<style scoped>
.home-container {
  text-align: center;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 160px);
  box-sizing: border-box;
  overflow: hidden;
}

.hero-section {
  max-width: 800px;
  margin: 0 auto 1.5rem;
}

.hero-title {
  font-size: 2.75rem;
  line-height: 1.15;
  font-weight: 800;
  margin-bottom: 0.75rem;
  background: linear-gradient(135deg, var(--primary), var(--accent));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: -0.04em;
}

.hero-subtitle {
  font-size: 1.1rem;
  color: var(--text-secondary);
  margin-bottom: 1.5rem;
  line-height: 1.45;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
}

.btn {
  display: inline-flex;
  align-items: center;
  padding: 0.8rem 2rem;
  border-radius: var(--border-radius-sm);
  font-weight: 600;
  text-decoration: none;
  font-size: 0.95rem;
  transition: var(--transition-smooth);
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary), var(--accent));
  color: white;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.45);
}

.btn-secondary {
  background: transparent;
  color: var(--text-primary);
  border: 1px solid var(--text-secondary);
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.08);
  border-color: var(--text-primary);
}

@media (prefers-color-scheme: light) {
  .btn-secondary:hover {
    background: rgba(0, 0, 0, 0.05);
  }
}

.features-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 1.5rem;
  max-width: 1000px;
  width: 100%;
  margin: 0 auto;
}

.feature-card {
  padding: 1.5rem 1.25rem;
  text-align: center;
}

.feature-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  display: inline-block;
  transition: transform var(--transition-speed) ease;
}

.feature-card:hover .feature-icon {
  transform: scale(1.15) rotate(5deg);
}

.feature-card h3 {
  font-size: 1.25rem;
  margin-bottom: 0.75rem;
}

.feature-card p {
  font-size: 0.95rem;
  line-height: 1.5;
}

@media (max-width: 600px) {
  .hero-title {
    font-size: 2.5rem;
  }
  .hero-actions {
    flex-direction: column;
    gap: 1rem;
  }
  .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
