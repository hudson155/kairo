package io.limberapp.auth.jwt

private const val BASE = "https://limberapp.io/"

object JwtClaims {
  const val org = BASE + "org"
  const val roles = BASE + "roles"
  const val user = BASE + "user"
}
