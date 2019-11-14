package io.limberapp.framework.store

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.convertValue
import io.ktor.features.NotFoundException
import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.CreationEntity
import io.limberapp.framework.entity.UpdateEntity
import io.limberapp.framework.mongo.collection.MongoStoreCollection
import io.limberapp.framework.mongo.collection.Update
import java.util.UUID

/**
 * MongoStore is an implementation of Store for MongoDB. It implements some default methods.
 *
 * TODO: This class uses Jackson to go to String and then to Document. Could we save time by using a
 *  stream, JsonNode, or some other intermediary structure?
 */
abstract class MongoStore<Creation : CreationEntity, Complete : CompleteEntity, Update : UpdateEntity>(
    protected val collection: MongoStoreCollection
) : Store<Creation, Complete, Update> {

    protected val objectMapper = LimberMongoObjectMapper()

    final override fun create(entity: Creation, typeRef: TypeReference<Complete>): Complete {
        val document = collection.insertOne(entity)
        return objectMapper.readValue(document.toJson(), typeRef)
    }

    final override fun get(id: UUID, typeRef: TypeReference<Complete>): Complete? {
        val document = collection.findOne(id) ?: return null
        return objectMapper.readValue(document.toJson(), typeRef)
    }

    final override fun update(
        id: UUID,
        entity: Update,
        typeRef: TypeReference<Complete>
    ): Complete {
        val map = objectMapper.convertValue<Map<String, Any?>>(entity).filterValuesNotNull()
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
