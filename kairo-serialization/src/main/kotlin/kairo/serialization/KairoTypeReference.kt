package kairo.serialization

import com.fasterxml.jackson.core.type.TypeReference
import java.lang.reflect.Type
import kairo.reflect.KairoType

public class KairoTypeReference<T>(
  private val type: KairoType<*>,
) : TypeReference<T>() {
  override fun getType(): Type = type.javaType
}

public val <T : Any> KairoType<T>.typeReference: KairoTypeReference<T>
  get() = KairoTypeReference(this)
