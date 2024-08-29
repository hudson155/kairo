# `kairo-rest-feature`

The REST Feature adds support for REST endpoints.
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

internal data class LibraryBookRep(
  val id: KairoId,
  val title: String,
) {
  internal data class Creator(
    val title: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  internal data class Update(
    val title: String? = null,
  )
}
```

Updates must be marked with `@JsonInclude(value = JsonInclude.Include.NON_NULL)`
so that Kairo can differentiate between `null` and `undefined` from the frontend
using Java's `Optional` class.
See [kairo-serialization](/kairo-serialization/) fore more detail.

### Step 3: Define your REST endpoints

REST endpoint implementations define the API contract for REST API endpoints.
They are intended to be grouped together using Kotlin singleton objects.

Implementations must be Kotlin data classes or data objects.

It's perfectly acceptable for two endpoints to have the same method and path,
as long as they differ by query params, accept headers, or content types.
Routing takes all of these into account.

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookApi.kt

object TypicalLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  data class Get(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  data object ListAll : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  data class SearchByIsbn(
    @QueryParam val isbn: String,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  data class SearchByText(
    @QueryParam val title: String?,
    @QueryParam val author: String?,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  data class Create(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("PATCH")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  data class Update(
    @PathParam val libraryBookId: KairoId,
    override val body: LibraryBookRep.Update,
  ) : RestEndpoint<LibraryBookRep.Update, LibraryBookRep>()

  @RestEndpoint.Method("DELETE")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
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

class LibraryBookHandler @Inject constructor() {
  class Get : RestHandler<LibraryBookApi.Get>()

  class ListAll : RestHandler<LibraryBookApi.ListAll>()

  class SearchByIsbn : RestHandler<LibraryBookApi.SearchByIsbn>()

  class SearchByText : RestHandler<LibraryBookApi.SearchByText>()

  class Create : RestHandler<LibraryBookApi.Create>()

  class Update : RestHandler<LibraryBookApi.Update>()

  class Delete : RestHandler<LibraryBookApi.Delete>()
}
```
