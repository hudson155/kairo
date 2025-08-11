package kairo.config

import com.typesafe.config.Config
import kairo.config.ConfigPropertySource.ConfigProperty
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

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
  public suspend fun <T : ConfigProperty> resolveProperty(configProperty: T): String? {
    val configPropertySource = checkNotNull(sources[configProperty::class])
    return (configPropertySource as ConfigPropertySource<T>).resolve(configProperty)
  }

  public companion object {
    private val defaultSources: List<ConfigPropertySource<*>> =
      listOf(
        EnvironmentVariableConfigPropertySource(),
        PlaintextConfigPropertySource(),
      )
  }
}

private fun <T : ConfigProperty> PolymorphicModuleBuilder<ConfigProperty>.subclass(
  source: ConfigPropertySource<T>,
) {
  subclass(source.kClass, source.serializer)
}
