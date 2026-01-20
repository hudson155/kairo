package kairo.hocon

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.typesafe.config.Config
import com.typesafe.config.ConfigRenderOptions
import kairo.serialization.KairoJson

public inline fun <reified T> KairoJson.deserialize(hocon: Config): T =
  deserialize(hocon, jacksonTypeRef())

public fun <T> KairoJson.deserialize(hocon: Config, typeReference: TypeReference<T>): T {
  val string = hocon.root().render(ConfigRenderOptions.concise())
  return deserialize(string, typeReference)
}
