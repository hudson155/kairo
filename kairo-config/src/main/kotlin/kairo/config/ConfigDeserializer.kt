package kairo.config

import com.typesafe.config.Config
import kairo.config.ConfigPropertySource.ConfigProperty
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

public class ConfigDeserializer internal constructor(sources: List<ConfigPropertySource<*>>) {
  private val hocon: Hocon =
    Hocon {
      serializersModule = SerializersModule {
        polymorphic(ConfigProperty::class) {
          sources.forEach { subclass(it) }
        }
      }
    }

  public inline fun <reified T : Any> deserialize(config: Config): T =
    deserialize(serializer<T>(), config)

  public fun <T : Any> deserialize(deserializer: DeserializationStrategy<T>, config: Config): T =
    hocon.decodeFromConfig(deserializer, config)
}

private fun <P : ConfigProperty> PolymorphicModuleBuilder<ConfigProperty>.subclass(
  source: ConfigPropertySource<P>,
) {
  subclass(source.kClass, source.serializer)
}
