package io.limberapp.common.restInterface

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod

/**
 * Configuration template for an API endpoint. The type parameter [E] is used for type safety in
 * concrete [EndpointHandler] classes; it's not actually necessary as part of this class.
 */
data class EndpointTemplate<E : Endpoint>(
    val httpMethod: HttpMethod,
    val pathTemplate: String,
    val requiredQueryParams: Set<String>,
    val contentType: ContentType,
)
