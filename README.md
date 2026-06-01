# TaskFlow Pro — Spring Boot API

Java 17 · Spring Boot 3.2.5 · Spring Security (JWT) · Spring Data JPA · MySQL 8.4 · Maven.

## Run locally

1. **Create the database** (or just let `ddl-auto: update` build it for you):
   ```bash
   mysql -u root -p < src/main/resources/schema.sql
   ```

2. **Set environment variables** (or edit `application.yml`):
   ```bash
   export DB_USERNAME=root
   export DB_PASSWORD=root
   export JWT_SECRET="$(openssl rand -base64 48)"
   export CORS_ORIGINS="http://localhost:5173,http://localhost:8080"
   ```

3. **Start the API:**
   ```bash
   ./mvnw spring-boot:run
   # or
   mvn spring-boot:run
   ```

   Listens on `http://localhost:8080/api`.

4. **Point the frontend at it:** in the project root, add a `.env` file:
   ```
   VITE_API_URL=http://localhost:8080/api
   ```
   Restart `vite dev`. With no `VITE_API_URL` set, the frontend uses an in-browser mock so you can still demo the UI.

## API

All endpoints are JSON. Authenticated routes require `Authorization: Bearer <jwt>`.

### Auth

| Method | Path             | Body                                | Returns                 |
|--------|------------------|-------------------------------------|-------------------------|
| POST   | `/auth/register` | `{ username, email, password }`     | `{ token, user }`       |
| POST   | `/auth/login`    | `{ email, password }`               | `{ token, user }`       |

### Users

| Method | Path        | Body                  | Returns |
|--------|-------------|-----------------------|---------|
| GET    | `/users/me` | —                     | `User`  |
| PUT    | `/users/me` | `{ username, email }` | `User`  |

### Tasks

| Method | Path           | Body                                                                         | Returns       |
|--------|----------------|------------------------------------------------------------------------------|---------------|
| GET    | `/tasks`       | —                                                                            | `Task[]`      |
| GET    | `/tasks/page`  | query: `page,size,sort`                                                      | `Page<Task>`  |
| POST   | `/tasks`       | `{ title, description?, status?, priority?, dueDate? }`                      | `Task`        |
| PUT    | `/tasks/{id}`  | same as POST (partial via null fields)                                       | `Task`        |
| DELETE | `/tasks/{id}`  | —                                                                            | `204`         |

`status`: `TODO | IN_PROGRESS | COMPLETED` · `priority`: `LOW | MEDIUM | HIGH` · `dueDate`: ISO date (`YYYY-MM-DD`).

### Error format

```json
{ "status": 400, "error": "Bad Request", "message": "…", "details": { "field": "…" }, "timestamp": "…" }
```

## Architecture

```
com.taskflow
├── TaskflowApplication
├── auth/         — register, login, JWT issuance
├── security/     — JwtService, JwtAuthFilter, SecurityConfig, @AuthenticatedUser resolver
├── user/         — User entity, repo, controller, DTO
├── task/         — Task entity, enums, repo, service, controller, DTO
└── common/       — exceptions + global handler
```

- **Controller → Service → Repository** with JPA entities, DTOs for request/response.
- **Stateless JWT**: filter parses the `Bearer` token, hydrates the user from the DB, and sets `SecurityContextHolder`.
- **BCrypt** password hashing.
- **Global exception handler** maps exceptions to a consistent JSON shape.
- **CORS** is configurable via `CORS_ORIGINS`.

## Schema

See `src/main/resources/schema.sql`. Tables: `users`, `tasks` (FK → users, ON DELETE CASCADE).
