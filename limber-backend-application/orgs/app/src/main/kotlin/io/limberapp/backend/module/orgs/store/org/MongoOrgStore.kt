package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.framework.store.MongoStore
import io.limberapp.framework.store.findOneByIdAndUpdate
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgEntity.Complete, OrgEntity.Update>(
    collection = mongoDatabase.getCollection(
        OrgEntity.collectionName,
        OrgEntity.Complete::class.java
    )
) {

    override fun getByMemberId(memberId: UUID): List<OrgEntity.Complete> {
        return collection.find(OrgEntity.Complete::members / MembershipEntity.Complete::userId eq memberId)
            .toList()
    }

    override fun createMembership(id: UUID, entity: MembershipEntity.Complete) {
        collection.findOneByIdAndUpdate(id, push(OrgEntity.Complete::members, entity))
    }

    override fun deleteMembership(id: UUID, memberId: UUID) {
        collection.findOneByIdAndUpdate(id, pullByFilter(OrgEntity.Complete::members, MembershipEntity.Complete::userId eq memberId))
    }
}
