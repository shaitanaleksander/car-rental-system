---
name: run-tests
description: Build the project and run all tests, then report results
disable-model-invocation: true
allowed-tools: Bash, Read, Glob
---

# Run Tests

Run the project test suite and report results.

## Steps

1. Run tests:
```bash
./mvnw test
```

2. If tests fail:
   - Read the test output and identify failures
   - Summarize which tests failed and why
   - Suggest fixes if the cause is obvious

3. If tests pass:
   - Report success with test count

4. If `$ARGUMENTS` is provided, run only matching tests:
```bash
./mvnw test -Dtest="$ARGUMENTS"
```
