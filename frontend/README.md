# Frontend (Vue 3 + Vite)

This folder contains the **client-side** of the Canteen Menu & Ratings System.

---

## Aim
Provide a responsive UI to:
- browse the daily canteen menu (optionally by date)
- submit ratings/feedback for menu items
- register/login (demo)
- view user orders and staff order views

---

## Objective
- Render menu and order data returned from the backend REST APIs.
- Integrate with Spring Boot using Axios.
- Keep development smooth using Vite’s proxy for `/api`.

---

## Architecture
### SPA + REST integration
```text
Vue Router (views)
  → components (UI)
    → Axios requests to /api/*
      → Vite proxy to http://localhost:8080
        → Spring Boot returns JSON
          → Vue renders the result
```

### Frontend layers
- `src/main.js` – app bootstrap
- `src/router.js` – routes and navigation
- `src/App.vue` – root layout
- `src/views/*` – page-level components
- `src/components/*` – reusable components
- `src/assets/*` – static assets

---

## Tech Stack
- Vue 3
- Vite
- Axios
- Vue Router
- Vuex
- Vitest (+ jsdom, vue test utils)

---

## Workflow (end-to-end)
1. User opens the frontend in the browser.
2. UI navigates to a view (menu/login/orders/admin).
3. Components call backend endpoints via Axios (e.g., `/api/menu`).
4. Backend responds; the UI updates state and displays results.

---

## Backend APIs used by the UI
Vite proxies `/api` to the backend, so frontend uses relative URLs.

### Menu
- `GET /api/menu` – fetch all menu items
- `GET /api/menu/date/{date}` – fetch menu for a given date

### Ratings
- `POST /api/menu/{menuItemId}/rate` – add rating/feedback for a menu item

### Admin menu (CRUD)
- `POST /api/menu` – add a new menu item
- `PUT /api/menu/{id}` – update a menu item
- `DELETE /api/menu/{id}` – delete a menu item

### Orders
- `GET /api/orders` – list all orders
- `GET /api/orders/user/{userId}` – orders by a user
- `GET /api/orders/status/{status}` – orders by status
- `GET /api/orders/{id}` – single order by id
- `POST /api/orders` – create order
- `PUT /api/orders/{id}` – update order

### Authentication (demo)
- `POST /api/public/register`
- `POST /api/public/authenticate` (returns a `fake-jwt-token` + role)

---

## Views (src/views/) — operations per screen
Based on repository files present in `src/views/`:
- `Home.vue` – entry/landing page
- `Menu.vue` – menu browsing / rating entry
- `ManageMenu.vue` – admin menu CRUD operations
- `Login.vue` – demo login (calls `/api/public/authenticate`)
- `Register.vue` – user registration (calls `/api/public/register`)
- `Orders.vue` – user order history and status views
- `StaffViewOrders.vue` – staff-oriented order monitoring

---

## Vite proxy (important)
Configured in `vite.config.js`:
```js
server: {
  proxy: {
    '/api': 'http://localhost:8080'
  }
}
```
So the frontend can call:
- `axios.get('/api/menu')`
- `axios.post('/api/menu/{id}/rate', payload)`

---

## Commands to run
### Install
```bash
cd frontend
npm install
```

### Run (development)
```bash
npm run dev
```
- Frontend URL: `http://localhost:5173`

### Build (production)
```bash
npm run build
```

### Test
```bash
npm test
```

---

## Run order with backend
1. Start backend:
   - `cd backend && mvn spring-boot:run`
2. Then start frontend:
   - `cd frontend && npm run dev`

---

## Notes
- Authentication currently returns a demo token and role; no real JWT validation is implemented yet.
- Ensure backend is reachable at `localhost:8080`.

