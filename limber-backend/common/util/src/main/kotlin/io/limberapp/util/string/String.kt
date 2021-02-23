package io.limberapp.util.string

/**
 * Joins human names together to form their full name.
 */
fun fullName(vararg names: String): String = names.joinToString(" ")
