package com.piperframework.store

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Collation
import com.mongodb.client.model.CollationStrength
import com.mongodb.client.model.FindOneAndDeleteOptions
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.ReturnDocument
import com.piperframework.entity.CompleteEntity
import org.bson.conversions.Bson
import org.litote.kmongo.ensureIndex
import org.litote.kmongo.findOne
import org.litote.kmongo.findOneById
import org.litote.kmongo.util.KMongoUtil
import java.util.Locale
import java.util.UUID
import kotlin.reflect.KClass

class MongoCollection<Complete : CompleteEntity>(
    mongoDatabase: MongoDatabase,
    collectionName: String,
    clazz: KClass<Complete>
) {

    private val delegate = mongoDatabase.getCollection(collectionName, clazz.java)

    private val collation = Collation.builder()
        .locale(Locale.ENGLISH.language)
        .collationStrength(CollationStrength.SECONDARY)
        .build()

    private fun findOneAndUpdateOptions() = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)

    private fun findOneAndDeleteOptions() = FindOneAndDeleteOptions()

    fun ensureIndex(index: Bson, unique: Boolean) {
        delegate.ensureIndex(index, IndexOptions().unique(unique).collation(collation).background(false))
    }

    fun insertOne(entity: Complete) {
        delegate.insertOne(entity)
    }

    fun findOneById(id: UUID): Complete? =
        delegate.findOneById(id)

    fun findOne(filter: Bson): Complete? =
        delegate.findOne(filter)

    fun find(filter: Bson): List<Complete> =
        delegate.find(filter).toList()

    fun findOneByIdAndUpdate(id: UUID, update: Bson, arrayFilters: List<Bson>? = null): Complete? =
        findOneAndUpdate(id(id), update, arrayFilters)

    fun findOneAndUpdate(filter: Bson, update: Bson, arrayFilters: List<Bson>? = null): Complete? =
        delegate.findOneAndUpdate(filter, update, findOneAndUpdateOptions().arrayFilters(arrayFilters))

    fun findOneByIdAndDelete(id: UUID): Unit? =
        findOneAndDelete(id(id))

    fun findOneAndDelete(filter: Bson): Unit? =
        delegate.findOneAndDelete(filter, findOneAndDeleteOptions())?.let { Unit }

    private fun id(id: UUID) = KMongoUtil.idFilterQuery(id)
}
