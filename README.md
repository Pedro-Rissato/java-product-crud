# 📦 Product CRUD Java

A product management application built with **pure Java 21** (no frameworks), featuring a fully layered architecture, PostgreSQL database, HikariCP connection pooling, Flyway migrations, and integration tests with Testcontainers.

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Main language |
| Maven | 4 | Build & dependency management |
| PostgreSQL | 16 | Relational database |
| HikariCP | 6.3.0 | JDBC connection pool |
| Flyway | 10.15.0 | Database migration & versioning |
| JUnit Jupiter | 5.10.0 | Unit testing |
| Testcontainers | 1.20.4 | Integration tests with a real database |
| Docker | 29 | Application containerization |

---


## ⚙️ Prerequisites

- Java 21+
- Maven 3.8+
- Docker and Docker Compose


---
## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Pedro-Rissato/product-crud-java-homolog.git
cd product-crud-java-homolog
```

### 2. Set up environment variables

```bash
cp .env.example .env
```

```env
DB_URL=jdbc:postgresql://localhost:5432/product-crud-java
DB_USER=crud
DB_PASSWORD=java
```

### 3. Start the database

```bash
docker compose -f dcoker-compose.yml up -d db
```

### 4. Build and run

```bash
mvn clean package
java -jar target/crudDeProdutos-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 🐳 Full Stack with Docker Compose

```bash
docker compose -f dcoker-compose.yml up --build
```

The application runs on port **8080**.

---

## 🏗️ Architecture

The project follows a clean **layered architecture**, wired manually via constructor injection — no IoC container, no Spring, no magic.

```
View  →  Controller  →  Service  →  Repository  →  PostgreSQL
```

Each layer has a clear responsibility:

- **View** — Handles user interaction (CLI menu)
- **Controller** — Routes user actions to the correct service method
- **Service** — Enforces all business rules and validations
- **Repository** — Abstracts data access; two implementations exist:
  - `ProductRepositoryPostgres` — production implementation using JDBC + HikariCP
  - `ProductRepositoryInMemory` — lightweight implementation for unit testing
- **Model** — Plain `Product` domain entity, no annotations
- **DTO** — Separates input (`ProductRequestDTO`) from output (`ProductResponseDTO`, `ProductPriceResponseDTO`)
- **Config** — Loads database credentials from environment variables and bootstraps HikariCP and Flyway

> Dependency injection is done manually in `Main.java`, making the dependency graph explicit and transparent.

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/br/com/rissato/
│   │   ├── Main.java
│   │   ├── config/              # DbConfig, ConnectionFactory
│   │   ├── controller/          # ProductController
│   │   ├── dto/                 # ProductRequestDTO, ProductResponseDTO, ProductPriceResponseDTO
│   │   ├── exception/           # Custom exceptions hierarchy
│   │   ├── model/               # Product entity
│   │   ├── repository/          # ProductRepository (interface) + 2 implementations
│   │   ├── service/             # ProductService (business logic)
│   │   └── view/                # ProductView (CLI)
│   └── resources/               # Flyway SQL migrations
└── test/                        # Unit and integration tests
```

---

## ✅ Features

### Core CRUD
- **Create** a product with name, price, stock and optional description
- **List** all products ordered by ID
- **Get** a product by ID
- **Full update** of a product (all fields at once)
- **Delete** a product by ID

### Granular Update Operations
- **Adjust price** — update only the price of a product
- **Adjust name** — update only the name
- **Adjust description** — update only the description
- **Update stock** — increment or decrement stock using a **transactional operation** with `SELECT ... FOR UPDATE` to prevent race conditions
- **Apply discount** — set a percentage discount (0–99%) on a product

### Price Calculation
- **Get final price** — returns the original price, discount percentage, and calculated final price after discount

---

## 📐 Business Rules

All rules are enforced in the `ProductService` layer:

- **Name** cannot be null or blank
- **Price** must be greater than zero (`BigDecimal`)
- **Stock** cannot be null or negative
- **Discount** must be between 0 (inclusive) and 100 (exclusive) — a 100% discount is not allowed
- **Stock update** is transactional: uses `SELECT FOR UPDATE` + explicit commit/rollback to guarantee consistency under concurrent access
- **Product not found** throws `ProductNotFoundException`
- **Duplicate product** (SQL State `23505`) throws `DuplicateProductException`
- **Validation errors** throw `ValidationException`
- **Database errors** throw `DatabaseException`

---

## 🧱 Exception Hierarchy

```
ProductCrudException (base RuntimeException)
├── ValidationException       # Invalid input data
├── ProductNotFoundException  # Product does not exist
├── DuplicateProductException # Unique constraint violation (SQL 23505)
└── DatabaseException         # General JDBC/SQL errors
```

---

## 🎯 Learning Goals

This project was built to practice and demonstrate:

- Building a **layered architecture** (View → Controller → Service → Repository) without any framework
- Using **raw JDBC** with `PreparedStatement` and `ResultSet` for all database operations
- Managing a **connection pool** with HikariCP
- Applying **database migrations** with Flyway
- Implementing **JDBC transactions manually** (`setAutoCommit(false)`, `commit()`, `rollback()`)
- Using the **Repository Pattern** with an interface and multiple implementations (PostgreSQL + InMemory)
- Writing **integration tests** with Testcontainers (real PostgreSQL in Docker during tests)
- Handling **SQL exceptions** semantically by translating SQLState codes into domain exceptions
- **Dependency injection by hand** — wiring all layers explicitly in `Main.java`
- Containerizing a Java app with **Docker** and **Docker Compose**



---

## 🧪 Tests

Integration tests use **Testcontainers** to spin up a real PostgreSQL instance automatically. Docker must be running.

```bash
mvn test
```

---

## 🔄 Database Migrations

Managed by **Flyway**, migrations run automatically on startup. Scripts are located at:

```
src/main/resources/db/migration/
```

---

## 📄 License

This project is licensed under the MIT License.