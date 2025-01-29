package kairo.reflect

import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.jvm.javaType

@Suppress("UseDataClass")
public class KairoType<T : Any>(
  public val kotlinType: KType,
) {
  public val javaType: Type
    get() = kotlinType.javaType

  @Suppress("UNCHECKED_CAST")
  public val kotlinClass: KClass<T>
    get() = kotlinType.classifier as KClass<T>

  public val javaClass: Class<T>
    get() = kotlinClass.java

  public companion object {
    public fun <T : Any> from(baseClass: KClass<*>, i: Int, thisClass: KClass<*>): KairoType<T> {
      val supertype = thisClass.allSupertypes.single { it.classifier == baseClass }
      return KairoType(supertype.arguments[i].type!!)
    }
  }
}
