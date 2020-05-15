package com.piperframework.store

import java.util.concurrent.ConcurrentHashMap

/**
 * Thread-safe cache for Java resources.
 */
internal class ResourceCache {
  private val cache = ConcurrentHashMap<String, String>()

  fun get(resourceName: String): String = cache.getOrPut(resourceName) {
    try {
      return@getOrPut this::class.java.getResourceAsStream(resourceName)
          .bufferedReader().use { it.readText() }
    } catch (e: IllegalStateException) {
      throw IllegalStateException("Could not load $resourceName", e)
    }
  }
}
