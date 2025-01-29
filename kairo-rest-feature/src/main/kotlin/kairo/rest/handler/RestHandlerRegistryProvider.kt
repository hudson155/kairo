package kairo.rest.handler

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton

@Singleton
internal class RestHandlerRegistryProvider @Inject constructor(
  private val handlers: Set<RestHandler<*, *>>,
) : Provider<RestHandlerRegistry> {
  override fun get(): RestHandlerRegistry =
    handlers.associateBy { it.endpointType.kotlinClass }
}
