package io.limberapp.backend.module.auth.entity.personalAccessToken

import io.limberapp.framework.entity.CompleteEntity
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class PersonalAccessTokenEntity(
    @BsonId override val id: UUID,
    override val created: LocalDateTime,
    override val version: Int,
    val userId: UUID,
    val token: String
) : CompleteEntity() {

    companion object {
        const val collectionName = "PersonalAccessToken"
    }
}
