package io.limberapp.backend.module.orgs.store.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.framework.mongo.collection.FindFilter
import io.limberapp.framework.mongo.collection.MongoStoreCollection
import io.limberapp.framework.mongo.collection.Update
import io.limberapp.framework.mongo.collection.idFilter
import io.limberapp.framework.store.MongoStore
import org.bson.Document
import java.util.UUID

private val ORG_MEMBER_KEY =
    "${OrgEntity.Complete::members.name}.${MembershipEntity.Complete::userId.name}"

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgEntity.Creation, OrgEntity.Complete, OrgEntity.Update>(
    collection = MongoStoreCollection(mongoDatabase, collectionName)
) {

    override fun getByMemberId(memberId: UUID): List<OrgEntity.Complete> {
        val findFilter = FindFilter().apply { eq[ORG_MEMBER_KEY] = memberId }
        val documents = collection.findMany(findFilter)
        return documents.map { objectMapper.readValue<OrgEntity.Complete>(it.toJson()) }
    }

    override fun createMembership(id: UUID, entity: MembershipEntity.Creation) {
        val update = Update()
            .apply { push[OrgEntity.Complete::members.name] = entity }
        collection.findOneAndUpdate(id, update)
    }

    override fun deleteMembership(id: UUID, memberId: UUID) {
        val update = Update().apply {
            pull[OrgEntity.Complete::members.name] =
                Document(MembershipEntity.Complete::userId.name, memberId)
        }
        collection.findOneAndUpdate(idFilter(id).apply { eq[ORG_MEMBER_KEY] = memberId }, update)
    }

    companion object {
        const val collectionName = "Org"
    }
}
