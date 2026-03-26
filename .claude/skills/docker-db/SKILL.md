---
name: docker-db
description: Start or stop the PostgreSQL Docker container for local development
disable-model-invocation: true
allowed-tools: Bash, Read
---

# Docker PostgreSQL Management

Action: $ARGUMENTS (expected: `up`, `down`, or `status`)

## Commands

### Up
Start PostgreSQL container:
```bash
docker run -d --name car-rental-postgres \
  -e POSTGRES_DB=car_rental \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin \
  -p 5432:5432 \
  postgres:17
```

If container already exists but is stopped, run:
```bash
docker start car-rental-postgres
```

After starting, confirm it's running with `docker ps`.

### Down
```bash
docker stop car-rental-postgres
```

### Status
```bash
docker ps -a --filter name=car-rental-postgres
```

## Connection Details
- Host: `localhost`
- Port: `5432`
- Database: `car_rental`
- Username: `admin`
- Password: `admin`
