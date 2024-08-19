@file:Suppress("ForbiddenMethodCall")

package kairo.dependencyInjection

import com.google.inject.Injector

public inline fun <reified T : Any> Injector.getInstance(): T =
  getInstance(T::class.java)
