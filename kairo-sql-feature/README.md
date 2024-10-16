# `kairo-sql-feature`

The SQL Feature adds support for SQL databases.
Under the hood, this Feature uses [JDBI](https://jdbi.org/).

At the current time, this only supports Postgres.
However, it would not be a huge lift to support other SQL databases.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-sql-feature:$kairoVersion")
}
```

### Step 2: Define your model

Models are data classes that represent entities internally.
They differ from reps in that they aren't exposed to the frontend.
They are often 1:1 with database tables, but may not always be.

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookModel.kt

data class LibraryBookModel(
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

  data class Update(
    val title: String,
    val author: String?,
  ) {
    constructor(model: LibraryBookModel) : this(
      title = model.title,
      author = model.author,
    )
  }
}
```

### Step 3: Create the store

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookStore.kt

class LibraryBookStore @Inject constructor() : SqlStore.ForType<LibraryBookModel>() {
  suspend fun get(id: KairoId): LibraryBookModel? =
    get(id, forUpdate = false)

  suspend fun listAll(): List<LibraryBookModel> =
    sql { handle ->
      val query = handle.createQuery(rs("store/libraryBook/listAll.sql"))
      return@sql query.mapToType().toList()
    }

  suspend fun searchByIsbn(isbn: String): List<LibraryBookModel> =
    sql { handle ->
      val query = handle.createQuery(rs("store/libraryBook/searchByIsbn.sql"))
      query.bind("isbn", isbn)
      return@sql query.mapToType().toList()
    }

  suspend fun searchByText(title: String?, author: String?): List<LibraryBookModel> =
    sql { handle ->
      val query = handle.createQuery(rs("store/libraryBook/searchByIsbn.sql"))
      query.bind("title", title)
      query.bind("author", author)
      return@sql query.mapToType().toList()
    }

  suspend fun create(creator: LibraryBookModel.Creator): LibraryBookModel =
    sql { handle ->
      logger.info { "Creating library book: $creator." }
      val query = handle.createQuery(rs("store/libraryBook/create.sql"))
      query.bindKotlin(creator)
      return@sql query.mapToType().single()
    }

  suspend fun update(
    id: KairoId,
    updater: Updater<LibraryBookModel.Update>,
  ): LibraryBookModel =
    sql { handle ->
      val libraryBook = get(id, forUpdate = true) ?: throw unprocessable(LibraryBookNotFound())
      val update = updater.update(LibraryBookModel.Update(libraryBook))
      logger.info { "Updating library book: $update." }
      val query = handle.createQuery(rs("store/libraryBook/update.sql"))
      query.bind("id", id)
      query.bindKotlin(update)
      return@sql query.mapToType().single()
    }

  suspend fun delete(id: KairoId): LibraryBookModel =
    sql { handle ->
      val query = handle.createQuery(rs("store/libraryBook/delete.sql"))
      query.bind("id", id)
      return@sql query.mapToType().singleNullOrThrow() ?: throw unprocessable(LibraryBookNotFound())
    }

  private suspend fun get(id: KairoId, forUpdate: Boolean): LibraryBookModel? =
    sql { handle ->
      val query = handle.createQuery(rs("store/libraryBook/get.sql"))
      query.define("lockingClause", if (forUpdate) "for no key update" else "")
      query.bind("id", id)
      return@sql query.mapToType().singleNullOrThrow()
    }
}
```

```postgresql
-- src/main/resources/store/libraryBook/get.sql

select *
from library.library_book
where id = :id
<lockingClause>
```

```postgresql
-- src/main/resources/store/libraryBook/listAll.sql

select *
from library.library_book
```

```postgresql
-- src/main/resources/store/libraryBook/searchByIsbn.sql

select *
from library.library_book
where isbn = :isbn
```

```postgresql
-- src/main/resources/store/libraryBook/searchByText.sql

select *
from library.library_book
where (:title is null or title = :title)
  and (:author is null or author = :author)
```

```postgresql
-- src/main/resources/store/libraryBook/create.sql

insert into library.library_book (id, title, author, isbn)
values (:id, :title, :author, :isbn)
returning *
```

```postgresql
-- src/main/resources/store/libraryBook/update.sql

update library.library_book
set title  = :title,
    author = :author
where id = :id
returning *
```

```postgresql
-- src/main/resources/store/libraryBook/listAll.sql

delete
from library.library_book
where id = :id
returning *
```

### Step 4: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

sql:
  jdbcUrl: "jdbc:postgresql://localhost/monolith"
  properties: { }
  connectionTimeoutMs: 5000 # 5 seconds.
  minimumIdle: 16
  maximumPoolSize: 64 # Matches rest.parallelism.callGroupSize.
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoSqlFeature(config.sql)
```
