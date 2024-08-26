@file:Suppress("ForbiddenMethodCall")

package kairo.dependencyInjection

import com.google.inject.Binder
import com.google.inject.binder.AnnotatedBindingBuilder

public inline fun <reified T : Any> Binder.bind(): AnnotatedBindingBuilder<T> =
  bind(type<T>())
