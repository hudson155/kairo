# SQL Feature

Adding `SqlFeature` to a Server enables interaction with a SQL database.
This is currently intended for Postgres.

## Usage

To access the database, create a store for your model.

```kotlin
internal class CelebrityStore : SqlStore<CelebrityRep>(
  tableName = "pop_culture.celebrity",
  type = CelebrityRep::class,
) {
  fun create(celebrity: CelebrityRep): CelebrityRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/celebrity/create.sql"))
      query.bindKotlin(celebrity)
      return@transaction query.mapToType().single()
    }

  fun update(guid: UUID, updater: Updater<CelebrityRep.Update>): CelebrityRep =
    transaction { handle ->
      val celebrity = get(guid, forUpdate = true) ?: throw CelebrityDoesNotExist()
      val query = handle.createQuery(rs("store/celebrity/update.sql"))
      query.bind("guid", guid)
      query.bindKotlin(update(CelebrityRep.Update(celebrity)))
      return@transaction query.mapToType().single()
    }

  fun delete(guid: UUID): CelebrityRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/celebrity/delete.sql"))
      query.bind("guid", guid)
      return@transaction query.mapToType().singleNullOrThrow() ?: throw CelebrityDoesNotExist()()
    }
}
```

Then add the required SQL statements referenced from your store.

```postgresql
insert into celebrity.celebrity (guid, name, age)
values (:guid, :name, :age)
returning *
```

```postgresql
update celebrity.celebrity
set name = :name,
    age  = :age
where guid = :guid
returning *
```

```postgresql
delete
from celebrity.celebrity
where guid = :guid
returning *
```

## Implementation notes

[JDBI](https://jdbi.org/) is used as the underlying implementation.
It runs on top of [JDBC](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html).
