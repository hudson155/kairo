package io.limberapp.web.auth

internal data class AuthContext(
  val isLoading: Boolean,
  val isAuthenticated: Boolean,
  val signIn: () -> Unit,
  val jwt: Jwt?,
  val signOut: () -> Unit
)
