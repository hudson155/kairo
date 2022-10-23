package limber.feature.rest

import com.google.inject.PrivateBinder
import kotlin.reflect.KClass

public fun PrivateBinder.bindRestEndpoint(kClass: KClass<out RestEndpointHandler<*, *>>) {
  bind(kClass.java)
  expose(kClass.java)
}
