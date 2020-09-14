@file:JvmName("UrlCommonKt")

package io.limberapp.common.util

import kotlin.jvm.JvmName

/**
 * Encodes a URL component.
 */
@Suppress("UnusedPrivateMember") // False positive - Detekt bug?
expect fun enc(value: Any): String

/**
 * Makes a string appropriate for URL usage.
 */
fun String.slugify() = enc(toLowerCase())

/**
 * Creates an href. The [path] and [queryParams] must be already encoded.
 */
@Suppress("UnusedPrivateMember") // False positive - Detekt bug?
fun href(path: String, queryParams: List<Pair<String, String>> = emptyList()): String {
  val queryString = queryParams.joinToString("&") { "${it.first}=${it.second}" }
  var href = path
  if (queryString.isNotEmpty()) href += "?$queryString"
  return href
}

/**
 * Replaces the last component of a path (where components are separated by slashes) with the given value.
 */
fun String.replaceLastPathComponentWith(value: String) =
  "${split('/').dropLastWhile { it.isEmpty() }.dropLast(1).joinToString("/")}/$value"
