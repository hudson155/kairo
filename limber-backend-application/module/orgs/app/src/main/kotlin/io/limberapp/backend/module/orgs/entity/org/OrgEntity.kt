package io.limberapp.backend.module.orgs.entity.org

import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class OrgEntity(
    @BsonId val id: UUID,
    val created: LocalDateTime,
    val name: String,
    val features: List<FeatureEntity>,
    val members: List<MembershipEntity>
) {

    data class Update(
        val name: String?
    )

    companion object {
        const val name = "Org"
    }
}
