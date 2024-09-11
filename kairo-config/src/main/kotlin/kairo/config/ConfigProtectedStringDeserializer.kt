package kairo.config

import kairo.protectedString.ProtectedString

/**
 * Adds support for [ProtectedString] from various [ConfigLoaderSource]s.
 */
internal class ConfigProtectedStringDeserializer(
  config: ConfigLoaderConfig,
) : ConfigDeserializer<ProtectedString>(ProtectedString::class, config) {
  override fun isSecure(source: ConfigLoaderSource): Boolean =
    when (source) {
      is ConfigLoaderSource.Command -> false // The command source is always unsafe.
      is ConfigLoaderSource.EnvironmentVariable -> false // Sensitive data should not be in environment variables.
      is ConfigLoaderSource.GcpSecret -> true
    }

  override fun convert(string: ProtectedString): ProtectedString =
    string
}
