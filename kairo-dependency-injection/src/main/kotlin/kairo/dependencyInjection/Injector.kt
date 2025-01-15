@file:Suppress("ForbiddenMethodCall", "UnnecessaryLet")

package kairo.dependencyInjection

import com.google.inject.Injector
import com.google.inject.Key
import kotlin.reflect.KClass

public inline fun <reified T : Any> Injector.getInstance(): T =
  getInstance(key())

public inline fun <reified T : Any> Injector.hasInstance(): Boolean =
  hasInstance(key<T>())

public inline fun <reified T : Any> Injector.hasInstance(key: Key<T>): Boolean =
  getExistingBinding<T>(key) != null

public fun <T : Any> Injector.getInstanceByClass(kClass: KClass<T>): T =
  getInstance(Key.get(kClass.java))

public inline fun <reified T : Any> Injector.getNamedInstance(name: String): T =
  getInstance(namedKey(name))

public inline fun <reified T : Any> Injector.getInstanceOptional(): T? =
  getInstanceOptional(key())

public fun <T : Any> Injector.getInstanceOptionalByClass(kClass: KClass<T>): T? =
  getInstanceOptional(Key.get(kClass.java))

public inline fun <reified T : Any> Injector.getNamedInstanceOptional(name: String): T? =
  getInstanceOptional(namedKey(name))

public inline fun <reified T : Any> Injector.getInstanceOptional(key: Key<T>): T? {
  if (!hasInstance(key)) return null
  return getInstance(key)
}
