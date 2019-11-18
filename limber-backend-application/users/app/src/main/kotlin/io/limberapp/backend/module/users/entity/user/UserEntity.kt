package io.limberapp.backend.module.users.entity.user

import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.UpdateEntity
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

object UserEntity {

    const val collectionName = "User"

    data class Complete(
        @BsonId override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val firstName: String?,
        val lastName: String?,
        val emailAddress: String,
        val profilePhotoUrl: String?
    ) : CompleteEntity()

    data class Update(
        val firstName: String?,
        val lastName: String?
    ) : UpdateEntity()
}
