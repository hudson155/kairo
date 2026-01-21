package kairo.hocon

import com.typesafe.config.Config
import com.typesafe.config.ConfigRenderOptions
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.KairoJson

private val logger: KLogger = KotlinLogging.logger {}

public inline fun <reified T> KairoJson.deserialize(hocon: Config): T =
  deserialize(hocon, kairoType())

public fun <T> KairoJson.deserialize(hocon: Config, type: KairoType<T>): T {
  val string = hocon.root().render(ConfigRenderOptions.concise())
  logger.info { "Deserializing HOCON: $string" }
  return deserialize(string, type)
}
