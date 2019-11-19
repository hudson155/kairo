package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.UpdateEntity
import java.util.UUID

/**
 * MongoStore is an implementation of Store for MongoDB. It implements some default methods.
 */
abstract class MongoStore<Complete : CompleteEntity, Update : UpdateEntity>(
    protected val collection: MongoCollection<Complete>
) : Store<Complete, Update> {

    final override fun create(entity: Complete) {
        collection.insertOne(entity)
    }

    final override fun get(id: UUID) = collection.findOneById(id)

    final override fun update(id: UUID, update: Update) =
        collection.findOneByIdAndUpdate(id, update)

    final override fun delete(id: UUID) = collection.findOneByIdAndDelete(id)
}
