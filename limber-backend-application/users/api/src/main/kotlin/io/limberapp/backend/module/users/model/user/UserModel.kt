package io.limberapp.backend.module.users.model.user

import io.limberapp.framework.model.CreationModel
import io.limberapp.framework.model.UpdateModel
import java.time.LocalDateTime
import java.util.UUID

data class UserModel(
    override val id: UUID,
    override val created: LocalDateTime,
    override val version: Int,
    val firstName: String?,
    val lastName: String?,
    val emailAddress: String,
    val profilePhotoUrl: String?
) : CreationModel() {

    data class Update(
        val firstName: String?,
        val lastName: String?
    ) : UpdateModel()
}
