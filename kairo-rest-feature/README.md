# `kairo-rest-feature`

The REST Feature adds support for REST endpoints.
Under the hood, this Feature uses [Ktor](https://ktor.io/).
Even if you're not building a REST API, you might need to include this to support health checks.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("kairo:kairo-rest-feature:0.4.0")
}
```

### Step 2: Define your REST endpoints

REST endpoint implementations define the API contract for REST API endpoints.
They are intended to be grouped together using Kotlin singleton objects.

It's perfectly acceptable for two endpoints to have the same method and path,
as long as they differ by query params, accept headers, or content types.
Routing takes all of these into account.

```kotlin
// src/main/kotlin/yourPackage/feature/library/book/LibraryBookApi.kt

object LibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/books/:bookId")
  @RestEndpoint.Accept("application/json")
  data class Get(
    @PathParam val bookId: String,
  ) : RestEndpoint<Nothing, BookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/books")
  @RestEndpoint.Accept("application/json")
  data object ListAll : RestEndpoint<Nothing, List<BookRep>>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/books")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  data class Create(
    override val body: BookRep.Creator,
  ) : RestEndpoint<BookRep.Creator, BookRep>()
}
```
