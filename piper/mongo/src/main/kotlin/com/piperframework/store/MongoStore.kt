package com.piperframework.store

import com.piperframework.entity.CompleteEntity

typealias MongoIndex<T> = MongoCollection<T>.() -> Unit

/**
 * MongoStore is an implementation of Store for MongoDB.
 */
abstract class MongoStore<Complete : CompleteEntity>(
    protected val collection: MongoCollection<Complete>,
    indices: List<MongoIndex<Complete>>
) : com.piperframework.store.Store<Complete> {

    init {
        indices.forEach { collection.it() }
    }
}
