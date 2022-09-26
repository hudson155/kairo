package limber.config

internal data class ConfigString(
  val type: Type,
  val value: String?,
) {
  enum class Type { Plaintext, EnvironmentVariable, GcpSecret, Command }
}
