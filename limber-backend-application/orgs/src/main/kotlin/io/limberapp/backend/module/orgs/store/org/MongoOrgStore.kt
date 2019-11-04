package io.limberapp.backend.module.orgs.store.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.framework.mongo.collection.MongoStoreCollection
import io.limberapp.framework.mongo.collection.FindFilter
import io.limberapp.framework.mongo.collection.Update
import io.limberapp.framework.store.MongoStore
import org.bson.Document
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgModel.Creation, OrgModel.Complete, OrgModel.Update>(
    collection = MongoStoreCollection(mongoDatabase, collectionName)
) {

    override fun getByMemberId(memberId: UUID): List<OrgModel.Complete> {
        val findFilter = FindFilter().apply {
            eq(
                key = OrgModel.Complete::members.name,
                value = Document(MembershipModel.Complete::userId.name, memberId)
            )
        }
        val documents = collection.findMany(findFilter)
        return documents.map { objectMapper.readValue<OrgModel.Complete>(it.toJson()) }
    }

    override fun createMembership(id: UUID, model: MembershipModel.Creation) {
        val update = Update()
            .apply { push[OrgModel.Complete::members.name] = model }
        collection.findOneAndUpdate(id, update)
    }

    override fun deleteMembership(id: UUID, memberId: UUID) {
        val update = Update().apply {
            pull[OrgModel.Complete::members.name] =
                Document(MembershipModel.Complete::userId.name, memberId)
        }
        collection.findOneAndUpdate(id, update)
    }

    companion object {
        const val collectionName = "Org"
    }
}
