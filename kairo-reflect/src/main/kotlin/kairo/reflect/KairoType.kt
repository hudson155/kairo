package kairo.reflect

import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf

/**
 * Unifies [Class], [KClass], [Type], and [KType] into a safer and richer wrapper
 * Preserves full generic fidelity.
 */
public data class KairoType<T>(
  public val kotlinType: KType,
) {
  public val javaType: Type
    get() = kotlinType.javaType

  @Suppress("UNCHECKED_CAST")
  public val kotlinClass: KClass<T & Any>
    get() = kotlinType.classifier as KClass<T & Any>

  public val javaClass: Class<T & Any>
    get() = kotlinClass.java

  public companion object {
    /**
     * Infers a [KairoType] at runtime, from within a generic abstract class.
     */
    public fun <T> from(baseClass: KClass<*>, i: Int, thisClass: KClass<*>): KairoType<T> {
      val supertype = thisClass.allSupertypes.single { it.classifier == baseClass }
      val type = checkNotNull(supertype.arguments[i].type)
      return KairoType(type)
    }
  }
}

public inline fun <reified T> kairoType(): KairoType<T> =
  KairoType(typeOf<T>())
