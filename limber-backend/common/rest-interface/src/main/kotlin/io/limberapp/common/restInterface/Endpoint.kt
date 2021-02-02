package io.limberapp.common.restInterface

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.encodeURLPath
import io.ktor.http.formUrlEncode
import io.limberapp.common.rep.ValidatedRep

open class Endpoint(
    val httpMethod: HttpMethod,
    rawPath: String,
    qp: List<Pair<String, String>> = emptyList(),
    val contentType: ContentType = ContentType.Application.Json,
    val body: ValidatedRep? = null,
) {
  val path: String = rawPath.encodeURLPath()
  val href: String = run {
    val queryString = qp.formUrlEncode()
    var href = path
    if (queryString.isNotEmpty()) href += "?$queryString"
    return@run href
  }
}
