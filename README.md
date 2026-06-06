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

## Other use cases

Check out these branches for more features:
- [**cursor-pagination**](https://github.com/trungtvo/spring-rest-boilerplate/tree/cursor-pagination): Cursor pagination vs offset pagination
- [**entity-relationships**](https://github.com/TrungTVo/spring-rest-boilerplate/tree/entity-relationship): Example of entity relationships (**one-to-one, one-to-many, many-to-many**) and how to handle them in REST APIs with Hibernate and Spring Data JPA. Usage of `@Transactional` annotation to manage transactions and ensure data integrity when performing CRUD operations on related entities.
- [**transaction-retries**](https://github.com/TrungTVo/spring-rest-boilerplate/tree/transaction-retries): Understand how to implement retry logic for transactional operations in Spring Data JPA.
- [**optimistic-lock**](https://github.com/TrungTVo/spring-rest-boilerplate/tree/optimistic-lock): Implement optimistic locking with `version` check in Spring Data JPA to handle concurrent updates to the same entity, preventing data inconsistencies and ensuring data integrity.
- [**pessimistic-lock**](https://github.com/TrungTVo/spring-rest-boilerplate/tree/pessimistic-lock): Implement pessimistic locking using `Redisson` lock in Spring Data JPA to handle concurrent access to the same entity, preventing data inconsistencies and ensuring data integrity.
