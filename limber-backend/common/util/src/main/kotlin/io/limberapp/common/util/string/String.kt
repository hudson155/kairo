package io.limberapp.common.util.string

fun List<String>.joinNames() = joinToString(" ").ifEmpty { null }
