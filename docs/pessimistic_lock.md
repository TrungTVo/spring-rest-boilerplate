# Pessimistic Lock

Use `Redisson` distributed lock to implement pessimistic locking for the `PUT /animal/{animalId}` endpoint, which updates the balance of an animal record. This ensures that only one instance can update the balance of a specific animal at any given time, preventing race conditions and ensuring data consistency across multiple instances of the application.

## Terminal 0 — start MySQL Database and Redis
```bash
docker compose -f ./mysql-redis/docker-compose.yml up -d
```
Then build the application:
```bash
./mvnw clean package -DskipTests
```

## Terminal 1 — instance `A` on port `8080`
```bash
java -jar target/boilerplate-0.0.1-SNAPSHOT.jar --instance=A --server.port=8080
```

## Terminal 2 — instance `B` on port `8081`
```bash
java -jar target/boilerplate-0.0.1-SNAPSHOT.jar --instance=B --server.port=8081
```

## Terminal 3 — instance `C` on port `8082`
```bash
java -jar target/boilerplate-0.0.1-SNAPSHOT.jar --instance=C --server.port=8082
```

## Terminal 4 — simulate 10 concurrent balance update requests
Run `docs/scripts/high_concurrency_requests.sh` script with the `animal ID` as an argument. You can find the animal ID in the `MySQL` database after inserting a sample animal record with balance `10.00`. For example, if the animal ID is `69d1bab2fa75b9259b06d213`, run:

```bash
sh docs/scripts/high_concurrency_requests.sh 69d1bab2fa75b9259b06d213
```

This will fire 10 concurrent `PUT /animal/{animalId}` requests to random instances (`A`, `B`, or `C`), all trying to update the balance on the same animal record.

# Output

```
----- request 1 -> 8080 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}

----- request 2 -> 8082 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance C failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}

----- request 3 -> 8080 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}

----- request 4 -> 8080 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}

----- request 5 -> 8082 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance C failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}

----- request 6 -> 8082 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance C failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}

----- request 7 -> 8081 -----
{"data":{"id":"fc24b6ec-cc4f-45c4-a852-dcab0a51307f","name":"rabbit","age":25,"balance":9.00,"habitat":null,"medicalRecord":null,"caretakers":[]},"metadata":{"statusCode":200,"status":"Success","message":"Successfully updated animal","errors":null}}

----- request 8 -> 8080 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}

----- request 9 -> 8081 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance B failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}

----- request 10 -> 8080 -----
{"data":null,"metadata":{"statusCode":500,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f","errors":null}}
```

## Instance `A`

```bash
2026-06-06T11:30:13.393-05:00 ERROR 42137 --- [spring-rest-boilerplate] [nio-8080-exec-2] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
2026-06-06T11:30:13.393-05:00 ERROR 42137 --- [spring-rest-boilerplate] [nio-8080-exec-4] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
2026-06-06T11:30:13.393-05:00 ERROR 42137 --- [spring-rest-boilerplate] [nio-8080-exec-5] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
2026-06-06T11:30:13.393-05:00 ERROR 42137 --- [spring-rest-boilerplate] [nio-8080-exec-1] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
2026-06-06T11:30:13.393-05:00 ERROR 42137 --- [spring-rest-boilerplate] [nio-8080-exec-3] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
```

## Instance `B`

```bash
2026-06-06T11:30:13.392-05:00 ERROR 42374 --- [spring-rest-boilerplate] [nio-8081-exec-2] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance B failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
2026-06-06T11:30:13.447-05:00  INFO 42374 --- [spring-rest-boilerplate] [nio-8081-exec-1] c.e.b.t.AnimalTransactions               : ✅ Instance B successfully updated animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
```

## Instance `C`

```bash
2026-06-06T11:30:13.393-05:00 ERROR 42539 --- [spring-rest-boilerplate] [nio-8082-exec-3] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance C failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
2026-06-06T11:30:13.393-05:00 ERROR 42539 --- [spring-rest-boilerplate] [nio-8082-exec-1] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance C failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
2026-06-06T11:30:13.393-05:00 ERROR 42539 --- [spring-rest-boilerplate] [nio-8082-exec-2] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance C failed to acquire lock for animal with ID: fc24b6ec-cc4f-45c4-a852-dcab0a51307f
```
