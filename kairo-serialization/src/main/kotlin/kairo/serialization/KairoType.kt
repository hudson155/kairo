package kairo.serialization

import com.fasterxml.jackson.core.type.TypeReference
import java.lang.reflect.Type
import kairo.reflect.KairoType

public fun <T> KairoType<T>.jacksonTypeReference(): TypeReference<T> =
  object : TypeReference<T>() {
    override fun getType(): Type =
      javaType
  }
