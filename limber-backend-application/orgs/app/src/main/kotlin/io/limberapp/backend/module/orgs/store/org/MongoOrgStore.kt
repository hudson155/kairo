package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.framework.store.MongoCollection
import io.limberapp.framework.store.MongoStore
import org.litote.kmongo.and
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgEntity.Complete, OrgEntity.Update>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.collectionName,
        clazz = OrgEntity.Complete::class
    )
) {

    override fun getByMemberId(memberId: UUID) =
        collection.find(OrgEntity.Complete::members / MembershipEntity.Complete::userId eq memberId)

    override fun createMembership(id: UUID, entity: MembershipEntity.Complete) {
        collection.findOneByIdAndUpdate(id, push(OrgEntity.Complete::members, entity))
    }

    override fun deleteMembership(id: UUID, memberId: UUID) {
        collection.findOneAndUpdate(
            filter = and(
                OrgEntity.Complete::id eq id,
                OrgEntity.Complete::members / MembershipEntity.Complete::userId eq memberId
            ),
            update = pullByFilter(
                property = OrgEntity.Complete::members,
                filter = MembershipEntity.Complete::userId eq memberId
            )
        )
    }
}
