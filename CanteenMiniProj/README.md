# 🍽️ Canteen Menu & Ratings System

A **full-stack web application** to display daily canteen dishes and collect **5-star ratings and feedback** from users.  
Built using **Spring Boot (backend)** and **Vue.js (frontend)** with **MongoDB** database integration.

---

## 🧭 Project Overview

### 🎯 Objective  
To digitize the college canteen’s daily menu and enable users to rate dishes, providing valuable feedback to improve food quality and menu management.

### ✨ Key Features  
- 📅 View daily dishes with category, price, and average rating  
- ⭐ Submit ratings and feedback for dishes  
- 🧑‍💼 Admin panel for managing daily menu items (Add, Edit, Delete)  
- 📊 Real-time average rating calculation per dish  
- ✅ Full validation (backend + frontend)  
- 💾 Browser storage for persistent session data  

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-------------|
| **Frontend** | Vue.js 3, Vite, Axios, TailwindCSS |
| **Backend** | Spring Boot, Spring Data JPA, Maven |
| **Database** | MongoDB |
| **Testing** | JUnit (Spring Boot), Vitest (Vue) |
| **Version Control** | Git & GitHub |
| **Build Tools** | npm, Maven |

---

## ⚙️ Architecture

### 🧱 System Design
A **client-server model** using RESTful APIs to connect Vue.js frontend and Spring Boot backend.

```text
Vue.js (Frontend)
   ↓ Axios (HTTP Requests)
Spring Boot (Backend)
   ↓ JPA Repositories
PostgreSQL Database
```

---

## 🗂️ Directory Structure

```
canteen-menu-ratings/
├── backend/
│   ├── src/main/java/com/canteen/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── model/
│   │   ├── repository/
│   │   └── CanteenApplication.java
│   ├── src/test/java/com/canteen/
│   └── pom.xml
└── frontend/
    ├── src/
    │   ├── components/
    │   ├── pages/
    │   ├── router/
    │   ├── store/
    │   ├── assets/
    │   └── App.vue
    ├── package.json
    ├── vite.config.js
    └── tests/
```

---

## 🧩 Implementation

### ✅ **1. Functionality & Correctness**

#### Classes Implemented
- **MenuItem** – represents a dish (id, name, category, price, rating, date)
- **Rating** – represents user feedback (id, user, menuId, stars, comment)

#### CRUD Operations
- Add, Update, Delete menu items (Admin)
- Create and Read ratings (User)
- Retrieve average rating for each dish

#### End-to-End Functionality
- Fully integrated Vue + Spring Boot + MongoDB  
- Verified via API and UI testing

---

### ✅ **2. API & Data Model**

#### RESTful Routes

| Method | Endpoint | Description |
|---------|-----------|-------------|
| GET | `/api/menu` | Fetch all menu items |
| POST | `/api/menu` | Add a new dish (Admin) |
| PUT | `/api/menu/{id}` | Update dish details |
| DELETE | `/api/menu/{id}` | Remove a dish |
| GET | `/api/ratings/{menuId}` | Get all ratings for a dish |
| POST | `/api/ratings` | Submit a rating |

#### Status Codes
- `200 OK` — success  
- `201 Created` — new record added  
- `400 Bad Request` — validation failure  
- `404 Not Found` — record missing  

#### Database Schema
MongoDB with one-to-many relationship:  
One `MenuItem` ↔ Many `Ratings`

---

### ✅ **3. UI/UX Basics**

- **Routing:** Implemented with Vue Router (`/menu`, `/rate`, `/admin`)  
- **Browser Storage:** Used `localStorage` to persist user session  
- **Responsive Design:** Built using **TailwindCSS** for accessibility and responsiveness  

---

### ✅ **4. Validation & Error Handling**

#### Backend
- Input validation using `@Valid`, `@NotBlank`, `@Min`, etc.  
- Exception handling with `@ControllerAdvice`  
- Returns descriptive JSON error messages  

#### Frontend
- Inline form validations and error prompts  
- Disabled submit button for invalid inputs  
- Clear toast/pop-up messages for errors and success  

---

### ✅ **5. Code Quality & Setup**

- Clean directory structure and layered architecture  
- Clear and descriptive naming conventions  
- Comments for all controllers and service methods  
- Environment variables stored in `.env`  
- Easy local setup with minimal steps  

```bash
# Backend
cd backend
mvn clean install
mvn spring-boot:run

# Frontend
cd frontend
npm install
npm run dev
```

---

### ✅ **6. Testing**

#### 🧪 Spring Boot Testing (JUnit)
File: `MenuServiceTest.java`  
Tests CRUD operations and service logic.

```java
@Test
void testAddMenuItem() {
    MenuItem item = new MenuItem("Idly", "Breakfast", 30.0);
    when(menuRepository.save(item)).thenReturn(item);
    assertEquals("Idly", menuService.addMenuItem(item).getName());
}
```

#### 🧩 Vue Testing (Vitest)
File: `MenuList.test.js`  
Tests UI rendering and data fetching behavior.

```js
test('renders menu items correctly', async () => {
  render(MenuList, { props: { dishes: [{ name: 'Idly' }] } });
  expect(screen.getByText('Idly')).toBeTruthy();
});
```

✅ **Both backend and frontend testing implemented successfully.**

---

## 🚀 Run the Project

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/<your-username>/canteen-menu-ratings.git
cd canteen-menu-ratings
```

### 2️⃣ Setup Backend
```bash
cd backend
mvn spring-boot:run
```

### 3️⃣ Setup Frontend
```bash
cd frontend
npm install
npm run dev
```

🖥️ Backend: `http://localhost:8080`  
🌐 Frontend: `http://localhost:5173`

---

## 🧠 Future Enhancements
- 📈 Analytics dashboard for popular dishes  
- 🔒 Role-based authentication (Admin/User)  
- 🖼️ Image upload for dishes  
- 🧾 Export ratings and menu as PDF reports  

---
## 👩‍💻 Author

**Gopika Ganesan**  
🎓 M.Tech (Integrated) — Computer Science  
🏫 SSN College of Engineering, Chennai

**Harini L. V.**  
🎓 M.Tech (Integrated) — Computer Science  
🏫 SSN College of Engineering, Chennai  

**Jayasree R**  
🎓 M.Tech (Integrated) — Computer Science  
🏫 SSN College of Engineering, Chennai

---

## 🏁 Conclusion

This project demonstrates a complete **end-to-end full-stack development workflow** using **Spring Boot, Vue.js, and PostgreSQL**.  
It meets all functionality, validation, and testing requirements, showcasing both technical and design proficiency.

---

⭐ *If you found this project useful, don’t forget to star the repo!* ⭐
