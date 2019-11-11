package io.limberapp.framework.validation.validator

/**
 * This object contains methods to validate primitive inputs.
 */
object Validator {

    private object Regex {
        val hex = Regex("[a-f0-9]", RegexOption.IGNORE_CASE)
        val uuid = Regex("$hex{8}-?$hex{4}-?$hex{4}-?$hex{4}-?$hex{12}", RegexOption.IGNORE_CASE)
    }

    fun uuid(value: String) = Regex.uuid.matches(value)
}
