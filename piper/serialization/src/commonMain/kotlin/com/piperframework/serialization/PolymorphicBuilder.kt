package com.piperframework.serialization

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Used to create serialization modules which help the serializer serialize and deserialize abstract classes
 * polymorphically.
 */
@OptIn(ImplicitReflectionSerializer::class)
class PolymorphicBuilder<T : Any> internal constructor(private val baseClass: KClass<T>) {

    private class ClassAndSerializer<U : Any>(private val klass: KClass<U>, private val serializer: KSerializer<U>) {

        fun applyTo(moduleBuilder: SerializersModuleBuilder) {
            moduleBuilder.contextual(klass, serializer)
        }

        fun applyTo(moduleBuilder: PolymorphicModuleBuilder<Any>) {
            moduleBuilder.addSubclass(klass, klass.serializer())
        }
    }

    private val serializers = mutableListOf<ClassAndSerializer<out T>>()

    fun <U : T> subclass(subclass: KClass<U>) {
        @Suppress("UNCHECKED_CAST")
        serializers.add(ClassAndSerializer(subclass, PolymorphicSerializer(baseClass) as KSerializer<U>))
    }

    internal fun applyTo(moduleBuilder: SerializersModuleBuilder) {
        moduleBuilder.contextual(baseClass, PolymorphicSerializer(baseClass))
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
