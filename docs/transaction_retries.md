# Transaction Retries

This branch adds a small Spring Retry demo around animal creation. The happy path for
`POST /animal/create` still creates an animal, but sending a request with a `null`
`name` now intentionally fails at the database layer and shows the retry behavior.

## What changed

- `pom.xml` adds `spring-retry` and `spring-boot-starter-aspectj`.
- `BoilerplateApplication` enables retry support with `@EnableRetry`.
- `Animal.name` is marked with `@Column(nullable = false)`, so Hibernate/H2 rejects
  inserts where the name is `null`.
- `AnimalService.createAnimal(...)` is annotated with `@Retryable`, using:
  - `retryFor = RuntimeException.class`
  - `maxAttempts = 3`
  - backoff starting at `1000ms` with multiplier `2`
- `AnimalService.cannotCreateAnimal(...)` is annotated with `@Recover` and throws a
  final error after all retry attempts are exhausted.

## High-level code snippets

Retry support is enabled at the application level:

```java
@SpringBootApplication
@EnableRetry
public class BoilerplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(BoilerplateApplication.class, args);
    }
}
```

The demo failure is triggered by making `Animal.name` required at the database
column level:

```java
@Entity
public class Animal {
    @Column(nullable = false)
    private String name;
}
```

The controller keeps the same create endpoint and delegates to the retryable
service method:

```java
@PostMapping("create")
public ResponseEntity<Response<?>> createAnimal(@RequestBody AnimalRequest request) {
    return this.response(
            this.animalService.createAnimal(request),
            "Successfully created animal",
            HttpStatus.CREATED);
}
```

The retry behavior lives on `AnimalService.createAnimal(...)`. If a runtime
exception is thrown while saving, Spring retries the method up to 3 total
attempts before calling the recovery method:

```java
@Retryable(
    retryFor = RuntimeException.class,
    maxAttempts = 3,
    backoff = @Backoff(delay = 1000, multiplier = 2)
)
public AnimalDTO createAnimal(AnimalRequest request) {
    return executeCreateAnimal(request);
}

@Recover
public AnimalDTO cannotCreateAnimal(RuntimeException ex, AnimalRequest request) {
    throw new RuntimeException("Failed to create animal after 3 retries: " + ex.getMessage());
}

@Transactional(rollbackFor = Exception.class)
public AnimalDTO executeCreateAnimal(AnimalRequest request) {
    Animal animal = new Animal(request.name(), request.age(), request.password());
    return this.animalDTOMapper.apply(this.animalRepository.save(animal));
}
```

## Demo flow

Call the create endpoint with `name` set to `null`:

```bash
curl -X POST http://localhost:8080/animal/create \
  -H "Content-Type: application/json" \
  -d '{"name": null, "age": 3, "password": "pass"}'
```

The request reaches `AnimalController.createAnimal(...)`, which calls
`AnimalService.createAnimal(...)`. Because the service method is `@Retryable`,
Spring Retry wraps the call. `executeCreateAnimal(...)` builds an `Animal` with a
`null` name and tries to save it. The new non-null column constraint causes the
save to throw a runtime exception, so Spring Retry calls the create flow again.

After 3 total attempts, Spring calls the `@Recover` method and returns the final
failure message:

```text
Failed to create animal after 3 retries: ...
```

This is useful as a simple reproducible demo: one invalid request shows the retry
configuration, backoff timing, rollback/failure handling, and final recovery path.

## How Spring Retry matches `@Recover` ?

Spring Retry matches `@Recover` by:

1. Same exception class as `@Retryable` method
2. Same return type
3. First parameter = the exception type (exact or supertype)
4. Remaining parameters = `@Retryable` params (left to right, trailing ones can be omitted)

If multiple `@Recover` match → most specific exception type wins

If no `@Recover` matches    → last exception propagates to caller