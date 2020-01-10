package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import org.bson.conversions.Bson
import org.litote.kmongo.combine
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.name,
        clazz = OrgEntity::class
    ),
    indices = emptyList()
) {

    override fun create(entity: OrgEntity) {
        collection.insertOne(entity)
    }

    override fun get(orgId: UUID) = collection.findOneById(orgId)

    override fun getByMemberId(memberId: UUID) =
        collection.find(OrgEntity::members / MembershipEntity::userId eq memberId)

    override fun update(orgId: UUID, update: OrgEntity.Update): OrgEntity {
        return collection.findOneByIdAndUpdate(
            id = orgId,
            update = combine(mutableListOf<Bson>().apply {
                update.name?.let { add(setValue(OrgEntity.Update::name, it)) }
            })
        ) ?: throw OrgNotFound()
    }

    override fun delete(orgId: UUID) {
        collection.findOneByIdAndDelete(orgId) ?: throw OrgNotFound()
    }
}
