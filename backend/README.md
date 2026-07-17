# Backend (Spring Boot)

This folder contains the **server-side** of the Canteen Menu & Ratings System.

---

## Aim
Expose REST APIs that manage:
- daily menu items
- user ratings/feedback for menu items
- orders
- user registration/authentication (demo)

---

## Objective
- Provide stable API endpoints that the Vue frontend can call.
- Persist menu/items/orders/users in **MongoDB**.
- Apply request validation and consistent error handling.

---

## Architecture
### Layered structure
- `controller/` – HTTP endpoints (REST controllers)
- `service/` – business logic
- `repository/` – MongoDB persistence layer
- `model/` – domain/data models
- `config/` – Spring Security configuration

### Request flow (workflow)
```text
HTTP Request
  → Controller (routes + input validation)
     → Service (business rules)
        → Repository (MongoDB operations)
           → Response JSON
```

---

## Tech Stack
- Spring Boot 3.5.x
- Spring Web (REST)
- Spring Security (route access)
- Spring Validation (Jakarta)
- MongoDB (Spring Data Mongo)
- Maven (build/test)

Key dependencies are defined in `pom.xml`.

---

## Security (Spring Security)
Implemented in: `src/main/java/com/example/canteen/config/SecurityConfig.java`

- CSRF disabled
- Basic auth disabled
- Route access:
  - `/api/public/**` -> permitAll
  - `/api/menu/**` -> permitAll
  - `/api/orders/**` -> permitAll
  - other routes -> authenticated (if added later)

---

## API Reference (endpoints)
### Menu: `MenuItemController`
File: `src/main/java/com/example/canteen/controller/MenuItemController.java`

Base path: `/api/menu`

| Method | Path | Description |
|---|---|---|
| GET | `/api/menu` | Get all menu items |
| GET | `/api/menu/date/{date}` | Get menu items by date (string date) |
| GET | `/api/menu/{id}` | Get menu item by id |
| POST | `/api/menu` | Add a menu item |
| PUT | `/api/menu/{id}` | Update a menu item |
| DELETE | `/api/menu/{id}` | Delete a menu item |
| POST | `/api/menu/{id}/rate` | Add a rating + feedback to a menu item |

**Rating workflow:**
1. Controller loads the menu item by id.
2. Adds `Rating` to the menu item’s ratings list.
3. Saves the updated menu item.

### Orders: `OrderController`
File: `src/main/java/com/example/canteen/controller/OrderController.java`

Base path: `/api/orders`

| Method | Path | Description |
|---|---|---|
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/user/{userId}` | Orders for a user |
| GET | `/api/orders/status/{status}` | Orders filtered by status |
| GET | `/api/orders/{id}` | Get order by id |
| POST | `/api/orders` | Create an order |
| PUT | `/api/orders/{id}` | Update an order |

### Authentication & Users: `UserController`
File: `src/main/java/com/example/canteen/controller/UserController.java`

Base path: `/api/public`

| Method | Path | Description |
|---|---|---|
| POST | `/api/public/register` | Register a new user |
| GET | `/api/public/user/{email}` | Get user by email |
| POST | `/api/public/authenticate` | Demo login (returns `fake-jwt-token`, `userId`, `role`) |

> Note: Current authenticate logic is plain password matching (demo). Token is not a real JWT yet.

---

## Data Model (MongoDB)
Models are located in `src/main/java/com/example/canteen/model/`.

### MenuItem
Represents a dish (name/details/price/date) and holds a list of `Rating` objects.

### Rating
Represents user rating/feedback (stars + feedback + timestamp).

### Order
Represents an order placed by a user for a menu item.

### User
Represents users and their roles.

---

## MongoDB Configuration
File: `src/main/resources/application.properties`

- `spring.data.mongodb.uri=mongodb://localhost:27017`
- `spring.data.mongodb.database=db`

---

## Seed Data (optional)
A Mongo seeding script exists at the project root: `seed.js`.

Seed roles and sample menu/orders.

Comment from script indicates usage pattern:
- Drop collections (`users`, `menuitems`, `orders`)
- Insert sample users: `ADMIN`, `STAFF`, `CUSTOMER`
- Insert sample `menuitems`
- Insert sample `orders` referencing seeded menu item ids

---

## Workflow to run (local development)
1. Start MongoDB.
2. Run:
   - `mvn clean install`
   - `mvn spring-boot:run`
3. Open the frontend (it proxies `/api` to this backend).

---

## Commands
### Build
```bash
cd backend
mvn clean install
```

### Run (dev)
```bash
mvn spring-boot:run
```

### Tests
```bash
mvn test
```

---

## Notes for API consumers (Frontend)
- Frontend should call endpoints under `/api/*`.
- Rate submission uses: `POST /api/menu/{menuItemId}/rate`.
- Orders use the `/api/orders` family of endpoints.


