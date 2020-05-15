package io.limberapp.backend.authorization.principal

import kotlinx.serialization.Serializable

@Serializable
enum class JwtRole {
  IDENTITY_PROVIDER,
  SUPERUSER;
}
