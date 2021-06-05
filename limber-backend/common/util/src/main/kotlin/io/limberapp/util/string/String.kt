package io.limberapp.util.string

fun String.initials(): String = split(' ').map { it.first() }.joinToString("")
