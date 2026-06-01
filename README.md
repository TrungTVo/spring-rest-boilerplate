# Spring Rest Boilerplate

Author: Trung Vo

Boilerplate code for Spring Boot RESTFUL project, including:

- POJO Models and DTOs
- Custom exception handlers
- CRUD operations (with offset pagination & sorting)
- Swagger OpenAPI
- Logging tool
- HTTP Controllers
- H2 in-memory database
- Spring Actuator (health, info, metrics, etc.)
- Dev Tools (for live reload)

## Quick Run

```
docker compose up
```

## Manual Setup

Start Server

```
./mvnw spring-boot:run
```

Access Swagger UI

```
localhost:8080/swagger-ui/index.html
```

Swagger JSON

```
localhost:8080/v3/api-docs
```

H2 console

```
localhost:8080/h2-console
```

Actuator

```
localhost:8080/actuator
```

## Zoo relationship lab

This project includes a small JPA relationship playground built around the
existing `/animal` endpoint.

- **One-to-one:** `Animal` to `MedicalRecord`
- **One-to-many:** `Habitat` to `Animal`
- **Many-to-many:** `Animal` to `Caretaker`

Animal endpoints:

```
POST   /animal/create
GET    /animal/{animalId}
PUT    /animal/{animalId}
DELETE /animal/{animalId}
PUT    /animal/{animalId}/habitat/{habitatId}
PUT    /animal/{animalId}/medical-record/{recordId}
PUT    /animal/{animalId}/caretakers/{caretakerId}
DELETE /animal/{animalId}/caretakers/{caretakerId}
```

Supporting endpoints:

```
POST   /habitat/create
GET    /habitat/all
GET    /habitat/{habitatId}
PUT    /habitat/{habitatId}
DELETE /habitat/{habitatId}

POST   /medical-record/create
GET    /medical-record/all
GET    /medical-record/{recordId}
PUT    /medical-record/{recordId}
DELETE /medical-record/{recordId}

POST   /caretaker/create
GET    /caretaker/all
GET    /caretaker/{caretakerId}
PUT    /caretaker/{caretakerId}
DELETE /caretaker/{caretakerId}
```
