package io.limberapp.common.util.string

/**
 * Joins human names together to form their full name.
 */
fun fullName(vararg names: String): String = names.joinToString(" ")
