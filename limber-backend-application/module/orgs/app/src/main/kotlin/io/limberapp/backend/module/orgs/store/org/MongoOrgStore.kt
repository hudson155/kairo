package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoIndex
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.orgs.entity.org.FeatureEntity
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

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.collectionName,
        clazz = OrgEntity::class
    ),
    indices = listOf<MongoIndex<OrgEntity>>(
        { ensureIndex(ascending(OrgEntity::members / MembershipEntity::userId), unique = false) },
        { ensureIndex(ascending(OrgEntity::features / FeatureEntity::id), unique = true) }
    )
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
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq id, OrgEntity::members / MembershipEntity::userId ne entity.userId),
            update = push(OrgEntity::members, entity)
        ) ?: return null
        return Unit
    }

    override fun deleteMembership(id: UUID, memberId: UUID): Unit? {
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq id, OrgEntity::members / MembershipEntity::userId eq memberId),
            update = pullByFilter(OrgEntity::members, MembershipEntity::userId eq memberId)
        ) ?: return null
        return Unit
    }

    override fun createFeature(id: UUID, entity: FeatureEntity): Unit? {
        collection.findOneAndUpdate(
            filter = and(
                OrgEntity::id eq id,
                OrgEntity::features / FeatureEntity::id ne entity.id,
                OrgEntity::features / FeatureEntity::name ne entity.name,
                OrgEntity::features / FeatureEntity::path ne entity.path
            ),
            update = push(OrgEntity::features, entity)
        ) ?: return null
        return Unit
    }

    override fun deleteFeature(id: UUID, featureId: UUID): Unit? {
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq id, OrgEntity::features / FeatureEntity::id eq featureId),
            update = pullByFilter(OrgEntity::features, FeatureEntity::id eq featureId)
        ) ?: return null
        return Unit
    }

    override fun delete(id: UUID) = collection.findOneByIdAndDelete(id)
}
