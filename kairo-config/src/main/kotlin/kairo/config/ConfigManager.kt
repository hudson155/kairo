package kairo.config

import com.typesafe.config.Config
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.config.ConfigManager.Companion.defaultSources
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

private val logger: KLogger = KotlinLogging.logger {}

/**
 * The config manager allows you to resolve config properties from multiple sources.
 * There are a few built-in sources (see [defaultSources]). Feel free to implement your own too.
 */
public class ConfigManager(sources: List<ConfigPropertySource<*>> = defaultSources) {
  private val hocon: Hocon =
    Hocon {
      serializersModule = SerializersModule {
        polymorphic(ConfigProperty::class) {
          sources.forEach { subclass(it) }
        }
      }
    }

  private val sources: Map<KClass<out ConfigProperty>, ConfigPropertySource<*>> =
    sources.associateBy { it.kClass }

  public inline fun <reified T : Any> deserialize(config: Config): T =
    deserialize(serializer<T>(), config)

  public fun <T : Any> deserialize(deserializer: DeserializationStrategy<T>, config: Config): T =
    hocon.decodeFromConfig(deserializer, config)

  @Suppress("UNCHECKED_CAST")
  public suspend fun <P : ConfigProperty> resolve(property: P): String? {
    logger.debug { "Resolving property (property=$property)." }
    val source = checkNotNull(sources[property::class])
    return (source as ConfigPropertySource<P>).resolve(property)
  }

  public companion object {
    private val defaultSources: List<ConfigPropertySource<*>> =
      listOf(
        GcpSecretConfigPropertySource(),
        PlaintextConfigPropertySource(),
      )
  }
}

private fun <P : ConfigProperty> PolymorphicModuleBuilder<ConfigProperty>.subclass(
  source: ConfigPropertySource<P>,
) {
  subclass(source.kClass, source.serializer)
}
