package io.limberapp.backend.module.orgs.entity.org

import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.UpdateEntity
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

object OrgEntity {

    const val collectionName = "Org"

    data class Complete(
        @BsonId override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val name: String,
        val members: List<MembershipEntity.Complete>
    ) : CompleteEntity()

    data class Update(
        val name: String?
    ) : UpdateEntity()
}
