@file:Suppress("ForbiddenMethodCall")

package kairo.dependencyInjection

import com.google.inject.PrivateBinder
import com.google.inject.binder.AnnotatedElementBuilder

public inline fun <reified T : Any> PrivateBinder.expose(): AnnotatedElementBuilder =
  expose(T::class.java)
