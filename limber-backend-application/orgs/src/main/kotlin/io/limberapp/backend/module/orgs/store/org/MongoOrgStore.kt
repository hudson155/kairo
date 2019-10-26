package io.limberapp.backend.module.orgs.store.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.framework.store.MongoStore
import org.bson.Document
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgModel.Complete, OrgModel.Update>(
    mongoDatabase,
    "Org"
) {

    override fun getByMemberId(memberId: UUID): List<OrgModel.Complete> {
        val filter = Filters.eq("memberships", Document("userId", memberId))
        val documents = collection.find(filter).toList()
        return documents.map { objectMapper.readValue(it.toJson()) }
    }
}
