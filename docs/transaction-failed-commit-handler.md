# Understand Transactional Exception Handling in Spring Data JPA

When using `@Transactional` in Spring Data JPA, it's crucial to understand **when** exceptions are thrown and **where** you can catch them. The key point is that many exceptions related to database constraints and optimistic locking are thrown **after your method returns**, during the flush/commit phase. This means they are **not catchable inside your method**.

The key distinction:

```
Your method executes          → exceptions thrown HERE are catchable inside
        │
        ▼
Method returns to proxy       ← you lose control here
        │
        ▼
em.flush() / conn.commit()    → exceptions thrown HERE are NOT catchable inside
```

## Exceptions thrown during method execution — catchable inside

These happen while your code is running, before the commit phase:
```java
@Transactional
public void doWork(Long id) {
    try {
        // ✓ Catchable — thrown during method execution, not at flush
        Account account = accountRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Account not found"));

    } catch (EntityNotFoundException e) {
        log.warn("Account missing: {}", id);
        // handle gracefully
    }

    try {
        // ✓ Catchable — your own business logic exception
        if (someCondition) throw new InsufficientFundsException(id);

    } catch (InsufficientFundsException e) {
        // handle gracefully
    }

    try {
        // ✓ Catchable — thrown immediately by Hibernate (not deferred)
        em.lock(account, LockModeType.PESSIMISTIC_WRITE);

    } catch (PessimisticLockException e) {
        // handle gracefully
    }
}
```

## Exceptions thrown at flush/commit time — **NOT** catchable inside

These happen **after your method returns**, when Hibernate sends SQL to the database:
```java
@Transactional
public void doWork(String email) {
    try {
        User user = new User(email);
        userRepo.save(user);   // ← no SQL yet, just queued in Persistence Context

        // ✗ Never reaches here for DB-level exceptions
        // Hibernate hasn't flushed yet — no SQL sent to DB
    } catch (DataIntegrityViolationException e) {
        // ✗ This catch block is USELESS for DB constraint violations
        // The exception fires at flush time, after this method exits
    }

    // same for optimistic lock:
    try {
        account.setBalance(newBalance);
        // ✗ version check hasn't happened yet — SQL not sent yet
    } catch (ObjectOptimisticLockingFailureException e) {
        // ✗ Never runs
    }
}
```

The exceptions that are always thrown at flush/commit time are following.
These all fire when Hibernate sends SQL → AFTER your method returns:

- **ObjectOptimisticLockingFailureException**  : version mismatch detected at flush
- **DataIntegrityViolationException**          : unique constraint, FK violation at flush  
- **ConstraintViolationException**             : DB constraint violation at flush
- **JpaSystemException**                       : general Hibernate flush failure


## The one exception to the rule — forced flush

You can make flush-time exceptions catchable by **manually flushing inside your method**:

```java
@Transactional
public void createUser(String email) {
    try {
        userRepo.save(new User(email));
        em.flush();   // ← force SQL to execute NOW, still inside the method

        // ✓ Now catchable — flush happened inside the method body
    } catch (DataIntegrityViolationException e) {
        log.warn("Email already exists: {}", email);
        throw new DuplicateEmailException(email);  // translate to business exception
    }
}
```

This pattern is useful when you want to catch a DB constraint violation and translate it into a meaningful business exception, rather than letting the raw Spring exception bubble up.