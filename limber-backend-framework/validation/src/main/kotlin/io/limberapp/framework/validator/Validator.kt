package io.limberapp.framework.validator

/**
 * This object contains methods to validate primitive inputs.
 */
object Validator {

    private object Regex {

        val hex = Regex("[a-f0-9]", RegexOption.IGNORE_CASE)

        val uuid = Regex("$hex{8}-?$hex{4}-?$hex{4}-?$hex{4}-?$hex{12}", RegexOption.IGNORE_CASE)

        val hostname = Regex(
            "[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?(?:\\.[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?)+",
            RegexOption.IGNORE_CASE
        )

        val url = run {

            val validChar = Regex("[a-z0-9\\-_.]", RegexOption.IGNORE_CASE)

            val protocol = Regex("https?://", RegexOption.IGNORE_CASE)
            val path = "(?:/$validChar+)*/?"
            val queryString = Regex(
                pattern = "(?:\\?$validChar+=$validChar*(?:&$validChar+=$validChar*)*)?",
                option = RegexOption.IGNORE_CASE
            )
            val hash = Regex("(?:#$validChar*)?")

            return@run Regex(
                pattern = listOf(protocol, hostname, path, queryString, hash)
                    .joinToString(""),
                option = RegexOption.IGNORE_CASE
            )
        }

        val emailAddress = Regex(
            "[a-z0-9.!#\$%&'*+/=?^_`{|}~-]+@$hostname",
            RegexOption.IGNORE_CASE
        )
    }

    fun emailAddress(value: String) = Regex.emailAddress.matches(value)

    /**
     * This URL validator is definitely not perfect, but there really is no such thing as a perfect
     * URL validator - just one that fits the use case.
     */
    fun url(value: String) = Regex.url.matches(value)

    fun uuid(value: String) = Regex.uuid.matches(value)
}
