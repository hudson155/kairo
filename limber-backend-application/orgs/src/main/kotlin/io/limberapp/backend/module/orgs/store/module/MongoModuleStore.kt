package io.limberapp.backend.module.orgs.store.module

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import io.limberapp.backend.module.orgs.model.module.ModuleModel
import io.limberapp.framework.store.MongoStore
import java.util.UUID

internal class MongoModuleStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : ModuleStore, MongoStore<ModuleModel.Complete, ModuleModel.Update>(
    mongoDatabase = mongoDatabase,
    collectionName = collectionName
) {

    override fun getByOrgId(orgId: UUID): List<ModuleModel.Complete> {
        val filter = Filters.eq("orgId", orgId)
        val documents = collection.find(filter).toList()
        return documents.map { objectMapper.readValue<ModuleModel.Complete>(it.toJson()) }
    }

    companion object {
        const val collectionName = "Module"
    }
}
