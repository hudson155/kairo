package io.limberapp.common.util.url

import java.net.URLEncoder
import java.text.Normalizer

/**
 * Encodes a URL component.
 */
fun enc(value: Any): String = URLEncoder.encode(value.toString(), "UTF-8")

/**
 * Makes a string appropriate for URL usage. There are no checks on string length. An empty string
 * may be returned.
 */
fun slugify(string: String) = Normalizer.normalize(string, Normalizer.Form.NFD)
    .replace(Regex("[^A-Za-z0-9\\s\\-.@]+"), "")
    .replace(Regex("[\\s\\-.@]+"), "-")
    .trim('-')
    .toLowerCase()

/**
 * Creates an href. The [path] and [queryParams] must be already encoded.
 */
fun href(path: String, queryParams: List<Pair<String, String>> = emptyList()): String {
  val queryString = queryParams.joinToString("&") { "${it.first}=${it.second}" }
  var href = path
  if (queryString.isNotEmpty()) href += "?$queryString"
  return href
}
