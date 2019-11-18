package io.limberapp.framework.store

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import io.ktor.features.NotFoundException
import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.UpdateEntity
import org.bson.conversions.Bson
import org.litote.kmongo.findOne
import org.litote.kmongo.findOneById
import org.litote.kmongo.util.KMongoUtil
import java.util.UUID
import kotlin.reflect.KClass

class MongoCollection<Complete : CompleteEntity>(
    mongoDatabase: MongoDatabase,
    collectionName: String,
    clazz: KClass<Complete>
) {

    private val delegate = mongoDatabase.getCollection(collectionName, clazz.java)

    fun insertOne(entity: Complete) {
        delegate.insertOne(entity)
    }

    fun findOneById(id: UUID): Complete? {
        return delegate.findOneById(id)
    }

    fun findOne(bson: Bson): Complete? {
        return delegate.findOne(bson)
    }

    fun find(bson: Bson): List<Complete> {
        return delegate.find(bson).toList()
    }

    fun findOneByIdAndUpdate(id: UUID, update: UpdateEntity): Complete {
        val bson = KMongoUtil.toBsonModifier(update, updateOnlyNotNullProperties = true)
        return findOneByIdAndUpdate(id, bson)
    }

    fun findOneByIdAndUpdate(id: UUID, update: Bson): Complete {
        val options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        return delegate.findOneAndUpdate(KMongoUtil.idFilterQuery(id), update, options)
            ?: throw NotFoundException()
    }

    fun findOneByIdAndDelete(id: UUID) {
        delegate.findOneAndDelete(KMongoUtil.idFilterQuery(id)) ?: throw NotFoundException()
    }
}
