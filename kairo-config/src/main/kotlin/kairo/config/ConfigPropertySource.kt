package kairo.config

import kairo.reflect.inferClass
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer

/**
 * Each config property source provides a different strategy for how to resolve the config property.
 * There are a few built-in ones (see [ConfigManager.defaultSources]). Feel free to implement your own too.
 */
public abstract class ConfigPropertySource<P : ConfigProperty> {
  public val kClass: KClass<P> = inferClass(ConfigPropertySource::class, 0, this::class)

  /**
   * Call [ConfigProperty.serializer] to get the serializer.
   */
  public abstract val serializer: KSerializer<P>

  /**
   * This method does the heavy lifting for resolving the config property.
   * It may be called multiple times for the same property.
   */
  public abstract suspend fun resolve(property: P): String?
}
