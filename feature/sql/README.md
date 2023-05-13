# SQL Feature

Adding `SqlFeature` to a Server enables interaction with a SQL database.
This is currently intended for Postgres.

## Usage

To access the database, create a store for your model.

```kotlin
internal class CelebrityStore : SqlStore<CelebrityModel>(
  tableName = "pop_culture.celebrity",
  type = CelebrityModel::class,
) {
  fun create(creator: CelebrityModel.Creator): CelebrityModel =
    transaction { handle ->
      val query = handle.createQuery(rs("store/celebrity/create.sql"))
      query.bindKotlin(creator)
      return@transaction query.mapToType().single()
    }

  fun update(guid: UUID, updater: Updater<CelebrityModel.Update>): CelebrityModel =
    transaction { handle ->
      val celebrity = get(guid, forUpdate = true) ?: throw CelebrityDoesNotExist()
      val query = handle.createQuery(rs("store/celebrity/update.sql"))
      query.bind("guid", guid)
      query.bindKotlin(updater(CelebrityModel.Update(celebrity)))
      return@transaction query.mapToType().single()
    }

  fun delete(guid: UUID): CelebrityModel =
    transaction { handle ->
      val query = handle.createQuery(rs("store/celebrity/delete.sql"))
      query.bind("guid", guid)
      return@transaction query.mapToType().singleNullOrThrow() ?: throw CelebrityDoesNotExist()
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
