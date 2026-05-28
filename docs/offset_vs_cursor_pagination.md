Using `H2` database console, run these 2 queries to compare the execution plan of offset pagination and cursor pagination. You can change the offset value and cursor value to test with different pages.

## Goal

Out of `1000` records in the `animal` table, we want to fetch the `19th` page with page `size` of `50`.

There will be `20` pages in total, and each page will have `50` records. Page is `0`-indexed:
- page `1st`: `0`-indexed
- page `2nd`: `1`-indexed
- ...
- page `18th`: `17`-indexed
- page `19th`: `18`-indexed
- page `20th`: `19`-indexed

## Syntax of H2 queries

**offset pagination:**
------------------------
```sql
EXPLAIN ANALYZE
SELECT id, created_at, name, age, password
FROM animal
ORDER BY name ASC, created_at ASC, id ASC
OFFSET 900 ROWS
FETCH NEXT 50 ROWS ONLY;
```

`OFFSET` = `page_index` * `page_size` = `18` * `50` = `900`

Running explain analyze on the above query will show that the database has to scan through `900` records before fetching the next `50` records, which can be inefficient for large datasets. **You can see `scanCount: 950` in the execution plan.**

**cursor pagination:**
------------------------
```sql
EXPLAIN ANALYZE
SELECT id, created_at, name, age, password
FROM animal
WHERE (name, created_at, id) > ('tiger', '2026-05-28T14:25:10.615186Z', '062936fe-8f39-4c37-b325-d1bc6dc132fc')
ORDER BY name ASC, created_at ASC, id ASC
FETCH FIRST 50 ROWS ONLY;
```

To find the cursor value for the `19th` page, we can look at the last record of the `18th` page. Assuming the records are sorted by `name` (ascending), then by `created_at` (ascending), and then by `id` (ascending), run the offset pagination API endpoint first to get the `18th` page (`17`-indexed) of records:

```
GET /animal/filter?page=17&size=50&sort=name,asc
```

The last record of the `18th` page (`17`-indexed) has the following values:
- name: `tiger`
- created_at: `2026-05-28T14:25:10.615186Z`
- id: `062936fe-8f39-4c37-b325-d1bc6dc132fc`

We can then use this record as the cursor value for the `19th` page (`18`-indexed) in the cursor pagination query. 

Running explain analyze on the above query will show that the database can directly jump to the position of the cursor and fetch the next `50` records, which is more efficient than offset pagination. **You can see `scanCount: 50` in the execution plan.**

**The actual result of the cursor pagination query will be the same as the offset pagination query, but the execution plan will show that cursor pagination is more efficient in terms of the number of records scanned.**

## NOTE

- For offset pagination, unsorted paging is possible but nondeterministic. (make sense even without `ORDER BY` clause, but the order of records can be inconsistent across pages)
- For cursor pagination, **sorting** is **required** because the cursor only has meaning inside a defined order. (with `WHERE` clause)

If you want offset and cursor pagination to behave consistently, use the same `ORDER BY` for both.
