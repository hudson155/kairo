package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity
import java.util.UUID

/**
 * MongoStore is an implementation of CrStore for MongoDB.
 */
abstract class MongoCrStore<Complete : CompleteEntity>(
    collection: MongoCollection<Complete>,
    indices: List<MongoCollection<Complete>.() -> Unit>
) : CrStore<Complete>, MongoStore<Complete>(collection, indices) {

    final override fun create(entity: Complete) {
        collection.insertOne(entity)
    }

    final override fun get(id: UUID) = collection.findOneById(id)
}
