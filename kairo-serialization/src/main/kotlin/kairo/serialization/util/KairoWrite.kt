@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.typeReference

public inline fun <reified T : Any> ObjectMapper.kairoWrite(value: T?): String =
  kairoWrite(value, kairoType<T>())

/**
 * Equivalent of [ObjectMapper.writeValueAsString], but doesn't suffer from type erasure.
 * https://stackoverflow.com/questions/34193177/why-does-jackson-polymorphic-serialization-not-work-in-lists
 */
public fun <T : Any> ObjectMapper.kairoWrite(value: T?, type: KairoType<T>): String =
  kairoWrite(value, type.typeReference)

public fun <T : Any> ObjectMapper.kairoWrite(value: T?, typeReference: TypeReference<T>): String =
  writerFor(typeReference).writeValueAsString(value)

public fun <T : Any> ObjectMapper.kairoWrite(value: T?, type: JavaType): String =
  writerFor(type).writeValueAsString(value)
