package kairo.config

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.config.ConfigPropertySource.ConfigProperty
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

public class ConfigPropertyResolver internal constructor(sources: List<ConfigPropertySource<*>>) {
  private val sources: Map<KClass<out ConfigProperty>, ConfigPropertySource<*>> =
    sources.associateBy { it.kClass }

  @Suppress("UNCHECKED_CAST")
  public suspend fun <P : ConfigProperty> resolve(property: P): String? {
    logger.debug { "Resolving property: $property." }
    val source = checkNotNull(sources[property::class])
    return (source as ConfigPropertySource<P>).resolve(property)
  }
}
