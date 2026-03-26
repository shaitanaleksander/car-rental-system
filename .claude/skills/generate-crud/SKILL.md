---
name: generate-crud
description: Generate full CRUD stack (Controller, Service, Repository, DTO) for a given entity
disable-model-invocation: true
allowed-tools: Read, Write, Glob, Grep
---

# Generate CRUD Stack

Generate a complete CRUD layer for the specified entity.

Entity name: $ARGUMENTS

## Steps

1. **Read the existing entity** in `src/main/java/org/ratifire/admin/carrentalsystem/entity/` to understand its fields
2. **Read existing CRUD classes** to follow established patterns in the project
3. Generate the following classes:

### Repository
- Path: `src/main/java/org/ratifire/admin/carrentalsystem/repository/<Entity>Repository.java`
- Extend `JpaRepository<Entity, Long>`
- Add custom query methods if obviously needed

### DTO
- Path: `src/main/java/org/ratifire/admin/carrentalsystem/dto/<Entity>Dto.java`
- Use Lombok `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Include all fields except audit fields (`createdAt`, `updatedAt`)
- Never expose the entity directly — always use DTOs

### Service
- Path: `src/main/java/org/ratifire/admin/carrentalsystem/service/<Entity>Service.java`
- Annotate with `@Service`, `@RequiredArgsConstructor`
- Use constructor injection (Lombok)
- Add `@Transactional` on write operations
- Implement: `create`, `getById`, `getAll`, `update`, `delete`
- Map between Entity and DTO manually (or use existing mapper if present)

### Controller
- Path: `src/main/java/org/ratifire/admin/carrentalsystem/controller/<Entity>Controller.java`
- Annotate with `@RestController`, `@RequestMapping("/api/<entities>")`
- Use `@RequiredArgsConstructor` for injection
- Implement standard REST endpoints: GET all, GET by id, POST, PUT, DELETE
- Use proper HTTP status codes (`201` for create, `204` for delete)

## Important
- If the entity does not exist yet, tell the user to run `/generate-entity` first
- Follow existing code patterns in the project
- All code in English
