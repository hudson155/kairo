package kairo.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.allSupertypes

@Suppress("UNCHECKED_CAST")
public fun <T : Any> inferKClass(baseClass: KClass<*>, i: Int, thisClass: KClass<*>): KClass<T> =
  inferKType(baseClass, i, thisClass).classifier as KClass<T>

public fun inferKType(baseClass: KClass<*>, i: Int, thisClass: KClass<*>): KType {
  val supertype = thisClass.allSupertypes.single { it.classifier == baseClass }
  return checkNotNull(supertype.arguments[i].type)
}
