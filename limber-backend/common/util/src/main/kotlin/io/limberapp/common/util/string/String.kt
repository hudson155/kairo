package io.limberapp.common.util.string

/**
 * Joins human names together to form their full name.
 */
fun fullName(names: List<String>) = names.joinToString(" ").ifEmpty { null }
