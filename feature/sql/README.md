# SQL Feature

Adding `SqlFeature` to a Server enables interaction with a SQL database.
This is currently intended for Postgres.

## Usage

To access the database, create a store for your model.

```kotlin
internal class CelebrityStore : SqlStore<CelebrityRep>(CelebrityRep::class) {
  fun create(model: CelebrityRep): CelebrityRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/celebrity/create.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun get(guid: UUID): CelebrityRep? =
    get(guid, forUpdate = false)

  private fun get(guid: UUID, forUpdate: Boolean): CelebrityRep? =
    handle { handle ->
      val query = handle.createQuery(rs("store/celebrity/get.sql"))
      query.define("lockingClause", if (forUpdate) "for no key update" else "")
      query.bind("guid", guid)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun update(guid: UUID, updater: Updater<CelebrityRep>): CelebrityRep =
    transaction { handle ->
      val model = updater(get(guid, forUpdate = true) ?: celebrityDoesNotExist())
      val query = handle.createQuery(rs("store/celebrity/update.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun delete(guid: UUID): CelebrityRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/celebrity/delete.sql"))
      query.bind("guid", guid)
      return@transaction query.mapToType().singleNullOrThrow() ?: celebrityDoesNotExist()
    }

  private fun celebrityDoesNotExist(): Nothing =
    throw UnprocessableException("Celebrity does not exist.")
}
```

Then add the required SQL statements referenced from your store.

```postgresql
insert into celebrity.celebrity (guid, name, age)
values (:guid, :name, :age)
returning *
```

```postgresql
select *
from celebrity.celebrity
where guid = :guid
<lockingClause>
```

```postgresql
update celebrity.celebrity
set name = :name,
    age = :age
where guid = :guid
returning *
```

```postgresql
delete
from celebrity.celebrity
where guid = :guid
returning *
```

## Implementation notes and limitations

[JDBI](https://jdbi.org/) is used as the underlying implementation.
It runs on top of [JDBC](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html).

Models should always be accessed and updated using some authorized identifier.
For example, the `OutfitRep` for a `CelebrityRep` might have its own GUID,
but if authorization was based on the celebrity's GUID,
outfit queries need to be conditioned as follows.
They cannot be conditioned only on the outfit's GUID.
```postgresql
where celebrity_guid = :celebrityGuid
  and guid = :guid
```
