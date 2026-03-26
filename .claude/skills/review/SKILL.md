---
name: review
description: Review current branch changes for code quality, conventions, and potential issues
disable-model-invocation: true
allowed-tools: Bash, Read, Glob, Grep
---

# Code Review

Review all changes on the current branch compared to `master`.

## Steps

1. Get the diff:
```bash
git diff master...HEAD
```
If on master, review uncommitted changes:
```bash
git diff
```

2. Read each changed file fully for context

3. Check for:
   - **Lombok usage**: Are Lombok annotations used where possible? No manual getters/setters/constructors when Lombok can handle it
   - **Architecture**: Controller -> Service -> Repository layering respected? DTOs used instead of exposing entities?
   - **Injection**: Constructor injection via `@RequiredArgsConstructor`, no `@Autowired`
   - **Naming**: English, clear, follows Java conventions
   - **Security**: No hardcoded credentials, no SQL injection, proper input validation
   - **Transactions**: `@Transactional` on service write methods
   - **Code quality**: No dead code, no unused imports, no overly complex methods

4. Report findings organized by severity:
   - **Must fix**: Bugs, security issues, broken patterns
   - **Should fix**: Convention violations, missing annotations
   - **Suggestions**: Optional improvements
