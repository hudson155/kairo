package io.limberapp.common.restInterface

import io.ktor.http.HttpMethod

/**
 * Configuration template for an API endpoint, uniquely represented by its HTTP method, path template, and the names of
 * the required query parameters.
 */
data class LimberEndpointTemplate(
  val httpMethod: HttpMethod,
  val pathTemplate: String,
  val requiredQueryParams: Set<String>,
  val contentType: ContentType,
)
