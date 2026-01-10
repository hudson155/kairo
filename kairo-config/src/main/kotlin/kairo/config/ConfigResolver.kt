package kairo.config

/**
 * Config resolvers let you dynamically resolve config string values.
 * String values that start with [prefix]
 * will be mapped through [resolve].
 */
@Suppress("UseDataClass")
public class ConfigResolver(
  public val prefix: String,
  public val resolve: suspend (raw: String) -> String?,
)
