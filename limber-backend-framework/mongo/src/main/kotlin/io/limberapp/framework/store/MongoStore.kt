package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity

typealias MongoIndex<T> = MongoCollection<T>.() -> Unit

/**
 * MongoStore is an implementation of Store for MongoDB.
 */
abstract class MongoStore<Complete : CompleteEntity>(
    protected val collection: MongoCollection<Complete>,
    indices: List<MongoIndex<Complete>>
) : Store<Complete> {

    init {
        indices.forEach { collection.it() }
    }
}
