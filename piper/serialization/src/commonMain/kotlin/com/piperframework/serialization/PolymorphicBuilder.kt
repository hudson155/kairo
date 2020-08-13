package com.piperframework.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import kotlin.reflect.KClass

/**
 * Used to create serialization modules which help the serializer serialize and deserialize abstract classes
 * polymorphically.
 */
class PolymorphicBuilder<T : Any> internal constructor(private val baseClass: KClass<T>) {
  private val baseClassSerializer = PolymorphicSerializer(baseClass)

  private inner class ClassAndSerializer<U : T>(private val kClass: KClass<U>, private val serializer: KSerializer<U>) {
    fun applyTo(moduleBuilder: SerializersModuleBuilder) {
      moduleBuilder.contextual(kClass, baseClassSerializer as KSerializer<U>)
    }

    fun applyTo(moduleBuilder: PolymorphicModuleBuilder<T>) {
      moduleBuilder.subclass(kClass, serializer)
    }
  }

  private val serializers = mutableListOf<ClassAndSerializer<out T>>()

  fun <U : T> subclass(subclass: KClass<U>, serializer: KSerializer<U>) {
    serializers.add(ClassAndSerializer(subclass, serializer))
  }

  //
  internal fun applyTo(moduleBuilder: SerializersModuleBuilder) {
    moduleBuilder.contextual(baseClass, baseClassSerializer)
    serializers.forEach { it.applyTo(moduleBuilder) }
    moduleBuilder.polymorphic(baseClass) {
      serializers.forEach { it.applyTo(this) }
    }
  }
}

fun <T : Any> SerializersModuleBuilder.baseClass(
  baseClass: KClass<T>,
  buildAction: PolymorphicBuilder<T>.() -> Unit
) {
  PolymorphicBuilder(baseClass).apply(buildAction).applyTo(this)
}
