package io.limberapp.common.config

data class UuidsConfig(
    val generation: Generation,
) {
  enum class Generation { DETERMINISTIC, RANDOM }
}
