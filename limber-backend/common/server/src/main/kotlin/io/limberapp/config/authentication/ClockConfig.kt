package io.limberapp.config.authentication

data class ClockConfig(
  val type: Type,
) {
  enum class Type { FIXED, REAL }
}
