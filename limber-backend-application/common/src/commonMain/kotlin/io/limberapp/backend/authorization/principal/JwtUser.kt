package io.limberapp.backend.authorization.principal

import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.UUID
import kotlinx.serialization.Serializable

@Serializable
data class JwtUser(
  @Serializable(with = UuidSerializer::class)
  val guid: UUID,
  val firstName: String,
  val lastName: String
)
