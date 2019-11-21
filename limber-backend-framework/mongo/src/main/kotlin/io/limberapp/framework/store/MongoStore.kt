package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity

/**
 * MongoStore is an implementation of Store for MongoDB.
 */
abstract class MongoStore<Complete : CompleteEntity>(
    protected val collection: MongoCollection<Complete>,
    indices: List<MongoCollection<Complete>.() -> Unit>
) : Store<Complete> {

    init {
        indices.forEach { collection.it() }
    }
}
