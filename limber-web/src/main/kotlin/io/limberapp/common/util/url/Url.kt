package io.limberapp.common.util.url

external fun encodeURIComponent(uriComponent: String): String

/**
 * Encodes a URL component.
 */
fun enc(value: Any): String = encodeURIComponent(value.toString()).replace("%20", "+")

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
