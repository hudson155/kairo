package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoIndex
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.orgs.entity.org.FeatureEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
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
    mongoDatabase: MongoDatabase
) : FeatureStore, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.collectionName,
        clazz = OrgEntity::class
    ),
    indices = listOf<MongoIndex<OrgEntity>> {
        ensureIndex(ascending(OrgEntity::features / FeatureEntity::id), unique = true)
    }
) {

    override fun create(orgId: UUID, entity: FeatureEntity): Unit? {
        collection.findOneAndUpdate(
            filter = and(
                OrgEntity::id eq orgId,
                OrgEntity::features / FeatureEntity::id ne entity.id,
                OrgEntity::features / FeatureEntity::name ne entity.name,
                OrgEntity::features / FeatureEntity::path ne entity.path
            ),
            update = push(OrgEntity::features, entity)
        ) ?: return null
        return Unit
    }

    override fun update(orgId: UUID, id: UUID, update: FeatureEntity.Update): FeatureEntity? {
        val result = collection.findOneAndUpdate(
            filter = and(
                mutableListOf(
                    OrgEntity::id eq orgId,
                    OrgEntity::features / FeatureEntity::id eq id
                ).apply {
                    update.name?.let { add(OrgEntity::features / FeatureEntity::name ne it) }
                    update.path?.let { add(OrgEntity::features / FeatureEntity::path ne it) }
                }
            ),
            update = combine(
                mutableListOf<Bson>().apply {
                    update.name?.let { add(setValue(OrgEntity::features.colProperty.posOp / FeatureEntity::name, it)) }
                    update.path?.let { add(setValue(OrgEntity::features.colProperty.posOp / FeatureEntity::path, it)) }
                }
            )
        ) ?: return null
        return result.features.single { it.id == id }
    }

    override fun delete(orgId: UUID, id: UUID): Unit? {
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq orgId, OrgEntity::features / FeatureEntity::id eq id),
            update = pullByFilter(OrgEntity::features, FeatureEntity::id eq id)
        ) ?: return null
        return Unit
    }
}
