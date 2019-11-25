package io.limberapp.framework.config.serving

data class StaticFiles(
    val serve: Boolean,
    val rootPath: String? = null
) {

    init {
        if (serve) require(rootPath != null)
    }
}
