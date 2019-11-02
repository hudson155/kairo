package io.limberapp.framework.store

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.convertValue
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import io.ktor.features.NotFoundException
import io.limberapp.framework.model.CompleteModel
import io.limberapp.framework.model.CreationModel
import io.limberapp.framework.model.UpdateModel
import io.limberapp.framework.util.asByteArray
import org.bson.BsonBinarySubType
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.Binary
import java.util.UUID

/**
 * MongoStore is an implementation of Store for MongoDB. It implements some default methods.
 *
 * TODO: This class uses Jackson to go to String and then to Document. Could we save time by using a
 * TODO: stream, JsonNode, or some other intermediary structure?
 */
abstract class MongoStore<Creation : CreationModel, Complete : CompleteModel, Update : UpdateModel>(
    mongoDatabase: MongoDatabase,
    collectionName: String
) : Store<Creation, Complete, Update> {

    protected val collection: MongoCollection<Document> =
        mongoDatabase.getCollection(collectionName)

    protected val objectMapper = LimberMongoObjectMapper()

    final override fun create(model: Creation, typeRef: TypeReference<Complete>): Complete {
        val json = objectMapper.writeValueAsString(model)
        collection.insertOne(Document.parse(json))
        return objectMapper.readValue(json, typeRef)
    }

    final override fun getById(id: UUID, typeRef: TypeReference<Complete>): Complete? {
        val document = collection.findOne(idFilter(id)) ?: return null
        return objectMapper.readValue(document.toJson(), typeRef)
    }

    final override fun update(
        id: UUID,
        model: Update,
        typeRef: TypeReference<Complete>
    ): Complete {
        val map = objectMapper.convertValue<Map<String, Any?>>(model).filterValues { it != null }
        if (map.isEmpty()) return getById(id, typeRef)!!
        val json = objectMapper.writeValueAsString(map)
        val update = Document(
            mapOf(
                "\$set" to Document.parse(json),
                "\$inc" to Document(CompleteModel::version.name, 1)
            )
        )
        val options = FindOneAndUpdateOptions().apply { returnDocument(ReturnDocument.AFTER) }
        val document = collection.findOneAndUpdate(idFilter(id), update, options) ?: throw NotFoundException()
        return objectMapper.readValue(document.toJson(), typeRef)
    }

    private fun idFilter(id: UUID): Bson {
        val binary = Binary(BsonBinarySubType.UUID_LEGACY, id.asByteArray())
        return Filters.eq(binary)
    }
}
