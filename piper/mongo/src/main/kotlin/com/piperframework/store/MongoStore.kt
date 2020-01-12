package com.piperframework.store

typealias MongoIndex<Complete> = MongoCollection<Complete>.() -> Unit

/**
 * MongoStore is an implementation of Store for MongoDB.
 */
abstract class MongoStore<Complete : Any>(
    protected val collection: MongoCollection<Complete>,
    indices: List<MongoIndex<Complete>>
) : Store {

    constructor(collection: MongoCollection<Complete>, index: MongoIndex<Complete>) : this(collection, listOf(index))

    init {
        indices.forEach { collection.it() }
    }
}
