package io.limberapp.framework.validation.validator

/**
 * This object contains methods to validate primitive inputs.
 */
object Validator {

    private object Pattern {
        const val hex = "[a-f0-9]"
        const val uuid = "$hex{8}-?$hex{4}-?$hex{4}-?$hex{4}-?$hex{12}"
    }

    private object Regex {
        val uuid = Regex(Validator.Pattern.uuid, RegexOption.IGNORE_CASE)
    }

    fun uuid(value: String) = Validator.Regex.uuid.matches(value)
}
