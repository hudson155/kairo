package kairo.config

import kairo.protectedString.ProtectedString

internal class ConfigProtectedStringDeserializer(
  config: ConfigLoaderConfig,
) : ConfigDeserializer<ProtectedString>(ProtectedString::class, config) {
  override fun isSecure(source: ConfigLoaderSource): Boolean =
    when (source) {
      is ConfigLoaderSource.Command -> false
      is ConfigLoaderSource.EnvironmentVariable -> false
      is ConfigLoaderSource.GcpSecret -> true
    }

  override fun convert(string: ProtectedString): ProtectedString =
    string
}
