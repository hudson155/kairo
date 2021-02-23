package io.limberapp.config

data class UuidsConfig(
    val generation: Generation,
) {
  enum class Generation { DETERMINISTIC, RANDOM }
}
