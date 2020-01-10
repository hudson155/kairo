package io.limberapp.backend.module.users.entity.user

import com.piperframework.entity.CompleteEntity
import com.piperframework.entity.UpdateEntity
import io.limberapp.backend.authorization.principal.JwtRole
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class UserEntity(
    @BsonId override val id: UUID,
    override val created: LocalDateTime,
    val firstName: String?,
    val lastName: String?,
    val emailAddress: String,
    val profilePhotoUrl: String?,
    val roles: Set<JwtRole>
) : CompleteEntity {

    data class Update(
        val firstName: String?,
        val lastName: String?
    ) : UpdateEntity

    companion object {
        const val name = "User"
    }
}
