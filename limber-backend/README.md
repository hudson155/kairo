# Limber backend

## Package structure

```
io.limberapp
├── auth
│   └── jwt: Classes representing and regarding JWTs (see the common:auth
│            module).
├── client: HTTP client for interaction with other services (see the
│           common:client module).
├── config: Classes representing and regarding application configuration (see
│           the common:config module).
├── exception: Application semantic exceptions, broken down by HTTP response
│              error code (see the common:exceptions module). Also, code to map
│              these exceptions to HTTP response body errors (see the
│              common:exception-mapping module).
├── finder: The finder interface, related interfaces, and supporting classes
│           (see the common:finder module).
├── reps: Base classes representing HTTP request and response bodies, including
│         errors (see the common:exception-mapping module).
├── serialization: Shared JSON serialization code (see the common:serialization
│                  module).
└── util
    ├── darb: Code relating to the Dense(ish) Albeit Readable Binary format
    │         (see the common:util module, especially the DarbEncoder class).
    ├── url: Code relating to URLs, including encoding, parsing, and
    │        manipulation (see the common:util module).
    └── uuid: Code relating to UUIDs, including encoding (see the common:util
              module).

kotlin: Custom extensions to the Kotlin standard library (see the common:util
        module).
```
