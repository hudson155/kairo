package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.module.annotation.Store
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.backend.module.orgs.exception.conflict.UserIsAlreadyAMemberOfOrg
import io.limberapp.backend.module.orgs.exception.notFound.MembershipNotFound
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.mapper.app.membership.MembershipMapper
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.service.org.MembershipService
import io.limberapp.backend.module.orgs.service.org.OrgService
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoMembershipStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    @Store private val orgStore: OrgService,
    private val membershipMapper: MembershipMapper
) : MembershipService, MongoStore<OrgEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = OrgEntity.name,
        clazz = OrgEntity::class
    ),
    index = { ensureIndex(ascending(OrgEntity::members / MembershipEntity::userId), unique = false) }
) {

    override fun create(orgId: UUID, model: MembershipModel) {
        val entity = membershipMapper.entity(model)
        orgStore.get(orgId) ?: throw OrgNotFound()
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq orgId, OrgEntity::members / MembershipEntity::userId ne entity.userId),
            update = push(OrgEntity::members, entity)
        ) ?: throw UserIsAlreadyAMemberOfOrg()
    }

    override fun get(orgId: UUID, userId: UUID): MembershipModel? {
        val org = orgStore.get(orgId) ?: throw OrgNotFound()
        return org.members.singleOrNull { it.userId == userId }
    }

    override fun delete(orgId: UUID, userId: UUID) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        collection.findOneAndUpdate(
            filter = and(OrgEntity::id eq orgId, OrgEntity::members / MembershipEntity::userId eq userId),
            update = pullByFilter(OrgEntity::members, MembershipEntity::userId eq userId)
        ) ?: throw MembershipNotFound()
    }
}
