package io.limberapp.framework.store

import com.fasterxml.jackson.core.type.TypeReference
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import io.limberapp.framework.model.Model
import io.limberapp.framework.util.asByteArray
import org.bson.BsonBinarySubType
import org.bson.Document
import org.bson.types.Binary
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

    protected val collection: MongoCollection<Document> =
        mongoDatabase.getCollection(collectionName)

    private val objectMapper = LimberMongoObjectMapper()

    /*
     * These helper methods assist mapping from Model -> Json -> Document (when persisting)
     *                                     from Document -> Json -> Model (when accessing)
     */

    protected fun toJson(model: M): String {
        check(model.id != null) // Sanity check
        return objectMapper.writeValueAsString(model)
    }

    protected fun fromJson(json: String, typeRef: TypeReference<M>): M {
        val model = objectMapper.readValue<M>(json, typeRef)
        check(model.id != null) // Sanity check
        return model
    }

    protected fun toDocument(json: String): Document {
        val document = Document.parse(json)
        check(document[MONGO_ID] != null) // Sanity check
        return document
    }

    protected fun fromDocument(document: Document): String {
        check(document[MONGO_ID] != null) // Sanity check
        return document.toJson()
    }

    /*
     * Implementations for Store methods
     */

    final override fun create(model: M, typeRef: TypeReference<M>): M {
        val id = UUID.randomUUID()
        val json = toJson(model.withId(id))
        collection.insertOne(toDocument(json))
        // It's important to reverse-parse the JSON here because there can be data loss when mapping
        // to JSON. For example, SimpleDateTimes natively have nanosecond precision, but when mapped
        // to JSON they only have millisecond precision.
        return fromJson(json, typeRef)
    }

    final override fun getById(id: UUID, typeRef: TypeReference<M>): M? {
        val binary = Binary(BsonBinarySubType.UUID_LEGACY, id.asByteArray())
        val document = collection.findOne(Filters.eq(binary)) ?: return null
        return fromJson(fromDocument(document), typeRef)
    }
}
