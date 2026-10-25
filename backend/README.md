# 🍔 El Buen Sabor – Backend API

REST API developed with **Spring Boot** for a food business management system.
The project focuses on **clean architecture**, **security**, and **scalability**, providing a robust backend for managing users, products, and orders.

> **Note:** Developed as an **Academic Project** for the "Desarrollo de Software" course, focusing on backend best practices and layered architecture.

---

## 🎯 System Overview

The system is designed to digitize the administration of a gastronomy business through a secure RESTful API.

### 🔑 Role-Based Access Control (RBAC)
The application implements security rules using JWT, differentiating access levels for:
- **Admin:** Full access to manage users, catalogue (products/ingredients), and view all orders.
- **Employee:** Operational access to manage orders and update states.
- **Client:** Access to browse products, place orders, and view personal order history.

### 📦 Data Management Capabilities
- **Product Management:** Complete CRUD for products and categories.
- **Order Processing:** Lifecycle management of orders from creation to completion.
- **Soft Delete:** Implementation of logical deletion strategies to maintain data integrity and history.

---

## 🚀 Technical Features

- **Architecture:** Layered REST API (Controller, Service, Repository) ensuring separation of concerns.
- **Security:** Stateless authentication using **JWT (JSON Web Tokens)** with custom filters.
- **Data Integrity:** **Soft Delete** implementation using Hibernate `@SQLDelete`.
- **Tech Stack:** Java 17, Spring Boot 3, Gradle, H2.

---

## 🛠️ Setup & Execution

### Prerequisites
- **Java 17 SDK**
- **Git**

### 1. Clone the repository
```bash
git clone https://github.com/matisalinas7/elbuensabor.git
cd elbuensabor/backend
```
### 2. Run the Application
- The project includes the Gradle Wrapper.

-- ## Mac/Linux
```bash
./gradlew bootRun
```

-- ## Windows
```bash
.\gradlew.bat bootRun
```

---

## 🔌 API Documentation & Usage

Once the application is running, you can access the interactive documentation and database console:

| Component | URL | Description |
| :--- | :--- | :--- |
| **Swagger UI** | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) | Interactive interface to test endpoints. |
| **H2 Console** | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) | Database GUI (Dev profile). |
| **API Root** | `http://localhost:8080` | Base server URL. |

---

## 📂 Project Structure

The project follows a **Layered Architecture** organizing the code by functional responsibility:

```text
src/main/java/com/peso/elBuenSabor
├── auth/            # Authentication logic & Entry points
├── config/          # Security (WebSecurityConfig) & App setup
├── controllers/     # REST Controllers (API Endpoints)
├── DTOs/            # Data Transfer Objects (Requests/Responses)
├── entities/        # JPA Entities (Database Models)
├── enums/           # Enumerations (e.g., User Roles, Order Status)
├── JWT/             # JWT Token Provider & Filter logic
├── mapper/          # Entity <-> DTO Mapping strategies
├── repositories/    # Data Access Layer (Spring Data JPA)
└── services/        # Core Business Logic
