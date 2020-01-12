package io.limberapp.backend.module.users.entity.user

import io.limberapp.backend.authorization.principal.JwtRole
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class UserEntity(
    @BsonId val id: UUID,
    val created: LocalDateTime,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val profilePhotoUrl: String?,
    val roles: Set<JwtRole>
) {

    data class Update(
        val firstName: String?,
        val lastName: String?
    )

    companion object {
        const val name = "User"
    }
}
