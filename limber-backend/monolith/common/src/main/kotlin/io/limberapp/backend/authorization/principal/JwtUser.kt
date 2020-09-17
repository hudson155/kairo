package io.limberapp.backend.authorization.principal

import io.limberapp.common.types.UUID

data class JwtUser(
  val guid: UUID,
  val firstName: String?,
  val lastName: String?,
)
