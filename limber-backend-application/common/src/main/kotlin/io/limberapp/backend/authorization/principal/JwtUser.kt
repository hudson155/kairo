package io.limberapp.backend.authorization.principal

import com.piperframework.serialization.serializer.UuidSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class JwtUser(
    @Serializable(with = UuidSerializer::class)
    val id: UUID,
    val firstName: String?,
    val lastName: String?
)
