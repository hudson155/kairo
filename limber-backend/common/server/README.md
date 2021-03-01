# Limber Server

A Limber server is a server that can host any number of modules.

**Microservices:**
In the most extreme example on one end, having one module per server is a pure microservice
architecture.

**Monolith:**
In the most extreme example on the other end, hosting all modules within the same server is a
monolith architecture.

With the Limber framework, you can choose anywhere in between.

Features should not be registered by more than 1 server, but other modules may have to be (for
example, the module for a database type).

### Example

```kotlin
internal class MyServer(application: Application) : Server<MyConfig>(
    application = application,
    modules = listOf(
        HealthCheckFeature(),
        MyFeature1(),
        MyFeature2(),
        SqlModule(),
    ),
    config = ConfigLoader.load("config"),
)
```

- The `HealthCheckFeature` feature enables server health checks.
- The `MyFeature1` and `MyFeature2` features are domain-specific features.
- The `SqlModule` module allows the server to interact with a particular SQL database.

In reality, the module constructors are probably not so simple. The health check feature needs to be
extended to properly check the server's health, and the SQL module will take some config. The
domain-specific features may also take parameters. But this should give the general idea.
