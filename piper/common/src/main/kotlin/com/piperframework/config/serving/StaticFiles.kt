package com.piperframework.config.serving

data class StaticFiles(
    val serve: Boolean,
    val rootPath: String? = null
) {

    init {
        if (serve) require(rootPath != null)
    }
}
