package io.limberapp.framework.store

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.convertValue
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import io.limberapp.framework.model.Model
import io.limberapp.framework.model.Updater
import io.limberapp.framework.util.asByteArray
import org.bson.BsonBinarySubType
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.Binary
import java.time.LocalDateTime
import java.util.UUID

const val MONGO_ID = "_id"

/**
 * MongoStore is an implementation of Store for MongoDB. It implements some default methods.
 *
 * TODO: This class uses Jackson to go to String and then to Document. Could we save time by using a
 * TODO: stream, JsonNode, or some other intermediary structure?
 */
abstract class MongoStore<M : Model<M>>(
    mongoDatabase: MongoDatabase,
    collectionName: String
) : Store<M> {

    private val collection: MongoCollection<Document> =
        mongoDatabase.getCollection(collectionName)

    private val objectMapper = LimberMongoObjectMapper()

    /*
     * Implementations for Store methods
     */

    final override fun create(model: M, typeRef: TypeReference<M>): M {
        val id = UUID.randomUUID()
        val completeModel = model.complete(id, LocalDateTime.now())
        val map = objectMapper.convertValue<Map<String, Any?>>(completeModel)
        val json = objectMapper.writeValueAsString(map)
        collection.insertOne(Document.parse(json))
        // It's important to reverse-parse the JSON here because there can be data loss when mapping
        // to JSON. For example, SimpleDateTimes natively have nanosecond precision, but when mapped
        // to JSON they only have millisecond precision.
        return modelFromJson(json, typeRef)
    }

    final override fun getById(id: UUID, typeRef: TypeReference<M>): M? {
        val document = collection.findOne(idFilter(id)) ?: return null
        return modelFromJson(document.toJson(), typeRef)
    }

    final override fun update(id: UUID, updater: Updater<M>, typeRef: TypeReference<M>): M {
        val map = objectMapper.convertValue<Map<String, Any?>>(updater).filterValues { it != null }
        if (map.isEmpty()) return getById(id, typeRef)!!
        val json = objectMapper.writeValueAsString(map)
        val filter = Filters.and(idFilter(id))
        val update = Document(
            mapOf(
                "\$set" to Document.parse(json),
                "\$inc" to Document("version", 1)
            )
        )
        val options = FindOneAndUpdateOptions().apply { returnDocument(ReturnDocument.AFTER) }
        val document = collection.findOneAndUpdate(filter, update, options)!!
        return modelFromJson(document.toJson(), typeRef)
    }

    private fun idFilter(id: UUID): Bson {
        val binary = Binary(BsonBinarySubType.UUID_LEGACY, id.asByteArray())
        return Filters.eq(binary)
    }

    private fun modelFromJson(json: String, typeRef: TypeReference<M>): M {
        val model = objectMapper.readValue<M>(json, typeRef)
        check(model.modelState == Model.ModelState.COMPLETE) // Sanity check
        return model
    }
}
