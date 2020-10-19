package io.limberapp.common.config.authentication

data class UuidsConfig(
    val generation: Generation,
) {
  enum class Generation { DETERMINISTIC, RANDOM }
}
