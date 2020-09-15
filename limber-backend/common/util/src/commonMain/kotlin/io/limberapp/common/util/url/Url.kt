@file:JvmName("UrlCommonKt")

package io.limberapp.common.util.url

import kotlin.jvm.JvmName

/**
 * Encodes a URL component.
 */
expect fun enc(value: Any): String

/**
 * Makes a string appropriate for URL usage.
 */
fun String.slugify() = enc(toLowerCase())

/**
 * Creates an href. The [path] and [queryParams] must be already encoded.
 */
fun href(path: String, queryParams: List<Pair<String, String>> = emptyList()): String {
  val queryString = queryParams.joinToString("&") { "${it.first}=${it.second}" }
  var href = path
  if (queryString.isNotEmpty()) href += "?$queryString"
  return href
}
