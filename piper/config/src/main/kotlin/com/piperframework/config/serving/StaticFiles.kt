package com.piperframework.config.serving

/**
 * This class encapsulates configuration regarding serving static file resources.
 */
data class StaticFiles(
  val serve: Boolean,
  val rootPath: String? = null
) {
  init {
    if (serve) require(rootPath != null)
  }
}
