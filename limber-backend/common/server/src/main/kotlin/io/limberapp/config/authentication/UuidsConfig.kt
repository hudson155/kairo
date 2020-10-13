package io.limberapp.config.authentication

data class UuidsConfig(
    val generation: Generation,
) {
  enum class Generation { DETERMINISTIC, RANDOM }
}
