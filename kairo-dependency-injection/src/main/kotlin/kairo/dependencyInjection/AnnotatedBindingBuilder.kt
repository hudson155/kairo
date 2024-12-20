@file:Suppress("ForbiddenMethodCall")

package kairo.dependencyInjection

import com.google.inject.Singleton
import com.google.inject.binder.AnnotatedBindingBuilder
import com.google.inject.binder.LinkedBindingBuilder
import com.google.inject.name.Names

/**
 * Shorthand for annotating a binding with a name.
 */
public fun <T : Any> AnnotatedBindingBuilder<T>.named(name: String): LinkedBindingBuilder<T> =
  annotatedWith(Names.named(name))

/**
 * Unlike the built-in [AnnotatedBindingBuilder.asEagerSingleton],
 * this makes singletons explicitly lazy.
 */
public fun AnnotatedBindingBuilder<*>.asLazySingleton() {
  `in`(Singleton::class.java)
}
