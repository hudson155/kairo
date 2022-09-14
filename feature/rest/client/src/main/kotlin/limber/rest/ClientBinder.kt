package limber.rest

import com.google.inject.PrivateBinder
import kotlin.reflect.KClass

public interface ClientBinder {
  public fun <T : Any> bind(interfaceKClass: KClass<T>, block: () -> KClass<out T>)
}

public fun PrivateBinder.bindClients(block: ClientBinder.() -> Unit) {
  object : ClientBinder {
    override fun <T : Any> bind(interfaceKClass: KClass<T>, block: () -> KClass<out T>) {
      bind(interfaceKClass.java).to(block().java)
      expose(interfaceKClass.java)
    }
  }.block()
}
