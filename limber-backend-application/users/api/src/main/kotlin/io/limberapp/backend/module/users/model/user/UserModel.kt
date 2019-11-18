package io.limberapp.backend.module.users.model.user

import java.time.LocalDateTime
import java.util.UUID

data class UserModel(
    val id: UUID,
    val created: LocalDateTime,
    val version: Int,
    val firstName: String?,
    val lastName: String?,
    val emailAddress: String,
    val profilePhotoUrl: String?
) {

    data class Update(
        val firstName: String?,
        val lastName: String?
    )
}
