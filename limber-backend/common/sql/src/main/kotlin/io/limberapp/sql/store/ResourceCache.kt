package io.limberapp.sql.store

import com.google.common.io.Resources
import java.util.concurrent.ConcurrentHashMap

@RequiresOptIn
internal annotation class ResourceManipulation

internal val DEFAULT_GET_RESOURCE: (String) -> String =
    { Resources.getResource(it).readText() }

/**
 * The [getResource] getter defines how resources should be fetched. [Resources.getResource] should
 * always be used in production code. Using this as a delegate function, however, allows for proper
 * testing. In order to avoid changing how resources are fetched in production, the
 * [ResourceManipulation] annotation prevents modification without explicit opt-in, which should
 * only be in test code.
 */
internal var getResource: (resourceName: String) -> String = DEFAULT_GET_RESOURCE
  @ResourceManipulation set

/**
 * Thread-safe cache for JVM resources.
 */
internal open class ResourceCache {
  private val cache = ConcurrentHashMap<String, String>()

  open operator fun get(resourceName: String): String = cache.getOrPut(resourceName) {
    try {
      return@getOrPut getResource(resourceName)
    } catch (e: IllegalStateException) {
      throw IllegalStateException("Could not load $resourceName.", e)
    }
  }
}
