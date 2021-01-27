package io.limberapp.common.config

data class ClockConfig(
    val type: Type,
) {
  enum class Type { FIXED, REAL }
}
