package io.limberapp.framework.endpoint

import io.ktor.http.HttpMethod

/**
 * The configuration for the API endpoint. Uniquely represents the HTTP method and path
 * template.
 */
data class EndpointConfig(val httpMethod: HttpMethod, val pathTemplate: String) {

    fun path(pathParams: Map<String, String>, queryParams: Map<String, String>): String {
        var path = pathTemplate.replace(Regex("\\{([a-z]+)}", RegexOption.IGNORE_CASE)) {
            val pathParam = it.groupValues[1]
            return@replace checkNotNull(pathParams[pathParam])
        }
        if (queryParams.isNotEmpty()) {
            path = "$path?${queryParams.map { "${it.key}=${it.value}" }.joinToString("&")}"
        }
        return path
    }
}
