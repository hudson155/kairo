package com.piperframework.validator

internal object Regex {

    val auth0ClientId = Regex("[a-z0-9]{22}", RegexOption.IGNORE_CASE)

    val base64EncodedUuid = Regex("[A-Za-z0-9+/]{21}[AQgw]==")

    val hostname = Regex(
        pattern = "[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?(?:\\.[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?)+",
        option = RegexOption.IGNORE_CASE
    )

    val emailAddress = Regex(
        pattern = "[a-z0-9.!#\$%&'*+/=?^_`{|}~-]+@$hostname",
        option = RegexOption.IGNORE_CASE
    )

    private val urlChar = Regex("[a-z0-9\\-_.]", RegexOption.IGNORE_CASE)

    val path = Regex("(?:/$urlChar+)*/?", RegexOption.IGNORE_CASE)

    val url = run {
        val protocol = Regex("https?://", RegexOption.IGNORE_CASE)
        val queryString = Regex(
            pattern = "(?:\\?$urlChar+=$urlChar*(?:&$urlChar+=$urlChar*)*)?",
            option = RegexOption.IGNORE_CASE
        )
        val hash = Regex("(?:#$urlChar*)?", RegexOption.IGNORE_CASE)
        return@run Regex(
            pattern = listOf(protocol, hostname, path, queryString, hash).joinToString(""),
            option = RegexOption.IGNORE_CASE
        )
    }

    private val hex = Regex("[a-f0-9]", RegexOption.IGNORE_CASE)

    val uuid = Regex("$hex{8}-?$hex{4}-?$hex{4}-?$hex{4}-?$hex{12}", RegexOption.IGNORE_CASE)
}
