package kairo.rest

import io.ktor.util.reflect.TypeInfo
import kairo.reflect.KairoType

public fun <T : Any> KairoType<T>.toKtor(): TypeInfo =
  TypeInfo(
    type = kotlinClass,
    kotlinType = kotlinType,
  )
