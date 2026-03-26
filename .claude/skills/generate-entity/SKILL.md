---
name: generate-entity
description: Scaffold a JPA entity class with Lombok annotations in the project's entity package
disable-model-invocation: true
allowed-tools: Read, Write, Glob, Grep
---

# Generate JPA Entity

Create a new JPA entity class in `src/main/java/org/ratifire/admin/carrentalsystem/entity/`.

## Requirements

Entity name: $ARGUMENTS

1. **Read existing entities** first to follow established patterns
2. **Use Lombok annotations**: `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
3. **Use JPA annotations**: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
4. **ID strategy**: Use `GenerationType.IDENTITY`
5. **Add `@Column`** annotations on fields where needed
6. **Use appropriate Java types**: `LocalDateTime` for timestamps, `BigDecimal` for money, `Long` for IDs
7. **Add audit fields**: `createdAt` and `updatedAt` with `LocalDateTime`
8. **Table name**: lowercase snake_case plural (e.g., `Car` -> `cars`)

## Template

```java
package org.ratifire.admin.carrentalsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "<table_name>")
public class <EntityName> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // entity-specific fields here

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

Ask the user what fields the entity should have if not specified.
