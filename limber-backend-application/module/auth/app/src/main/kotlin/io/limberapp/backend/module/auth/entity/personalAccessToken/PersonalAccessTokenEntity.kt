package io.limberapp.backend.module.auth.entity.personalAccessToken

import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class PersonalAccessTokenEntity(
    @BsonId val id: UUID,
    val created: LocalDateTime,
    val userId: UUID,
    val token: String
) {

    companion object {
        const val name = "PersonalAccessToken"
    }
}
