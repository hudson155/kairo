package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.mapper.app.org.OrgMapper
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import org.bson.conversions.Bson
import org.litote.kmongo.combine
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    private val orgMapper: OrgMapper
) : OrgService, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.name,
        clazz = OrgEntity::class
    ),
    indices = emptyList()
) {

    override fun create(model: OrgModel) {
        val entity = orgMapper.entity(model)
        collection.insertOne(entity)
    }

    override fun get(orgId: UUID): OrgModel? {
        val entity = collection.findOneById(orgId) ?: return null
        return orgMapper.model(entity)
    }

    override fun getByMemberId(memberId: UUID): List<OrgModel> {
        val entities = collection.find(OrgEntity::members / MembershipEntity::userId eq memberId)
        return entities.map { orgMapper.model(it) }
    }

    override fun update(orgId: UUID, update: OrgModel.Update): OrgModel {
        val updateEntity = orgMapper.update(update)
        val entity = collection.findOneByIdAndUpdate(
            id = orgId,
            update = combine(mutableListOf<Bson>().apply {
                updateEntity.name?.let { add(setValue(OrgEntity.Update::name, it)) }
            })
        ) ?: throw OrgNotFound()
        return orgMapper.model(entity)
    }

    override fun delete(orgId: UUID) {
        collection.findOneByIdAndDelete(orgId) ?: throw OrgNotFound()
    }
}
