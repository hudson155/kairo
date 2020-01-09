package io.limberapp.backend.module.auth.entity.personalAccessToken

import com.piperframework.entity.CompleteEntity
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class PersonalAccessTokenEntity(
    @BsonId override val id: UUID,
    override val created: LocalDateTime,
    val userId: UUID,
    val token: String
) : CompleteEntity {

    companion object {
        const val name = "PersonalAccessToken"
    }
}
