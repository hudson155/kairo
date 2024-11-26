# `kairo-rest-client`

A thin wrapper around [Ktor](https://ktor.io/)'s `HttpClient`.

### Implementation notes

CIO is Ktor's custom coroutine-based I/O HTTP client engine factory.
This is an alternative to OkHttp; CIO uses coroutines instead of threads for handling HTTP requests.
There may be some potential footguns with CIO when interacting with other libraries,
especially those that use Thread-local variables.
See [kairo-rest-feature](../kairo-rest-feature) for more information.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-rest-client:$kairoVersion")
}
```

### Step 2: Create and use a client.

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val client = createKairoClient {
  install(ContentNegotiation) {
    register(
      contentType = ContentType.Application.Json,
      converter = JacksonConverter(
        objectMapper = jsonMapper {
          allowUnknownProperties = true
          prettyPrint = true
        }.build(),
        streamRequestBody = false,
      ),
    )
  }
  defaultRequest {
    url("https://example.com/")
  }
}

client.request {
  method = HttpMethod.Post
  url("/library-books")
  accept(ContentType.Application.Json)
  contentType(ContentType.Application.Json)
  setBody(
    """
      {
        "title": "The Name of the Wind",
        "author": "Patrick Rothfuss",
        "isbn": "978-0756405892"
      }
    """.trimIndent(),
  )
}
```
