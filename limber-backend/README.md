# Limber Backend

## Entities

- `healthCheck` (health check module)
- `user` (users module)

## Packages

```
io.limberapp
├── auth: Authorization library code.
│   ├── auth: Authorization implementations (library & application).
│   ├── exception
│   └── jwt: JWT data classes and principal.
├── api
│   └── <entity>: Per-entity API implementations.
├── client: HTTP client library code.
│   ├── exception
│   └── <entity>: Per-entity HTTP clients.
├── config: Config library code and implementations.
├── endpoint
│   └── <entity>: Per-entity endpoints.
├── exception: REST exception library code, including some implementations.
│   └── <entity>: Per-entity exception classes.
├── logging: Multiplatform logging library code.
├── mapper
│   └── <entity>: Per-entity model/rep mappers.
├── model
│   └── <entity>: Per-entity models.
├── module: Module and feature library code.
│   └── <module>: Module implementations.
├── permissions: Permissions library code base classes.
│   └── feature: Feature permissions library code & implementations.
│   └── limber: Limber permissions library code.
│   └── org: Org permissions library code.
├── rep: Rep base classes.
│   └── <entity>: Per-entity rep implementations (and test fixtures).
├── restInterface: Endpoint and routing library code.
│   └── exception
├── serialization: JSON-serialization library code.
├── server: Server library code.
│   ├── exception
│   ├── feature: Ktor feature library code (for server library code).
│   └── <server>: Server implementations and their main entrypoints.
├── service
│   └── <entity>: Per-entity services.
├── sql: SQL library code.
│   └── store: SQL store base classes (library code).
├── store
│   └── <entity>: Per-entity database stores.
├── testing
│   └── integration: Integration test library code and per-module implementations.
├── typeConversion: Type conversion library code, used for serialization and databases.
│   ├── exception
│   └── typeConverter: Type converter implementations (library code).
├── util
│   ├── darb: DARB-related utility code.
│   ├── string: String-related utility code.
│   ├── time: Time-related utility code.
│   ├── url: URL-related utility code.
│   └── uuid: GUID-related utility code.
├── validation: String and rep validation code.
```
