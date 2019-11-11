package io.limberapp.framework.config.serving

/**
 * This class encapsulates configuration regarding which resources to serve and where to serve them.
 */
data class ServingConfig(
    val apiPathPrefix: String,
    val staticFiles: StaticFiles
)
