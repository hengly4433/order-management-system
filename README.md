##  🛒 Order Management System (Spring Boot)


### 📌 System Process Overview

A production-ready Order Management System built with Java 21 and Spring Boot 3.4.5. It features JWT-based authentication, modular RBAC (Role-Based Access Control), Dockerized deployment, and support for managing orders, products, roles, permissions, users, customers, categories, modules, and packages, 


### ✨ Features

- ✅ Spring Boot 3.4.5 with layered architecture (Controller, Service, Repository, DTO, Mapper)
- 🔐 JWT Authentication & Authorization with Role-Permission structure
- 🔄 RBAC (User → Roles → Permissions) for secure, granular access
- 📦 Module & Package Management with dynamic permission assignment
- ⚠️ Exception Handling via GlobalExceptionHandler
- 📘 SpringDoc OpenAPI (Swagger UI) at /swagger-ui.html
- 🐘 PostgreSQL database integration
- ⚙️ Spring DevTools hot reload (via Docker Dev profile)
- 🐳 Docker Compose for containerized development and production builds
- 🖼️ File upload support for product images (custom upload dir)

### 🌐 API Modules

This table outlines the core entities in the system, their CRUD support status, and additional notes:

| **Entity**    | **CRUD** | **Notes**                            |
|---------------|:--------:|--------------------------------------|
| Auth          | ✅       | Login with JWT                       |
| User          | ✅       | User has multiple roles              |
| Role          | ✅       | Role has many permissions            |
| Permission    | ✅       | Permissions tied to modules          |
| Product       | ✅       | Image upload supported               |
| Category      | ✅       | -                                    |
| Customer      | ✅       | -                                    |
| Order         | ✅       | Supports multiple items              |
| Module        | ✅       | Belongs to Package                   |
| Package       | ✅       | Groups multiple modules              |
| Stock         | ✅       | Stock per product                    |


### 🔄 API Layers

This project follows the standard MVC + DTO + Mapper pattern:

```text
[Controller] → [Service] → [Repository]
         ↓             ↓
      [DTOs]       [Entities]
         ↑             ↓
     [Mappers] ←→ [Database]
```

### ⚡ Technologies

- Java 21
- Spring Boot 3.4.5
- Spring Security + JWT (jjwt 0.12.6)
- PostgreSQL 15
- Spring Data JPA
- Spring DevTools (enabled with Docker override)
- MapStruct (manual mapping currently)
- OpenAPI via springdoc-openapi-starter
- Docker + Docker Compose

### 🚀 Project Structure

```text
src
└── main
    ├── java/com.upskilldev.ordersystem
    │   ├── config              # Security, OpenAPI, MVC configuration
    │   ├── controller          # REST API endpoints
    │   ├── dto                 # DTOs (Create, Update, Response)
    │   ├── entity              # JPA Entities
    │   ├── exception           # Global Exception Handlers
    │   ├── mapper              # DTO ↔ Entity mapping
    │   ├── repository          # Spring Data JPA Repositories
    │   ├── security            # JWT Utils, UserDetailsService
    │   ├── service             # Interfaces and Implementations
    │   └── OrderSystemApplication.java
    └── resources
        ├── application.yml     # Main configuration
        ├── application-dev.yml # Dev profile config
        └── logback-spring.xml  # Logging
```

### 📁 Uploads & Logging

- Images are uploaded through the product endpoint.
- Uploaded files are stored in the uploads/images/ directory (mounted via Docker).
- File handling is configured with FileStorageProperties and custom logic in FileStorageService.

```bash
Uploaded images: uploads/images/
```

```bash
Application logs: logs/
```

### 📚 Swagger API UI

View all endpoints and test them interactively:

```text
GET     /api/users
POST    /api/roles
POST    /api/auth/login
GET     /api/orders
...
```

### 🔐 Authentication & Authorization

- Users register and login using /api/auth/ endpoints.
- On successful login, a JWT token is issued and sent back to the client.
- Every subsequent request must include the token in the Authorization: Bearer <token> header.
- All protected endpoints are secured with Spring Security using @PreAuthorize, ensuring only users with proper roles and permissions can access them.

### 🔒 RBAC Permission Model

- User has many Roles
- Role has many Permissions
- Permission defines access to endpoints (e.g., VIEW_ORDER, CREATE_MODULE)
- Modules logically group permissions for management

### 🧑‍💼 User → Role → Permission Assignment

- Users can be assigned one or more roles.
- Roles are assigned one or more permissions.
- Permissions are grouped into modules (e.g., User Management, Product Management).
- This separation of concerns allows fine-grained control of what actions a user can perform.

✅ Example:

```text
Package:
 └─ Module:
     └─ Permissions:
         - ADD_MODULE
         - VIEW_MODULE
         - UPDATE_MODULE
         - DELETE_MODULE
```

### 🧱 Modules & Packages Structure

- Modules are features of the application (e.g., Order Management, Inventory, Auth).
- Packages are groupings of modules for broader categorization (e.g., Core System, Sales Module).
- Each module contains a set of permissions, which are linked to endpoints.

### 📦 CRUD Operations Flow

- Every core entity (like Product, Category, Order, Customer) follows this flow:
- DTOs (Data Transfer Objects) receive validated client input.
- Controllers handle HTTP requests and route data to services.
- Services contain business logic and delegate persistence to repositories.
- Repositories interact with the database using Spring Data JPA.
- Mappers convert DTOs ⇌ Entities.

### 📝 Error Handling

- Global exceptions are managed through GlobalExceptionHandler.
- Custom exceptions like ResourceNotFoundException return meaningful 400, 404, or 500 messages.
- Validation failures return a structured JSON with details of invalid fields.

### ⚙️ Running the Application

Clone the Repository

```bash
git clone https://github.com/your-username/order-system.git
cd order-system
```

### 📁 Environment Variables (`.env`)

```text
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
DB_HOST=localhost
DB_PORT=5432
DB_NAME=ordersystem
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=changeme
JWT_EXPIRATION_MS=3600000
```

### 📦 Docker Setup

Build & Run (Dev - Hot Reload)

```bash
docker-compose -f docker-compose.yml -f docker-compose.override.yml up --build
```

Build & Run (Production)

```bash
docker-compose up --build
```

### 🧪 Sample SQL Data for Testing

Use the following SQL script to populate initial permissions, roles, users, and their many-to-many relationships for testing and development purposes.

```sql
-- PERMISSIONS
INSERT INTO permission (id, name) VALUES (1, 'VIEW_USER');
INSERT INTO permission (id, name) VALUES (2, 'CREATE_USER');
INSERT INTO permission (id, name) VALUES (3, 'UPDATE_USER');
INSERT INTO permission (id, name) VALUES (4, 'DELETE_USER');

INSERT INTO permission (id, name) VALUES (5, 'VIEW_ROLE');
INSERT INTO permission (id, name) VALUES (6, 'CREATE_ROLE');
INSERT INTO permission (id, name) VALUES (7, 'UPDATE_ROLE');
INSERT INTO permission (id, name) VALUES (8, 'DELETE_ROLE');

INSERT INTO permission (id, name) VALUES (9, 'VIEW_PERMISSION');
INSERT INTO permission (id, name) VALUES (10, 'CREATE_PERMISISON');
INSERT INTO permission (id, name) VALUES (11, 'UPDATE_PERMISSION');
INSERT INTO permission (id, name) VALUES (12, 'DELETE_PERMISSION');

-- ROLES
INSERT INTO role (id, name) VALUES (1, 'ADMIN');

-- ROLE-PERMISSIONS (Many-to-Many)
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 1);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 2);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 3);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 4);

INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 5);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 6);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 7);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 8);

INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 9);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 10);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 11);
INSERT INTO role_permissions (role_id, permissions_id) VALUES (1, 12);

-- USERS
-- Passwords encoded using BCrypt (password = `admin123`, `manager123`, `user123`)
INSERT INTO users (id, username, email, password) VALUES 
(1, 'admin', 'admin@example.com', '$2a$10$7QW9BZyQAvktPIpx0mI1BO/wJSsyoH6FuKmRHRwE7JAp6Ko3o/1Le')

-- USER-ROLES (Many-to-Many)
INSERT INTO user_roles (user_id, roles_id) VALUES (1, 1); -- admin -> ADMIN
```

### 🔐 Login credentials
| Username | Password     | Role    |
| -------- | ------------ | ------- |
| admin    | `admin123`   | ADMIN   |

### 🌍 API Docs

Swagger UI is available at:

```bash
http://localhost:8080/swagger-ui.html
```

All endpoints are annotated with OpenAPI tags.

### 🛠️ Common Commands

Compile inside container

```bash
docker exec -it ordersystem-app-dev mvn compile
```

Rebuild only the JAR in prod

```bash
docker-compose build app && docker-compose up app
```

### 📚 License

MIT License. Feel free to use and modify.

### 👤 Author

Hengly — `UpSkillDev`

Designed for production-level learning and freelance portfolio.