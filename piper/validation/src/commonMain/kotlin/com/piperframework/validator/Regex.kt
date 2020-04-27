package com.piperframework.validator

/**
 * Important Note: When building a Regex object by referencing other Regex options, it's important to use regex.pattern
 * instead of just regex, since when Regex objects are compiled to JS they have "/g" added to the end, which screws up
 * concatenation.
 */
internal object Regex {
    val auth0ClientId = Regex("[A-Za-z0-9]{32}")

    val base64EncodedUuid = Regex("[A-Za-z0-9+/]{21}[AQgw]==")

    val hostname = run {
        val portion = Regex("[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])")
        return@run Regex("${portion.pattern}?(?:\\.${portion.pattern}?)+")
    }

    private val hex = Regex("[A-Fa-f0-9]")

    val darb = Regex("[0-9]+\\.${hex.pattern}*")

    val emailAddress = Regex("[A-Za-z0-9.!#\$%&'*+/=?^_`{|}~-]+@${hostname.pattern}")

    private val urlChar = Regex("[A-Za-z0-9\\-_.]")

    val path = Regex("(?:/${urlChar.pattern}+)*/?")

    val url = run {
        val protocol = Regex("[Hh][Tt][Tt][Pp][Ss]?://")
        val queryString =
            Regex("(?:\\?${urlChar.pattern}+=${urlChar.pattern}*(?:&${urlChar.pattern}+=${urlChar.pattern}*)*)?")
        val hash = Regex("(?:#${urlChar.pattern}*)?")
        return@run Regex(listOf(protocol, hostname, path, queryString, hash).joinToString(""))
    }

    val uuid = Regex("${hex.pattern}{8}-?${hex.pattern}{4}-?${hex.pattern}{4}-?${hex.pattern}{4}-?${hex.pattern}{12}")
}
