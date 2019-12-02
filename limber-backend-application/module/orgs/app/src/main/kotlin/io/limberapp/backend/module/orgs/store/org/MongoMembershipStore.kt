package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoIndex
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoMembershipStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : MembershipStore, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.collectionName,
        clazz = OrgEntity::class
    ),
    indices = listOf<MongoIndex<OrgEntity>> {
        ensureIndex(ascending(OrgEntity::members / MembershipEntity::userId), unique = false)
    }
) {

    override fun create(orgId: UUID, entity: MembershipEntity): Unit? {
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq orgId, OrgEntity::members / MembershipEntity::userId ne entity.userId),
            update = push(OrgEntity::members, entity)
        ) ?: return null
        return Unit
    }

    override fun delete(orgId: UUID, memberId: UUID): Unit? {
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq orgId, OrgEntity::members / MembershipEntity::userId eq memberId),
            update = pullByFilter(OrgEntity::members, MembershipEntity::userId eq memberId)
        ) ?: return null
        return Unit
    }
}
