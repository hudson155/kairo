package io.limberapp.framework.config.serving

/**
 * This class encapsulates configuration regarding which resources to serve and where to serve them.
 */
data class StaticFiles(
    val serve: Boolean,
    val rootPath: String?
) {

    init {
        if (serve) require(rootPath != null)
    }
}
