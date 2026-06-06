## Compare `lock` and `try_lock` in Redisson

| Redisson                                  | SQL analogy                                        | Behavior                                   |
| ----------------------------------------- | -------------------------------------------------- | ------------------------------------------ |
| `lock.lock()`                             | `SELECT ... FOR UPDATE`                            | Wait until lock is available               |
| `lock.tryLock()`                          | `SELECT ... FOR UPDATE NOWAIT`                     | Try immediately; fail if unavailable       |
| `lock.tryLock(waitTime, leaseTime, unit)` | `SELECT ... FOR UPDATE` with timeout-like behavior | Wait up to `waitTime`; fail if unavailable |
| `lock.lock(leaseTime, unit)`              | `SELECT ... FOR UPDATE` plus automatic expiry      | Wait, then hold only up to lease time      |


**Example:**

| Code                                    | Auto-extends? | Behavior                                                   |
| --------------------------------------- | ------------: | ---------------------------------------------------------- |
| `lock.lock()`                           |           Yes | Watchdog renews until `unlock()`                           |
| `lock.tryLock(3, TimeUnit.SECONDS)`     |           Yes | Wait max 3s, then watchdog renews until `unlock()`         |
| `lock.lock(15, TimeUnit.SECONDS)`       |            No | Expires after 15s unless unlocked sooner                   |
| `lock.tryLock(3, 10, TimeUnit.SECONDS)` |            No | Wait max 3s, then expires after 10s unless unlocked sooner |
