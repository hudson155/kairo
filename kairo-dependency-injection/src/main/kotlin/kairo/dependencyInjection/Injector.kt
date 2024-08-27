@file:Suppress("ForbiddenMethodCall")

package kairo.dependencyInjection

import com.google.inject.Injector
import com.google.inject.Key
import kotlin.reflect.KClass

public inline fun <reified T : Any> Injector.getInstance(): T =
  getInstance(Key.get(type<T>()))

public fun <T : Any> Injector.getInstanceByClass(kClass: KClass<T>): T =
  getInstance(kClass.java)
