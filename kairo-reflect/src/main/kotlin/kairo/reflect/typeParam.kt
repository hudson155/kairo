package kairo.reflect

import io.leangen.geantyref.GenericTypeReflector
import java.lang.reflect.Type
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
public fun <T : Any> typeParamKclass(baseClass: KClass<*>, i: Int, thisClass: KClass<*>): KClass<T> =
  (typeParam(baseClass, i, thisClass) as Class<T>).kotlin

/**
 * USE THIS SPARINGLY, probably only in framework code.
 *
 * It's sometimes useful to have a [KClass] for a generic type param,
 * but it's inconvenient to pass it in explicitly.
 *
 * [typeParam] uses some JVM reflection magic from [GenericTypeReflector]
 * to find the [KClass] for a type param given its index.
 * Taking an index as an argument is a bit fragile,
 * but given that this should be used quite sparingly, it's okay.
 */
public fun typeParam(baseClass: KClass<*>, i: Int, thisClass: KClass<*>): Type {
  val param = baseClass.java.typeParameters[i]
  return GenericTypeReflector.getTypeParameter(thisClass.java, param)
}
