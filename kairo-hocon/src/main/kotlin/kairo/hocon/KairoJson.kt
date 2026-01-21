package kairo.hocon

import com.typesafe.config.Config
import com.typesafe.config.ConfigRenderOptions
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.KairoJson

public inline fun <reified T> KairoJson.deserialize(hocon: Config): T =
  deserialize(hocon, kairoType())

public fun <T> KairoJson.deserialize(hocon: Config, type: KairoType<T>): T {
  val string = hocon.root().render(ConfigRenderOptions.concise())
  return deserialize(string, type)
}
