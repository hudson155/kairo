@file:Suppress("ForbiddenMethodCall")

package kairo.dependencyInjection

import com.google.inject.Singleton
import com.google.inject.binder.AnnotatedBindingBuilder

/**
 * Unlike the built-in [AnnotatedBindingBuilder.asEagerSingleton],
 * this makes singletons explicitly lazy.
 */
public fun AnnotatedBindingBuilder<*>.asLazySingleton() {
  `in`(Singleton::class.java)
}
