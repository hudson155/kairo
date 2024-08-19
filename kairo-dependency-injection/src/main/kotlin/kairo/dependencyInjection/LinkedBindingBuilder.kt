@file:Suppress("ForbiddenMethodCall")

package kairo.dependencyInjection

import com.google.inject.Provider
import com.google.inject.binder.LinkedBindingBuilder
import com.google.inject.binder.ScopedBindingBuilder
import kotlin.reflect.KClass

public fun <T : Any> LinkedBindingBuilder<in T>.toClass(kClass: KClass<T>): ScopedBindingBuilder =
  to(kClass.java)

public fun <T : Any> LinkedBindingBuilder<T>.toProvider(kClass: KClass<out Provider<T>>): ScopedBindingBuilder =
  toProvider(kClass.java)
