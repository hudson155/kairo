package io.limberapp.common.store

import java.util.concurrent.*

/**
 * Thread-safe cache for Java resources.
 */
internal open class ResourceCache {
  private val cache = ConcurrentHashMap<String, String>()

  open fun get(resourceName: String): String = cache.getOrPut(resourceName) {
    try {
      return@getOrPut this::class.java.getResourceAsStream(resourceName)
          .bufferedReader().use { it.readText() }
    } catch (e: IllegalStateException) {
      throw IllegalStateException("Could not load $resourceName", e)
    }
  }
}
