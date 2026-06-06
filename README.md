# Bookwise API

A library management system built with Spring Boot. Exposes RESTful endpoints for managing books and members, with a simple HTML/CSS/JS frontend.

**Course:** SECJ4383 Software Construction — Assignment 2

---

## Prerequisites

Make sure you have these installed before starting:

- [Java 21](https://www.oracle.com/java/technologies/downloads/#java21) — check with `java -version`
- [Laragon](https://laragon.org/download/) — for running MySQL locally
- [VS Code](https://code.visualstudio.com/) or any IDE

---

## Setup

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd bookwise-api
```

### 2. Start Laragon

1. Open **Laragon**
2. Click **Start All** — Apache and MySQL must both be green
3. Click **Database** to open HeidiSQL

### 3. Create the database

In HeidiSQL, open the **Query** tab and run:

```sql
CREATE DATABASE bookwise_db;
```

Press **F9** to execute. You should see `bookwise_db` appear on the left panel.

### 4. Run the app

Open a terminal in the project folder and run:

```bash
./mvnw spring-boot:run
```

Wait until you see:
```
Tomcat started on port 8080
Started BookwiseApiApplication
```

---

## Using the App

Once running, open your browser:

| Page | URL |
|---|---|
| Book list | http://localhost:8080/ |
| Add a book | http://localhost:8080/add-book.html |
| Edit a book | http://localhost:8080/edit-book.html |

---

## API Endpoints

Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| GET | `/api/books/search?title=` | Search books by title |
| POST | `/api/books` | Add a new book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |

### Example — Add a book (POST)

**URL:** `POST http://localhost:8080/api/books`  
**Header:** `Content-Type: application/json`  
**Body:**
```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "available": true
}
```
**Expected response:** `201 Created`

### Example — Get all books (GET)

**URL:** `GET http://localhost:8080/api/books`  
**Expected response:** `200 OK` with an array of books

---

## Troubleshooting

| Error | Cause | Fix |
|---|---|---|
| `Communications link failure` | Laragon MySQL not running | Open Laragon → Start All |
| `Unknown database 'bookwise_db'` | Database not created | Run `CREATE DATABASE bookwise_db;` in HeidiSQL |
| `Access denied for user 'root'` | Wrong MySQL password | Update `spring.datasource.password` in `application.properties` |
| Port 8080 already in use | Another app using the port | Change `server.port=8081` in `application.properties` |
| Lombok errors / red underlines | Annotation processing off | Enable it in your IDE settings |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.14 |
| Build tool | Maven |
| Database | MySQL (via Laragon) |
| ORM | Spring Data JPA / Hibernate |
| Frontend | HTML + CSS + Vanilla JavaScript |