package io.limberapp.framework.mongo.collection

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import io.ktor.features.NotFoundException
import io.limberapp.framework.mongo.collection.findFilter.FindFilter
import io.limberapp.framework.mongo.collection.update.Update
import io.limberapp.framework.store.LimberMongoObjectMapper
import io.limberapp.framework.store.findOne
import io.limberapp.framework.util.asByteArray
import org.bson.BsonBinarySubType
import org.bson.Document
import org.bson.types.Binary
import java.util.UUID

private const val MONGO_ID_KEY = "_id"

/**
 * TODO: Get rid of all explicit strings
 */
class MongoStoreCollection(mongoDatabase: MongoDatabase, collectionName: String) {

    private val objectMapper = LimberMongoObjectMapper()

    private val delegate: MongoCollection<Document> = mongoDatabase.getCollection(collectionName)

    fun insertOne(any: Any): Document {
        val document = Document.parse(objectMapper.writeValueAsString(any))
        delegate.insertOne(document)
        return document
    }

    fun findOne(id: UUID): Document? {
        return findOne(idFilter(id))
    }

    fun findOne(filter: FindFilter): Document? {
        val filterBson = filter.asBson()
        return delegate.findOne(filterBson)
    }

    fun findMany(filter: FindFilter): List<Document> {
        val filterBson = filter.asBson()
        return delegate.find(filterBson).toList()
    }

    fun findOneAndUpdate(id: UUID, update: Update): Document {
        return findOneAndUpdate(idFilter(id), update)
    }

    fun findOneAndUpdate(filter: FindFilter, update: Update): Document {
        val filterBson = filter.asBson()
        val updateBson = update.asBson()
        val options = FindOneAndUpdateOptions().apply { returnDocument(ReturnDocument.AFTER) }
        return delegate.findOneAndUpdate(filterBson, updateBson, options)
            ?: throw NotFoundException()
    }

    private fun idFilter(id: UUID): FindFilter {
        val binary = Binary(BsonBinarySubType.UUID_LEGACY, id.asByteArray())
        return FindFilter().apply {
            eq(MONGO_ID_KEY, binary)
        }
    }
}
