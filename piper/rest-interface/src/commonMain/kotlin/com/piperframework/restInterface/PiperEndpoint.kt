package com.piperframework.restInterface

import com.piperframework.rep.ValidatedRep
import com.piperframework.util.href

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
  val href = href(path, queryParams)
}
