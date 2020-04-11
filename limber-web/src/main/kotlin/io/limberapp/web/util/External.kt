package io.limberapp.web.util

interface Process {

    interface Env {
        val API_ROOT_URL: String
    }

    val env: Env
}

external val process: Process

internal external fun encodeURIComponent(uriComponent: String): String
