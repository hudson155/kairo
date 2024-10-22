# `kairo-sql-migration-feature`

**This Feature is used by the [kairo-sample](https://github.com/hudson155/kairo-sample) repository.**

The SQL Migration Feature complements [kairo-sql-feature](../kairo-sql-feature)
by adding support for database migrations.
Under the hood, this Feature uses [Flyway Community](https://www.red-gate.com/products/flyway/community/).

At the current time, this only supports Postgres.
However, it would not be a huge lift to support other SQL databases.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-sql-migration-feature:$kairoVersion")
}
```

### Step 2: Write some migrations

```postgresql
-- src/main/resources/db/monolith/migration/common

create table library.library_book
(
    id text -- KairoIds are stored as text.
        constraint pkey__library_book
            primary key,

    title text not null,
    author text,
    isbn text not null
);

-- Note: This table is missing indexes.
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoSqlMigrationFeature(config.sqlMigration)
```

### Step 3: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

sqlMigration:
  run: true
  cleanOnValidationError: false # Never enable this in production.
  locations:
    - "db/monolith/migration/common"
  defaultSchema: "public"
  schemas:
    - "library"
  createSchemas: true
  tableName: "database_migration"
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoSqlMigrationFeature(config.sqlMigration)
```
