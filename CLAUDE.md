# CLAUDE.md — Bookwise API Project

This file tells Claude everything it needs to know about this project.
Read this before writing, editing, or suggesting any code.

---

## Project Overview

**Name:** Bookwise API  
**Type:** Spring Boot REST API + Static HTML Frontend  
**Purpose:** SECJ4383 Software Construction — Assignment 2  
**Description:** A library management system exposing RESTful endpoints for book and member management, with a simple HTML/CSS/JS frontend served directly by Spring Boot.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.14 |
| Build tool | Maven |
| Database | MySQL (via Laragon) |
| ORM | Spring Data JPA / Hibernate |
| Boilerplate reduction | Lombok |
| Frontend | Plain HTML + CSS + vanilla JavaScript |
| API calls (frontend) | `fetch()` API — no framework needed |
| API testing | Postman |
| IDE | IntelliJ IDEA |

---

## Project Structure

```
bookwise-api/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/bookwise/
│       │       ├── BookwiseApiApplication.java     ← main entry point
│       │       ├── controller/
│       │       │   ├── BookController.java         ← REST endpoints (exposed)
│       │       │   └── MemberController.java
│       │       ├── service/
│       │       │   ├── BookService.java            ← business logic
│       │       │   ├── BookServiceImpl.java
│       │       │   ├── MemberService.java
│       │       │   └── MemberServiceImpl.java
│       │       ├── repository/
│       │       │   ├── BookRepository.java         ← JPA repositories
│       │       │   └── MemberRepository.java
│       │       └── model/
│       │           ├── Book.java                   ← JPA entities
│       │           └── Member.java
│       └── resources/
│           ├── static/                             ← frontend lives here
│           │   ├── index.html                      ← book list page
│           │   ├── add-book.html                   ← add book form
│           │   ├── edit-book.html                  ← edit book form
│           │   └── css/
│           │       └── style.css                   ← shared styles
│           └── application.properties              ← DB config lives here
├── pom.xml
└── CLAUDE.md
```

---

## Package Naming

All classes live under `com.bookwise`. Never use `com.example`.

```java
package com.bookwise.controller;
package com.bookwise.service;
package com.bookwise.repository;
package com.bookwise.model;
```

---

## Database — Laragon MySQL

Laragon runs MySQL on **port 3306** by default. The database must exist before running the app.

**Create database (run once in Laragon's MySQL shell or HeidiSQL):**
```sql
CREATE DATABASE bookwise_db;
```

**application.properties:**
```properties
spring.application.name=bookwise-api

# Laragon MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/bookwise_db
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Server
server.port=8080
```

> **Note:** Laragon's MySQL default password is blank (empty string). If you set a custom password in Laragon, update `spring.datasource.password` accordingly.

---

## Five Functionalities

These are the five required functionalities for the assignment:

| # | Functionality | HTTP Method | Endpoint | Exposed as REST? |
|---|---|---|---|---|
| 1 | Add a new book | POST | `/api/books` | Yes |
| 2 | Get all books / get by ID | GET | `/api/books`, `/api/books/{id}` | Yes |
| 3 | Update an existing book | PUT | `/api/books/{id}` | Internal demo |
| 4 | Delete a book | DELETE | `/api/books/{id}` | Internal demo |
| 5 | Search books by title | GET | `/api/books/search?title=` | Internal demo |

Functionalities 1 and 2 are the **two REST API endpoints tested in Postman** for the assignment.

---

## REST API Design Rules

Always follow these conventions when writing controllers:

- Base URL: `/api/books`, `/api/members`
- Use proper HTTP methods: GET (read), POST (create), PUT (update), DELETE (remove)
- Return `ResponseEntity<>` always — never return raw objects
- All responses must be JSON
- Use `@RestController` and `@RequestMapping` on every controller
- Never use `@Controller` alone (that is for MVC/HTML views)

**Example response pattern:**
```java
// Success
return ResponseEntity.ok(book);

// Created
return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);

// Not found
return ResponseEntity.notFound().build();

// No content (delete)
return ResponseEntity.noContent().build();
```

---

## Code Style Rules

Follow these rules for every file written in this project:

### General
- Use **Lombok** annotations to eliminate boilerplate — no manual getters/setters
- Use `@Service`, `@Repository`, `@RestController`, `@Entity` annotations correctly
- Always use a **Service interface + ServiceImpl** pattern (separation of concerns)
- Never put business logic in the Controller — controllers only call services
- Never put database queries in the Service — repositories handle data access

### Entity (Model) template
```java
@Entity
@Table(name = "books")
@Data                  // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor     // Lombok: no-arg constructor (required by JPA)
@AllArgsConstructor    // Lombok: all-arg constructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String isbn;
    private boolean available = true;
}
```

### Repository template
```java
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
}
```

### Service interface template
```java
public interface BookService {
    Book addBook(Book book);
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    List<Book> searchByTitle(String title);
}
```

### Controller template
```java
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(book));
    }
}
```

---

## Laragon Setup Checklist

Before running the Spring Boot app, verify these in Laragon:

1. Laragon is running (green status in the Laragon panel)
2. Apache and MySQL services are both started
3. MySQL port is 3306 (Laragon default — check under Menu → MySQL → my.ini if unsure)
4. Database `bookwise_db` exists (create via HeidiSQL or Laragon terminal)
5. Password is blank (or matches what you set in `application.properties`)

**Access HeidiSQL from Laragon:** Click `Database` button in the Laragon panel.

---

## How to Run

1. Open the project in IntelliJ IDEA
2. Make sure Laragon is running with MySQL started
3. Verify `application.properties` has the correct credentials
4. Click the green Run button (or `Shift + F10`)
5. Wait for the console to show:
   ```
   Tomcat started on port 8080
   Started BookwiseApiApplication
   ```
6. Test with Postman: `GET http://localhost:8080/api/books` → expect `200 OK` with `[]`

---

## Common Errors and Fixes

| Error in console | Cause | Fix |
|---|---|---|
| `Communications link failure` | MySQL (Laragon) not running | Start Laragon → start MySQL |
| `Access denied for user 'root'` | Wrong password | Set correct password in `application.properties` |
| `Unknown database 'bookwise_db'` | DB not created | Run `CREATE DATABASE bookwise_db;` in HeidiSQL |
| `Port 8080 already in use` | Another app on 8080 | Change `server.port=8081` in properties |
| Bean creation error with Lombok | Lombok not enabled in IntelliJ | Enable annotation processing: Settings → Build → Compiler → Annotation Processors |

---

## Postman Testing Reference

### Add a book (POST)
- Method: `POST`
- URL: `http://localhost:8080/api/books`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "available": true
}
```
- Expected response: `201 Created`

### Get all books (GET)
- Method: `GET`
- URL: `http://localhost:8080/api/books`
- Expected response: `200 OK` with array of books

### Get by ID (GET)
- Method: `GET`
- URL: `http://localhost:8080/api/books/1`
- Expected response: `200 OK` with single book

### Update a book (PUT)
- Method: `PUT`
- URL: `http://localhost:8080/api/books/1`
- Body: updated JSON fields
- Expected response: `200 OK`

### Delete a book (DELETE)
- Method: `DELETE`
- URL: `http://localhost:8080/api/books/1`
- Expected response: `204 No Content`

### Search by title (GET)
- Method: `GET`
- URL: `http://localhost:8080/api/books/search?title=clean`
- Expected response: `200 OK` with filtered array

---

## Frontend — Static HTML

### How it works
Spring Boot automatically serves any file placed in `src/main/resources/static/` at the root URL.
No extra server, no CORS issues — everything runs on `http://localhost:8080`.

| Page | URL | Purpose |
|---|---|---|
| `index.html` | `http://localhost:8080/` | List all books, delete button per row |
| `add-book.html` | `http://localhost:8080/add-book.html` | Form to add a new book |
| `edit-book.html` | `http://localhost:8080/edit-book.html` | Form to edit an existing book |

### Frontend rules
- Use plain HTML, CSS, and vanilla JavaScript only — no React, no Vue, no jQuery
- Use the `fetch()` API to call backend REST endpoints
- All API calls go to `http://localhost:8080/api/books`
- No page reloads for actions — use `fetch()` then refresh the list dynamically
- Keep all styles in `css/style.css` — no inline styles

### fetch() pattern to follow

**GET all books and render as a table:**
```javascript
async function loadBooks() {
    const response = await fetch('http://localhost:8080/api/books');
    const books = await response.json();
    const tbody = document.getElementById('book-list');
    tbody.innerHTML = '';
    books.forEach(book => {
        tbody.innerHTML += `
            <tr>
                <td>${book.id}</td>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.isbn}</td>
                <td>${book.available ? 'Yes' : 'No'}</td>
                <td>
                    <a href="edit-book.html?id=${book.id}">Edit</a>
                    <button onclick="deleteBook(${book.id})">Delete</button>
                </td>
            </tr>`;
    });
}
```

**POST a new book from a form:**
```javascript
async function addBook(event) {
    event.preventDefault();
    const book = {
        title: document.getElementById('title').value,
        author: document.getElementById('author').value,
        isbn: document.getElementById('isbn').value,
        available: true
    };
    const response = await fetch('http://localhost:8080/api/books', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(book)
    });
    if (response.ok) {
        window.location.href = 'index.html';
    }
}
```

**DELETE a book:**
```javascript
async function deleteBook(id) {
    await fetch(`http://localhost:8080/api/books/${id}`, { method: 'DELETE' });
    loadBooks();
}
```

**PUT (update) a book:**
```javascript
async function updateBook(id, updatedBook) {
    const response = await fetch(`http://localhost:8080/api/books/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedBook)
    });
    if (response.ok) {
        window.location.href = 'index.html';
    }
}
```

### CORS configuration (required for frontend to talk to backend)
Add this to `BookController.java` or create a separate `CorsConfig.java`:

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

> Since the frontend is served by the same Spring Boot app on port 8080, CORS issues are unlikely — but add this config anyway as a safeguard.

---

## Group Member Responsibilities

| Member | Files to own |
|---|---|
| Member 1 | `BookController.java`, `CorsConfig.java`, `application.properties`, project setup |
| Member 2 | `BookService.java`, `BookServiceImpl.java`, `BookRepository.java` |
| Member 3 | `Member.java`, `MemberService.java`, `MemberServiceImpl.java`, `MemberRepository.java` |
| Member 4 | `Book.java`, `index.html`, `add-book.html`, `edit-book.html`, `style.css`, `README.md`, demo video |

---

## Assignment Requirements Mapping

| Requirement | Implementation |
|---|---|
| 5 functionalities | Add, Get All, Update, Delete, Search |
| 2 REST endpoints | POST `/api/books`, GET `/api/books` |
| Correct HTTP methods | POST, GET, PUT, DELETE |
| JSON responses | `ResponseEntity<>` with `@RestController` |
| Postman tested | All endpoints documented above |
| GitHub README | Describe setup steps + project purpose |

---

*Last updated: Bookwise API — SECJ4383 Assignment 2*