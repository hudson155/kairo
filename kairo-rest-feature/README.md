# `kairo-rest-feature`

**This Feature is used by the [kairo-sample](https://github.com/hudson155/kairo-sample) repository.**

The REST Feature adds support for REST endpoints, including auth.
Under the hood, this Feature uses [Ktor](https://ktor.io/).
Even if you're not building a REST API, you might need to include this to support health checks.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-rest-feature:$kairoVersion")
}
```

### Step 2: Define your rep

Reps are just data classes that represent request and response bodies

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookRep.kt

data class LibraryBookRep(
  val id: KairoId,
  val title: String,
  val author: String?,
  val isbn: String,
) {
  data class Creator(
    val title: String,
    val author: String?,
    val isbn: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  data class Update(
    val title: String? = null,
    val author: Optional<String>? = null,
  )
}
```

Updates must be marked with `@JsonInclude(value = JsonInclude.Include.NON_NULL)`
so that Kairo can differentiate between `null` and `undefined` from the frontend
using Java's `Optional` class.
See [kairo-serialization](../kairo-serialization) fore more detail.

### Step 3: Define your REST endpoints

REST endpoint implementations define the API contract for REST API endpoints.
They are intended to be grouped together using Kotlin singleton objects.

Implementations must be Kotlin data classes or data objects.

It's perfectly acceptable for two endpoints to have the same method and path,
as long as they differ by query params, `Content-Type` header, or `Accept` header.
Routing takes all of these into account.

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookApi.kt

object TypicalLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  data class Get(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  data object ListAll : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  data class SearchByIsbn(
    @QueryParam val isbn: String,
    @QueryParam val strict: Boolean?,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  data class SearchByText(
    @QueryParam val title: String?,
    @QueryParam val author: String?,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  data class Create(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("PATCH")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  data class Update(
    @PathParam val libraryBookId: KairoId,
    override val body: LibraryBookRep.Update,
  ) : RestEndpoint<LibraryBookRep.Update, LibraryBookRep>()

  @RestEndpoint.Method("DELETE")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  data class Delete(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep>()
}
```

### Step 4: Create your handlers

REST handler implementations are the entrypoints for a specific REST endpoints.
They are intended to be grouped together using Kotlin singleton objects.

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookHandler.kt

class OrganizationHandler @Inject constructor() {
  inner class Get : RestHandler<LibraryBookApi.Get, LibraryBookRep>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.Get): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.Get): LibraryBookRep {
      TODO()
    }
  }

  inner class ListAll : RestHandler<LibraryBookApi.ListAll, List<LibraryBookRep>>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.ListAll): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.ListAll): List<LibraryBookRep> {
      TODO()
    }
  }

  inner class SearchByIsbn : RestHandler<LibraryBookApi.SearchByIsbn, List<LibraryBookRep>>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.SearchByIsbn): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.SearchByIsbn): List<LibraryBookRep> {
      TODO()
    }
  }

  inner class SearchByText : RestHandler<LibraryBookApi.SearchByText, List<LibraryBookRep>>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.SearchByText): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.SearchByText): List<LibraryBookRep> {
      TODO()
    }
  }

  inner class Create : RestHandler<LibraryBookApi.Create, LibraryBookRep>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.Create): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.Create): LibraryBookRep {
      TODO()
    }
  }

  inner class Update : RestHandler<LibraryBookApi.Update, LibraryBookRep>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.Update): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.Update): LibraryBookRep {
      TODO()
    }
  }

  inner class Delete : RestHandler<LibraryBookApi.Delete, LibraryBookRep>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.Delete): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.Delete): LibraryBookRep {
      TODO()
    }
  }
}
```

### Step 4: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

auth:
  jwtIssuer: "https://example.com/"
  jwtSecret: { source: "GcpSecret", id: "projects/012345678900/secrets/example/versions/1" }
  leewaySec: 20 # 20 seconds.

rest:
  connector:
    host: "0.0.0.0"
    port: 8080
  lifecycle:
    shutdownGracePeriodMs: 0
    shutdownTimeoutMs: 25_000 # 25 seconds.
  parallelism:
    connectionGroupSize: ~
    workerGroupSize: 4
    callGroupSize: 16
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoRestFeature(config.rest) {
  install(Auth) {
    kairoConfigure {
      add(
        JwtAuthVerifier(
          schemes = listOf("Bearer"),
          mechanisms = listOf(
            JwtJwtAuthMechanism(
              issuers = listOf(authConfig.jwtIssuer),
              algorithm = Algorithm.HMAC256(authConfig.jwtSecret.value),
              leewaySec = authConfig.leewaySec,
            ),
          ),
        ),
      )
    }
  }
}
```
