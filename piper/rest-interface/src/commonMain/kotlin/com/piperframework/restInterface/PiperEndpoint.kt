package com.piperframework.restInterface

import com.piperframework.rep.ValidatedRep

/**
 * Instances represent parameterized requests to an HTTP endpoint.
 */
@Suppress("UnnecessaryAbstractClass")
abstract class PiperEndpoint(
    val httpMethod: HttpMethod,
    val path: String,
    val queryParams: List<Pair<String, String>> = emptyList(),
    val body: ValidatedRep? = null
) {

    val href = run {
        val queryString = queryParams.joinToString("&") { "${it.first}=${it.second}" }
        var href = path
        if (queryString.isNotEmpty()) href += "?$queryString"
        return@run href
    }
}
