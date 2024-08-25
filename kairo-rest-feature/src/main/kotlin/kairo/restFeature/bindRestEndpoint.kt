package kairo.restFeature

import com.google.inject.PrivateBinder
import com.google.inject.multibindings.Multibinder
import kairo.dependencyInjection.expose
import kairo.dependencyInjection.toClass
import kairo.dependencyInjection.type

/**
 * Binding REST endpoints uses a [Multibinder],
 * which allows multiple instances to be bound separately but injected together as a set.
 */
public inline fun <reified Handler : RestHandler<*>> PrivateBinder.bindRestEndpoint() {
  val multibinder = Multibinder.newSetBinder(this, type<RestHandler<*>>())
  multibinder.addBinding().toClass(Handler::class)
  expose<Set<RestHandler<*>>>()
}
