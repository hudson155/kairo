package io.limberapp.common.auth.jwt

private const val BASE = "https://limberapp.io/"

object JwtClaims {
  const val permissions = BASE + "permissions"
  const val org = BASE + "org"
  const val features = BASE + "features"
  const val user = BASE + "user"
}
