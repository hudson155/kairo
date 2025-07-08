@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URL
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.typeReference

public inline fun <reified T : Any> ObjectMapper.kairoRead(string: String): T =
  kairoRead(string, kairoType<T>())

public fun <T : Any> ObjectMapper.kairoRead(string: String, type: KairoType<T>): T =
  kairoRead(string, type.typeReference)

public fun <T : Any> ObjectMapper.kairoRead(string: String, typeReference: TypeReference<T>): T =
  readValue(string, typeReference)

public fun <T : Any> ObjectMapper.kairoRead(string: String, type: JavaType): T =
  readValue(string, type)

public inline fun <reified T : Any> ObjectMapper.kairoRead(url: URL): T =
  kairoRead(url, kairoType<T>())

public fun <T : Any> ObjectMapper.kairoRead(url: URL, type: KairoType<T>): T =
  kairoRead(url, type.typeReference)

public fun <T : Any> ObjectMapper.kairoRead(url: URL, typeReference: TypeReference<T>): T =
  readValue(url, typeReference)

public fun <T : Any> ObjectMapper.kairoRead(url: URL, type: JavaType): T =
  readValue(url, type)
