package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.UpdateEntity
import java.util.UUID

/**
 * MongoStore is an implementation of CrudStore for MongoDB.
 */
abstract class MongoCrudStore<Complete : CompleteEntity, Update : UpdateEntity>(
    collection: MongoCollection<Complete>,
    indices: List<MongoCollection<Complete>.() -> Unit>
) : CrudStore<Complete, Update>, MongoCrStore<Complete>(collection, indices) {

    final override fun update(id: UUID, update: Update) =
        collection.findOneByIdAndUpdate(id, update)

    final override fun delete(id: UUID) = collection.findOneByIdAndDelete(id)
}
