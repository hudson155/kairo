@file:Suppress("ForbiddenMethodCall")

package kairo.dependencyInjection

import com.google.inject.Injector
import com.google.inject.Key

public inline fun <reified T : Any> Injector.getInstance(): T =
  getInstance(Key.get(type<T>()))
