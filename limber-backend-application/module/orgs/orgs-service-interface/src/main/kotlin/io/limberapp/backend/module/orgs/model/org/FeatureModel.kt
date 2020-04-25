package io.limberapp.backend.module.orgs.model.org

import java.time.LocalDateTime
import java.util.UUID

data class FeatureModel(
    val guid: UUID,
    val createdDate: LocalDateTime,
    val name: String,
    val path: String,
    val type: Type,
    val isDefaultFeature: Boolean
) {
    enum class Type {
        FORMS,
        HOME;
    }

    data class Update(
        val name: String?,
        val path: String?,
        val isDefaultFeature: Boolean?
    )
}
