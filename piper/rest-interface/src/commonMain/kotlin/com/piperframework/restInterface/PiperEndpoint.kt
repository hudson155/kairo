package com.piperframework.restInterface

import com.piperframework.endpoint.command.AbstractCommand
import com.piperframework.rep.ValidatedRep

/**
 * Instances represent parameterized requests to an HTTP endpoint.
 */
abstract class PiperEndpoint(
    val httpMethod: HttpMethod,
    val path: String,
    val queryParams: List<Pair<String, String>> = emptyList(),
    val body: ValidatedRep? = null
) : AbstractCommand() {

    val href = run {
        val queryString = queryParams.joinToString("&") { "${it.first}=${it.second}" }
        var href = path
        if (queryString.isNotEmpty()) href += "?$queryString"
        return@run href
    }
}
