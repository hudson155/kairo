package com.piperframework.validator

/**
 * This object contains methods to validate primitive inputs.
 */
object Validator {

    private object Regex {

        val hex = Regex("[a-f0-9]", RegexOption.IGNORE_CASE)

        val uuid = Regex("$hex{8}-?$hex{4}-?$hex{4}-?$hex{4}-?$hex{12}", RegexOption.IGNORE_CASE)

        val hostname = Regex(
            pattern = "[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?(?:\\.[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?)+",
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

        val emailAddress = Regex(
            pattern = "[a-z0-9.!#\$%&'*+/=?^_`{|}~-]+@$hostname",
            option = RegexOption.IGNORE_CASE
        )
    }

    fun emailAddress(value: String) = Regex.emailAddress.matches(value)

    fun path(value: String) = Regex.path.matches(value)

    /**
     * This URL validator is definitely not perfect, but there really is no such thing as a perfect URL validator - just
     * one that fits the use case.
     */
    fun url(value: String) = Regex.url.matches(value)

    fun uuid(value: String) = Regex.uuid.matches(value)
}
