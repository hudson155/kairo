package io.limberapp.backend.module.orgs.entity.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.time.LocalDateTime
import java.util.UUID

data class FeatureEntity(
    val id: UUID,
    val created: LocalDateTime,
    val name: String,
    val path: String,
    val type: FeatureModel.Type
) {

    data class Update(
        val name: String?,
        val path: String?
    )
}
