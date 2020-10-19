# `limber-backend:common:client`

`LimberHttpClient` enables implementation of client classes
that interact with Limber service APIs.
Services use these clients to communicate with other services.
Each Limber module generally implements a client for use by other services,
as well as by its own tests.

### Example

```kotlin
val httpClient: LimberHttpClient = LimberHttpClientImpl(baseUrl) {
  header("Accept-Version", "v2") // For most requests, we want to use v2 of the API.
  // This header value can be overridden on a per-request basis.
}
```
