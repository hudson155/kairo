package kairo.config

import kairo.config.ConfigPropertySource.ConfigProperty
import kairo.reflect.inferClass
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Each config property source provides a different strategy for how to resolve the config property.
 * There are a few built-in ones (see [ConfigManager.defaultSources]). Feel free to implement your own too.
 */
public abstract class ConfigPropertySource<T : ConfigProperty> {
  /**
   * This must be a data class that defines the shape of the config property.
   */
  @Serializable
  public abstract class ConfigProperty

  public val kClass: KClass<T> = inferClass(ConfigPropertySource::class, 0, this::class)

  /**
   * Call [ConfigProperty.serializer] to get the serializer.
   */
  public abstract val serializer: KSerializer<T>

  /**
   * This method does the heavy lifting for resolving the config property.
   * It may be called multiple times for the same property.
   */
  public abstract suspend fun resolve(property: T): String?
}
