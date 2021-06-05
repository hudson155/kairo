package io.limberapp.auth.jwt

import kotlin.js.JsExport

private const val BASE: String = "https://limberapp.io/"

@JsExport
object JwtClaims {
  const val permissions: String = BASE + "permissions"
  const val org: String = BASE + "org"
  const val features: String = BASE + "features"
  const val user: String = BASE + "user"
}
