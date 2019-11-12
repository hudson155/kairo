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
        val emailAddress = Regex(
            "[a-z0-9.!#\$%&'*+/=?^_`{|}~-]+@$hostname",
            RegexOption.IGNORE_CASE
        )
    }

    fun emailAddress(value: String) = Regex.emailAddress.matches(value)

    fun uuid(value: String) = Regex.uuid.matches(value)
}
