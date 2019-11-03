package io.limberapp.framework.store

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.convertValue
import com.mongodb.client.model.Filters
import io.ktor.features.NotFoundException
import io.limberapp.framework.model.CompleteModel
import io.limberapp.framework.model.CreationModel
import io.limberapp.framework.model.UpdateModel
import io.limberapp.framework.mongo.collection.MongoStoreCollection
import io.limberapp.framework.mongo.collection.update.Update
import io.limberapp.framework.util.asByteArray
import org.bson.BsonBinarySubType
import org.bson.conversions.Bson
import org.bson.types.Binary
import java.util.UUID

/**
 * MongoStore is an implementation of Store for MongoDB. It implements some default methods.
 *
 * TODO: This class uses Jackson to go to String and then to Document. Could we save time by using a
 *  stream, JsonNode, or some other intermediary structure?
 */
abstract class MongoStore<Creation : CreationModel, Complete : CompleteModel, Update : UpdateModel>(
    protected val collection: MongoStoreCollection
) : Store<Creation, Complete, Update> {

    protected val objectMapper = LimberMongoObjectMapper()

    final override fun create(model: Creation, typeRef: TypeReference<Complete>): Complete {
        val document = collection.insertOne(model)
        return objectMapper.readValue(document.toJson(), typeRef)
    }

    final override fun get(id: UUID, typeRef: TypeReference<Complete>): Complete? {
        val document = collection.findOne(id) ?: return null
        return objectMapper.readValue(document.toJson(), typeRef)
    }

    final override fun update(id: UUID, model: Update, typeRef: TypeReference<Complete>): Complete {
        val map = objectMapper.convertValue<Map<String, Any?>>(model).filterValuesNotNull()
        if (map.isEmpty()) return get(id, typeRef) ?: throw NotFoundException()
        val document = collection.findOneAndUpdate(id, Update().apply { set += map })
        return objectMapper.readValue(document.toJson(), typeRef)
    }

    final override fun delete(id: UUID) {
        val update = Update()
            .apply { set["deleted"] = true }
        collection.findOneAndUpdate(id, update)
    }
}

private fun <K, V : Any> Map<K, V?>.filterValuesNotNull(): Map<K, V> =
    filterValues { it != null }.mapValues { it.value!! }
