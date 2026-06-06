# Optimistic Lock

Use `version` field in the `Animal` entity to implement optimistic locking. This way, when multiple concurrent requests try to update the same animal record, only one will succeed while the others will fail with a `409 Conflict` error, preventing data inconsistencies.

## Terminal 0 — start MySQL Database
```bash
docker compose -f ./mysql/docker-compose.yml up -d
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
----- request 1 -> 8081 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance B failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}

----- request 2 -> 8080 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}

----- request 3 -> 8080 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}

----- request 4 -> 8082 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance C failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}

----- request 5 -> 8082 -----
{"data":{"id":"1f5c4c64-63f4-4b31-85d7-04d5ed1562a6","name":"rabbit","age":25,"balance":9.00,"habitat":null,"medicalRecord":null,"caretakers":[],"version":0},"metadata":{"statusCode":200,"status":"Success","message":"Successfully updated animal","errors":null}}

----- request 6 -> 8081 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance B failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}

----- request 7 -> 8081 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance B failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}

----- request 8 -> 8081 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance B failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}

----- request 9 -> 8080 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}

----- request 10 -> 8080 -----
{"data":null,"metadata":{"statusCode":409,"status":"Error","message":"❌ Conflict! Server busy! Instance A failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']","errors":null}}
```

## Instance `A`

```bash
2026-06-04T14:25:25.336-05:00 ERROR 96877 --- [spring-rest-boilerplate] [nio-8080-exec-1] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
2026-06-04T14:25:25.336-05:00 ERROR 96877 --- [spring-rest-boilerplate] [nio-8080-exec-4] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
2026-06-04T14:25:25.336-05:00 ERROR 96877 --- [spring-rest-boilerplate] [nio-8080-exec-2] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
2026-06-04T14:25:25.336-05:00 ERROR 96877 --- [spring-rest-boilerplate] [nio-8080-exec-3] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance A failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
```

## Instance `B`

```bash
2026-06-04T14:25:25.331-05:00 ERROR 97043 --- [spring-rest-boilerplate] [nio-8081-exec-2] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance B failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
2026-06-04T14:25:25.331-05:00 ERROR 97043 --- [spring-rest-boilerplate] [nio-8081-exec-1] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance B failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
2026-06-04T14:25:25.331-05:00 ERROR 97043 --- [spring-rest-boilerplate] [nio-8081-exec-3] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance B failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
2026-06-04T14:25:25.332-05:00 ERROR 97043 --- [spring-rest-boilerplate] [nio-8081-exec-4] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance B failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
```

## Instance `C`

```bash
2026-06-04T14:25:25.324-05:00  INFO 97237 --- [spring-rest-boilerplate] [nio-8082-exec-2] c.e.b.t.AnimalTransactions               : ✅ Instance C successfully updated animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6
2026-06-04T14:25:25.327-05:00 ERROR 97237 --- [spring-rest-boilerplate] [nio-8082-exec-1] c.e.b.s.AnimalService                    : ❌ Conflict! Server busy! Instance C failed to update animal with ID: 1f5c4c64-63f4-4b31-85d7-04d5ed1562a6. Error: Unexpected row count (expected row count 1 but was 0) [update animal set age=?,balance=?,habitat_id=?,name=?,password=?,version=? where id=? and version=?] for entity [com.example.boilerplate.commons.models.Animal with id '1f5c4c64-63f4-4b31-85d7-04d5ed1562a6']
```
