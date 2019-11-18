package io.limberapp.framework.store

import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.UpdateEntity
import org.litote.kmongo.util.UpdateConfiguration
import java.util.UUID

/**
 * MongoStore is an implementation of Store for MongoDB. It implements some default methods.
 *
 * TODO: This class uses Jackson to go to String and then to Document. Could we save time by using a
 *  stream, JsonNode, or some other intermediary structure?
 */
abstract class MongoStore<Complete : CompleteEntity, Update : UpdateEntity>(
    protected val collection: MongoCollection<Complete>
) : Store<Complete, Update> {

    final override fun create(entity: Complete) {
        collection.insertOne(entity)
    }

    final override fun get(id: UUID): Complete? {
        return collection.findOneById(id)
    }

    final override fun update(id: UUID, update: Update): Complete {
        return collection.findOneByIdAndUpdate(id, update)
    }

    final override fun delete(id: UUID) {
        collection.findOneByIdAndDelete(id)
    }

    companion object {
        init {
            UpdateConfiguration.updateOnlyNotNullProperties = true
        }
    }
}
