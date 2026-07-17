# 🍽️ Canteen Menu & Ratings System (Spring Boot + Vue)

A full-stack web application that lets users view the daily canteen menu and submit **star ratings + feedback** for menu items. Administrators can manage the daily menu.

- **Frontend:** Vue 3 (Vite) 
- **Backend:** Spring Boot (REST APIs + validation + Spring Security)
- **Database:** MongoDB

---

## Aim
Provide a digital workflow for canteen menu presentation and user feedback by combining:
- a modern SPA (Vue)
- a RESTful backend (Spring Boot)
- persistent storage (MongoDB)

---

## Objective
1. Display daily menu items (by date).
2. Allow users to submit ratings/feedback for menu items.
3. Allow admins to add/update/delete menu items.
4. Support viewing orders and user-related order history.

---

## Architecture
### High-level architecture
Client-server architecture using REST APIs.

```text
Vue.js (Frontend)
  ↓ Axios calls to /api/*
Spring Boot (Backend)
  ↓ Spring MVC controllers + Services + Repositories
MongoDB
```

### Backend security model (demo-friendly)
- CSRF disabled (API use case).
- Basic HTTP auth disabled.
- `permitAll` is enabled for:
  - `POST /api/public/register`
  - `POST /api/public/authenticate`
  - Menu endpoints under `/api/menu/**`
  - Orders endpoints under `/api/orders/**`

> Authentication is currently a **demo “fake token”** returned from `/api/public/authenticate`.

---

## Workflow
### 1) Start application
1. Run MongoDB.
2. Run Spring Boot backend (`localhost:8080`).
3. Run Vue frontend (`localhost:5173`).

### 2) Menu browsing
- Frontend requests menu data from:
  - `GET /api/menu`
  - `GET /api/menu/date/{date}`

### 3) Rating submission
- Frontend submits a rating for a menu item using:
  - `POST /api/menu/{id}/rate`

### 4) Admin menu management
- Admin/UI calls:
  - `POST /api/menu`
  - `PUT /api/menu/{id}`
  - `DELETE /api/menu/{id}`

### 5) Orders
- Order-related requests:
  - `POST /api/orders`
  - `GET /api/orders` 
  - `GET /api/orders/user/{userId}`
  - `GET /api/orders/status/{status}`
  - `GET /api/orders/{id}`
  - `PUT /api/orders/{id}`

### 6) Authentication (demo)
- Register:
  - `POST /api/public/register`
- Login:
  - `POST /api/public/authenticate`

---

## Tech Stack
| Layer | Technology |
|---|---|
| Frontend | Vue 3, Vite, Axios, Vue Router, Vuex, Vitest |
| Backend | Spring Boot, Spring Web, Spring Security, Spring Validation |
| Database | MongoDB |
| Build/Test | Maven (backend), npm/Vite/Vitest (frontend) |

---

## Implementation (what each layer does)
### Backend (Spring Boot)
- **Controllers** expose REST endpoints.
- **Services** implement application logic.
- **Repositories** persist and query MongoDB collections.
- **SecurityConfig** defines route access.

### Frontend (Vue)
- Vue Router provides navigation between views.
- Axios makes API calls; Vite proxies `/api` to the backend.
- Vuex holds shared state (where applicable).

---

## Project Structure
```text
CanteenMenu-main/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/example/canteen/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── model/
│       └── config/
└── frontend/
    ├── package.json
    └── src/
        ├── views/
        ├── components/
        ├── assets/
        └── App.vue
```

---

## Backend Operations (backend/)
See: `backend/HELP.md`

---

## Frontend Operations (frontend/)
See: `frontend/Readme.md`

---

## Commands to Run
### Backend (Spring Boot)
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
- Backend URL: `http://localhost:8080`

### Frontend (Vue/Vite)
```bash
cd frontend
npm install
npm run dev
```
- Frontend URL: `http://localhost:5173`

---

## Git Compile (typical workflow)
```bash
git clone <your-repo-url>
git checkout -b blackboxai/docs

# build
cd backend && mvn clean install
cd ../frontend && npm install && npm run build
```

---

## MongoDB Configuration
Backend configuration is in:
- `backend/src/main/resources/application.properties`

Current values:
- `spring.data.mongodb.uri=mongodb://localhost:27017`
- `spring.data.mongodb.database=db`

---

## Troubleshooting
- **MongoDB not running:** start MongoDB and ensure port `27017` is available.
- **API calls failing from frontend:** confirm backend is running on `8080`.
- **Authentication issues:** `/api/public/authenticate` returns `fake-jwt-token` and does not implement real JWT yet.

---

## Future Enhancements
- Replace demo auth with real JWT + role-based authorization.
- Add rating aggregation endpoints (server-side averages).
- Add file/image upload for menu items.
- Add CI checks and improve test coverage.
