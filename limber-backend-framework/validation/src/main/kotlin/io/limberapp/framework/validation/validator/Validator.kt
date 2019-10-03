package io.limberapp.framework.validation.validator

/**
 * This object contains methods to validate primitive inputs.
 */
object Validator {

    private object Pattern {
        const val hex = "[a-f0-9]"
        const val guid = "$hex{8}-?$hex{4}-?$hex{4}-?$hex{4}-?$hex{12}"
    }

    private object Regex {
        val guid = Regex(Validator.Pattern.guid, RegexOption.IGNORE_CASE)
    }

    fun guid(value: String) = Validator.Regex.guid.matches(value)
}
