package io.limberapp.backend.module.orgs.entity.org

import com.piperframework.entity.CompleteEntity
import com.piperframework.entity.UpdateEntity
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class OrgEntity(
    @BsonId override val id: UUID,
    override val created: LocalDateTime,
    override val version: Int,
    val name: String,
    val members: List<MembershipEntity>
) : CompleteEntity() {

    data class Update(
        val name: String?
    ) : UpdateEntity()

    companion object {
        const val collectionName = "Org"
    }
}
