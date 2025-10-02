# SQL

Relational databases remain the backbone of many serious applications.
`kairo-sql` standardizes SQL access in Kairo
using [Exposed](https://www.jetbrains.com/exposed/)'s **lightweight & idiomatic ORM DSL**,
with support for async I/O using **R2DBC**.

### Why Exposed?

- **Kotlin-first with async I/O:**
  - JetBrains' own type-safe DSL is Kotlin-idomatic.
  - R2DBC offers enables lightweight async I/O instead of JDBC's blocking approach.

- **Production-ready defaults** with easy overrides.

## Installation

Install `kairo-sql-feature`.\
You don't need to install Exposed separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-sql-feature")
}
```

## Usage

First, add the Feature to your Server.
This example uses Postgres, but you can use any R2DBC-compatible database.

```kotlin
val features = listOf(
  SqlFeature(
    config = config.sql,
    configureDatabase = {
      explicitDialect = PostgreSQLDialect() // Choose your dialect.
    },
  ),
)
```

We recommend using [kairo-config](../kairo-config) to configure the Feature.

```hocon
sql.connectionFactory {
  url = ${POSTGRES_URL}
  username = ${POSTGRES_USERNAME}
  password = ${POSTGRES_PASSWORD}
}
```

### The Exposed DSL, by example

Suppose you have this user model,
where `UserModel` represents a user
and `UserModel.Creator` represents the data needed to create a user.

```kotlin
data class UserModel(
  val id: UserId,
  val createdAt: Instant,
  val emailAddress: String,
) {
  data class Creator(
    val emailAddress: String,
  )
}
```

With Exposed, the first thing you'll do is define your database table.

```kotlin
object UserTable : Table("user") {
  val id: Column<UserId> =
    text("id")
      .transform(::UserId, UserId::value)

  override val primaryKey: PrimaryKey = PrimaryKey(id)

  val createdAt: Column<Instant> =
    timestamp("created_at")
      .defaultExpression(CurrentTimestamp)

  val emailAddress: Column<String> =
    text("email_address")
      .uniqueIndex("uq__user__email_address")
}
```

It's also helpful to write a reusable transformation function
to turn a database row into the model.

```kotlin
fun UserModel.Companion.fromRow(row: ResultRow): UserModel =
  UserModel(
    id = row[UserModel.id],
    createdAt = row[UserModel.createdAt],
    emailAddress = row[UserModel.emailAddress],
  )
```

Now you can write type-safe DSL queries!

```kotlin
suspend fun get(id: UserId): UserModel? =
  suspendTransaction(db = database) {
    UserTable
      .selectAll()
      .where { UserTable.id eq id }
      .map(UserModel::fromRow)
      .singleNullOrThrow()
  }

suspend fun getByEmailAddress(emailAddress: String): UserModel? =
  suspendTransaction(db = database) {
    UserTable
      .selectAll()
      .where { UserTable.emailAddress eq emailAddress }
      .map(UserModel::fromRow)
      .singleNullOrThrow()
  }

suspend fun listAll(): List<UserModel> =
  suspendTransaction(db = database) {
    UserTable
      .selectAll()
      .map(UserTable::fromRow)
      .toList()
  }

suspend fun create(creator: UserModel.Creator): UserModel =
  suspendTransaction(db = database) {
    UserTable
      .insertReturning { statement ->
        statement[this.id] = UserId.random()
        statement[this.title] = creator.title
        statement[this.authors] = creator.authors
        statement[this.isbn] = creator.isbn
      }
      .map(UserModel::fromRow)
      .single()
  }
```

[Exposed's documentation](https://github.com/JetBrains/Exposed)
covers the DSL in more detail.

## Advanced configuration

This section documents all available configuration options.

### `ConnectionFactory`

The `ConnectionFactory` config specifies how to connect to your database.

- `url`: The R2DBC URL to connect to.
  For example, `r2dbc:postgresql://localhost:5432/...`
- `username`
- `password`
- `connectTimeout`: The timeout for connecting to the database.
  Defaults to 4 seconds.

Further Exposed connection factory configuration is available
by passing a `configureHikari` builder block to the `SqlFeature` constructor.

### `ConnectionPool`

The `ConnectionPool` config specifies how connections are managed.

- `size.min`: The minimum size of the connection pool.
  Defaults to 5.
- `size.max`: The maximum size of the connection pool.
  Defaults to 25.

Further Exposed connection pool configuration is available
by passing a `configureHikari` builder block to the `SqlFeature` constructor.

### `Database`

The `Database` config offers some further options regarding how the database is accessed.

- `readOnly`: Whether connections to the database should be read-only.
  Defaults to `false`.
- `maxAttempts`: How many times to try each transaction.
  Defaults to 3.

Further Exposed database configuration is available
by passing a `configureDatabase` builder block to the `SqlFeature` constructor.
