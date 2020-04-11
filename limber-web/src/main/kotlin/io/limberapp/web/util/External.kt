package io.limberapp.web.util

internal interface Process {

    interface Env {
        val API_ROOT_URL: String
        val COPYRIGHT_HOLDER: String
    }

    val env: Env
}

internal external val process: Process

internal external fun encodeURIComponent(uriComponent: String): String
