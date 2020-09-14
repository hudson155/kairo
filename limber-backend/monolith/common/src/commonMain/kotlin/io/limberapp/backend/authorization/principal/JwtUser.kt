package io.limberapp.backend.authorization.principal

import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.UUID
import kotlinx.serialization.Serializable

@Serializable
data class JwtUser(
  @Serializable(with = UuidSerializer::class)
  val guid: UUID,
  val firstName: String?,
  val lastName: String?,
)
