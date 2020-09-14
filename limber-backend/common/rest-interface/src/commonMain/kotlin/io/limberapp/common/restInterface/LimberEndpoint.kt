package io.limberapp.common.restInterface

import io.limberapp.common.rep.ValidatedRep
import io.limberapp.common.util.href

/**
 * Instances represent parameterized requests to an HTTP endpoint.
 */
@Suppress("UnnecessaryAbstractClass")
abstract class LimberEndpoint(
  val httpMethod: HttpMethod,
  val path: String,
  val queryParams: List<Pair<String, String>> = emptyList(),
  val contentType: ContentType = ContentType.JSON,
  val body: ValidatedRep? = null,
) {
  val href = href(path, queryParams)
}
