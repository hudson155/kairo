package kairo.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.allSupertypes

@Suppress("UNCHECKED_CAST")
public fun <T : Any> inferClass(baseClass: KClass<*>, i: Int, thisClass: KClass<*>): KClass<T> =
  inferType(baseClass, i, thisClass).classifier as KClass<T>

public fun inferType(baseClass: KClass<*>, i: Int, thisClass: KClass<*>): KType {
  val supertype = thisClass.allSupertypes.single { it.classifier == baseClass }
  return checkNotNull(supertype.arguments[i].type)
}
