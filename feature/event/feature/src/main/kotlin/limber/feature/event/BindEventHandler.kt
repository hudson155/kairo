package limber.feature.event

import com.google.inject.PrivateBinder
import kotlin.reflect.KClass

public fun PrivateBinder.bindEventHandler(kClass: KClass<out EventHandler<*>>) {
  bind(kClass.java).asEagerSingleton()
  expose(kClass.java)
}
