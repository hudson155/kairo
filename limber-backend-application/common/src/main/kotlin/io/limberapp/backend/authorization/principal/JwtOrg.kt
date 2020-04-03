package io.limberapp.backend.authorization.principal

import com.piperframework.serialization.UuidSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class JwtOrg(
    @Serializable(with = UuidSerializer::class)
    val id: UUID,
    val name: String,
    val featureIds: List<@Serializable(with = UuidSerializer::class) UUID>
)
