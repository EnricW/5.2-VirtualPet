# 🐾 Virtual Pet Backend API

This is the backend service for the Virtual Pet application, built with **Spring Boot**. It offers RESTful APIs to manage user authentication (JWT-based), user roles, and virtual pets with interactive actions.

---

## 🚀 Core Features

- ✅ User registration and login with JWT-based authentication
- ✅ Role support (`ROLE_USER`, `ROLE_ADMIN`)
- ✅ Full CRUD operations for virtual pets
- ✅ Interactive pet actions: Feed, Play, and Interact
- ✅ API documentation available via Swagger UI
- ✅ Simple in-memory caching for pet retrieval
- ✅ Integration tests covering critical endpoints

---

## ⚙️ Technologies

- Java 17
- Spring Boot 3.4.3
- Spring Security with JWT (`jjwt`)
- Spring Data JPA + MySQL
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + MockMvc
- Maven

---

## 📁 Directory Overview

```
src
├── main
│   ├── java/s05/virtualpet
│   │   ├── controller/         # REST controllers
│   │   ├── dto/                # Data transfer objects
│   │   ├── enums/              # Enum classes like PetType and Luck
│   │   ├── model/              # Entity models for persistence
│   │   ├── repository/         # JPA repository interfaces
│   │   ├── security/           # Security config and JWT utilities
│   │   ├── service/            # Service layer interfaces
│   │   ├── service/impl/       # Business logic implementations
│   │   └── VirtualpetApplication.java
│   └── resources/
│       ├── application.properties
│       └── logback.xml
└── test/java/s05/virtualpet
    ├── BaseIntegrationTest.java
    ├── controller/
    │   ├── AuthControllerTest.java
    │   └── PetControllerTest.java
```

---

## 🗄️ Database Configuration

- Backend uses **MySQL**. Make sure to create a database named `virtualpet_db`.
- Default database credentials (adjust in `application.properties`):
  ```properties
  spring.datasource.username=root
  spring.datasource.password=root
  ```

---

## ▶️ Running the Backend

```bash
# Start the backend server
mvn spring-boot:run
```

---

## 🧪 Running Tests

```bash
# Execute unit and integration tests
mvn test
```

---

## 🔌 API Endpoints

### 1. Register a user  
**POST** `/auth/register`  
**Body (JSON):**
```json
{
  "username": "exampleUser",
  "password": "SecurePass123"
}
```  
**Returns:** Confirmation message

---

### 2. Login a user  
**POST** `/auth/login`  
**Body (JSON):**
```json
{
  "username": "exampleUser",
  "password": "SecurePass123"
}
```  
**Returns:** JWT token

---

### 3. Get all pets of current user  
**GET** `/pets`  
**Authorization:** Bearer token  
**Returns:** List of pets

---

### 4. Get a specific pet by ID  
**GET** `/pets/{id}`  
**Authorization:** Bearer token  
**Parameter:** `id` of the pet  
**Returns:** Pet details

---

### 5. Create a new pet  
**POST** `/pets`  
**Authorization:** Bearer token  
**Body (JSON):**
```json
{
  "name": "Lucky",
  "type": "HEARTS"
}
```  
**Returns:** Created pet details

---

### 6. Perform an action on a pet  
**POST** `/pets/{id}/action?action=FEED`  
**Authorization:** Bearer token  
**Parameter:**  
- `id`: ID of the pet  
- `action`: One of `FEED`, `PLAY`, `INTERACT`  
**Returns:** Updated pet

---

### 7. Delete a pet  
**DELETE** `/pets/{id}`  
**Authorization:** Bearer token  
**Parameter:** `id` of the pet  
**Returns:** Confirmation message

---

## 📖 Swagger API Docs

After launching the backend, you can access the interactive API documentation at:

```
http://localhost:8080/swagger-ui.html
```

Use the **Authorize** button to input your JWT token when calling secured endpoints.

---
