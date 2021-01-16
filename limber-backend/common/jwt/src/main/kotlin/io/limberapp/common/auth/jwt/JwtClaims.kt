package io.limberapp.common.auth.jwt

private const val BASE: String = "https://limberapp.io/"

object JwtClaims {
  const val permissions: String = BASE + "permissions"
  const val org: String = BASE + "org"
  const val features: String = BASE + "features"
  const val user: String = BASE + "user"
}
