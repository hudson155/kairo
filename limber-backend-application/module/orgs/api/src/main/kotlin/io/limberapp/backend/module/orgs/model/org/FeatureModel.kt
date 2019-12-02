package io.limberapp.backend.module.orgs.model.org

import java.time.LocalDateTime
import java.util.UUID

data class FeatureModel(
    val id: UUID,
    val created: LocalDateTime,
    val name: String,
    val path: String,
    val type: Type
) {

    enum class Type {
        HOME;
    }

    data class Update(
        val name: String?,
        val path: String?
    )
}
