package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.framework.store.MongoCollection
import io.limberapp.framework.store.MongoStore
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.collectionName,
        clazz = OrgEntity::class
    ),
    indices = listOf<MongoCollection<OrgEntity>.() -> Unit> {
        ensureIndex(ascending(OrgEntity::members / MembershipEntity::userId), unique = false)
    }
) {

    override fun create(entity: OrgEntity) {
        collection.insertOne(entity)
    }

    override fun get(id: UUID) = collection.findOneById(id)

    override fun getByMemberId(memberId: UUID) =
        collection.find(OrgEntity::members / MembershipEntity::userId eq memberId)

    override fun update(id: UUID, update: OrgEntity.Update) =
        collection.findOneByIdAndUpdate(id, update)

    override fun createMembership(id: UUID, entity: MembershipEntity): Unit? {
        return collection.findOneAndUpdate(
            filter = and(
                OrgEntity::id eq id,
                OrgEntity::members / MembershipEntity::userId ne entity.userId
            ),
            update = push(OrgEntity::members, entity)
        )?.let { }
    }

    override fun deleteMembership(id: UUID, memberId: UUID): Unit? {
        return collection.findOneAndUpdate(
            filter = and(
                OrgEntity::id eq id,
                OrgEntity::members / MembershipEntity::userId eq memberId
            ),
            update = pullByFilter(OrgEntity::members, MembershipEntity::userId eq memberId)
        )?.let { }
    }

    override fun delete(id: UUID) = collection.findOneByIdAndDelete(id)
}
