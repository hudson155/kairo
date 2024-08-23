package kairo.reflect

import io.leangen.geantyref.GenericTypeReflector
import kotlin.reflect.KClass

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
@Suppress("UNCHECKED_CAST")
public fun <T : Any> typeParam(i: Int, baseClass: KClass<*>, thisClass: KClass<*>): KClass<T> {
  val param = baseClass.java.typeParameters[i]
  val typeParameter = GenericTypeReflector.getTypeParameter(thisClass.java, param)
  return (typeParameter as Class<T>).kotlin
}
