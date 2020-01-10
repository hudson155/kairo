package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.orgs.entity.org.FeatureEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.backend.module.orgs.exception.conflict.FeatureIsNotUnique
import io.limberapp.backend.module.orgs.exception.notFound.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import org.bson.conversions.Bson
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.colProperty
import org.litote.kmongo.combine
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import org.litote.kmongo.setValue
import java.util.UUID

internal class MongoFeatureStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    private val orgStore: OrgStore
) : FeatureStore, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.name,
        clazz = OrgEntity::class
    ),
    index = { ensureIndex(ascending(OrgEntity::features / FeatureEntity::id), unique = true) }
) {

    override fun create(orgId: UUID, entity: FeatureEntity) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        collection.findOneAndUpdate(
            filter = and(
                OrgEntity::id eq orgId,
                OrgEntity::features / FeatureEntity::path ne entity.path
            ),
            update = push(OrgEntity::features, entity)
        ) ?: throw FeatureIsNotUnique()
    }

    override fun get(orgId: UUID, featureId: UUID): FeatureEntity? {
        val org = orgStore.get(orgId) ?: throw OrgNotFound()
        return org.features.singleOrNull { it.id == featureId }
    }

    override fun update(orgId: UUID, featureId: UUID, update: FeatureEntity.Update): FeatureEntity {
        get(orgId, featureId) ?: throw FeatureNotFound()
        val org = collection.findOneAndUpdate(
            filter = and(
                mutableListOf(
                    OrgEntity::id eq orgId,
                    OrgEntity::features / FeatureEntity::id eq featureId
                ).apply {
                    update.path?.let { add(OrgEntity::features / FeatureEntity::path ne it) }
                }
            ),
            update = combine(
                mutableListOf<Bson>().apply {
                    update.name?.let { add(setValue(OrgEntity::features.colProperty.posOp / FeatureEntity::name, it)) }
                    update.path?.let { add(setValue(OrgEntity::features.colProperty.posOp / FeatureEntity::path, it)) }
                }
            )
        ) ?: throw FeatureIsNotUnique()
        return org.features.single { it.id == featureId }
    }

    override fun delete(orgId: UUID, featureId: UUID) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq orgId, OrgEntity::features / FeatureEntity::id eq featureId),
            update = pullByFilter(OrgEntity::features, FeatureEntity::id eq featureId)
        ) ?: throw FeatureNotFound()
    }
}
