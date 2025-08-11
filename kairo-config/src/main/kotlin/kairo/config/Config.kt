package kairo.config

import com.typesafe.config.Config
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.serializer

public inline fun <reified T : Any> Config.deserialize(): T =
  Hocon.decodeFromConfig(serializer<T>(), this)
