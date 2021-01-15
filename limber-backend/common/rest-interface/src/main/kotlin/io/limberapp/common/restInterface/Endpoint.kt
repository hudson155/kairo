package io.limberapp.common.restInterface

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.limberapp.common.rep.ValidatedRep
import io.limberapp.common.util.url.href

abstract class Endpoint(
    val httpMethod: HttpMethod,
    val path: String,
    val queryParams: List<Pair<String, String>> = emptyList(),
    val contentType: ContentType = ContentType.Application.Json,
    val body: ValidatedRep? = null,
) {
  val href = href(path, queryParams)
}
