package io.limberapp.backend.authorization.principal

import java.util.*

data class JwtUser(
  val guid: UUID,
  val firstName: String?,
  val lastName: String?,
)
