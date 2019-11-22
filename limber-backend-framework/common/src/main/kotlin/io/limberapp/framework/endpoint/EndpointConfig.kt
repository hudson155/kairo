package io.limberapp.framework.endpoint

import io.ktor.http.HttpMethod
import io.ktor.http.encodeURLParameter

/**
 * The configuration for the API endpoint. Uniquely represents the HTTP method and path template.
 */
data class EndpointConfig(val httpMethod: HttpMethod, val pathTemplate: String) {

    fun path(pathParams: Map<String, String>, queryParams: Map<String, String>): String {

        var path = pathTemplate.replace(Regex("\\{([a-z]+)}", RegexOption.IGNORE_CASE)) {
            val pathParam = it.groupValues[1]
            return@replace checkNotNull(pathParams[pathParam]).encodeURLParameter(spaceToPlus = true)
        }

        if (queryParams.isNotEmpty()) {
            path = "$path?${queryParams.map {
                val encodedKey = it.key.encodeURLParameter(spaceToPlus = true)
                val encodedValue = it.value.encodeURLParameter(spaceToPlus = true)
                return@map "$encodedKey=$encodedValue"
            }.joinToString("&")}"
        }

        return path
    }
}
