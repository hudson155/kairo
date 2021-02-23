package io.limberapp.config

data class ClockConfig(
    val type: Type,
) {
  enum class Type { FIXED, REAL }
}
