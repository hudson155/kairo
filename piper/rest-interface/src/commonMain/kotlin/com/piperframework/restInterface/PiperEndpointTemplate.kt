package com.piperframework.restInterface

/**
 * Configuration template for an API endpoint, uniquely represented by its HTTP method and path template.
 */
class PiperEndpointTemplate(val httpMethod: HttpMethod, val pathTemplate: String)
