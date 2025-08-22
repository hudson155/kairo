package kairo.rest.server

import com.google.inject.Binder
import com.google.inject.multibindings.Multibinder
import kairo.dependencyInjection.DelegatingProvider
import kairo.dependencyInjection.getInstanceByClass
import kairo.dependencyInjection.type
import kairo.rest.handler.RestHandler
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

public inline fun <reified T : Any> Binder.bindRestHandlers() {
  bindRestHandlers(T::class)
}

/**
 * Uses JVM reflection to bind REST handlers using a [Multibinder],
 * which allows multiple instances to be bound separately but injected together as a set.
 */
public fun <T : Any> Binder.bindRestHandlers(kClass: KClass<T>) {
  bindRestHandlers { multibinder ->
    kClass.nestedClasses.forEach { restHandler ->
      try {
        @Suppress("UNCHECKED_CAST")
        restHandler as KClass<RestHandler<*, *>>
      } catch (e: ClassCastException) {
        throw IllegalArgumentException("Class was not a REST handler: ${restHandler.qualifiedName!!}.", e)
      }
      require(restHandler.isInner) { "REST handlers must be inner classes: ${restHandler.qualifiedName!!}." }
      val constructor = restHandler.primaryConstructor
      requireNotNull(constructor) { "REST handlers must have a primary constructor: ${restHandler.qualifiedName!!}." }
      val provider = DelegatingProvider { injector ->
        constructor.call(injector.getInstanceByClass(kClass)).apply {
          injector.injectMembers(this)
        }
      }
      multibinder.addBinding().toProvider(provider)
    }
  }
}

internal fun Binder.bindRestHandlers(block: (multibinder: Multibinder<RestHandler<*, *>>) -> Unit) {
  val multibinder = Multibinder.newSetBinder(this, type<RestHandler<*, *>>())
  block(multibinder)
}
