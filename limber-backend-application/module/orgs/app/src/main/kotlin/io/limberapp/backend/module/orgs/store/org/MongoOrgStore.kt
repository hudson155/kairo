package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import org.litote.kmongo.div
import org.litote.kmongo.eq
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.collectionName,
        clazz = OrgEntity::class
    ),
    indices = emptyList()
) {

    override fun create(entity: OrgEntity) {
        collection.insertOne(entity)
    }

    override fun get(id: UUID) = collection.findOneById(id)

    override fun getByMemberId(memberId: UUID) =
        collection.find(OrgEntity::members / MembershipEntity::userId eq memberId)

    override fun update(id: UUID, update: OrgEntity.Update) =
        collection.findOneByIdAndUpdate(id, update)

    override fun delete(id: UUID) = collection.findOneByIdAndDelete(id)
}
