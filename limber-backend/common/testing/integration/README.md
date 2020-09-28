# `limber-backend:common:testing:integration`

Limber server integration testing library.
Note that this library is not necessary for unit testing.

Modules that offer integration testing should define the following classes:
- `IntegrationTest`, which extends this module's `LimberIntegrationTest` class.
    Integration tests in the module should extend this `IntegrationTest` class.
- `IntegrationTestExtension`, which extends this module's `LimberIntegrationTestExtension` class.
    `IntegrationTest` should have an `@ExtendWith(IntegrationTestExtension::class)` annotation.
