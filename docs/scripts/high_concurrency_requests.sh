#!/bin/bash
set -u

animal_id="$1"

tmpdir="$(mktemp -d)"
start_file="$tmpdir/start"
trap 'rm -rf "$tmpdir"' EXIT

# Send 10 concurrent PUT /animal/{animalId} requests to update balance for same animal ID, spread across all 3 instances
for i in {1..10}; do
  (
    port="808$((RANDOM % 3))"

    # Wait until all workers are ready
    while [ ! -f "$start_file" ]; do
      sleep 0.001
    done

    {
      echo "----- request $i -> $port -----"

      curl -sS -X PUT "http://localhost:${port}/animal/${animal_id}" \
        --header 'Content-Type: application/json' \
        --data '{"name":"rabbit","age":25,"password":"password"}' \
        | jq -c .

      echo
    } > "$tmpdir/$i.out" 2>&1
  ) &
done

# Release all 10 workers at roughly the same time
touch "$start_file"

wait

# Print cleanly after all requests finish
for i in {1..10}; do
  cat "$tmpdir/$i.out"
done
