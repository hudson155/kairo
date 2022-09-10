package limber.rest

import com.google.inject.PrivateBinder
import kotlin.reflect.KClass

public fun <Interface : Any, Implementation : Interface> PrivateBinder.bindClient(
  interfaceKClass: KClass<Interface>,
  implementationKClass: KClass<Implementation>,
) {
  bind(interfaceKClass.java).to(implementationKClass.java)
  expose(interfaceKClass.java)
}
