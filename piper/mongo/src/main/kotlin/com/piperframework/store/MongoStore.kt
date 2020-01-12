package com.piperframework.store

import com.piperframework.entity.CompleteEntity

typealias MongoIndex<Complete> = MongoCollection<Complete>.() -> Unit

/**
 * MongoStore is an implementation of Store for MongoDB.
 */
abstract class MongoStore<Complete : CompleteEntity>(
    protected val collection: MongoCollection<Complete>,
    indices: List<MongoIndex<Complete>>
) : Store {

    constructor(collection: MongoCollection<Complete>, index: MongoIndex<Complete>) : this(collection, listOf(index))

    init {
        indices.forEach { collection.it() }
    }
}
