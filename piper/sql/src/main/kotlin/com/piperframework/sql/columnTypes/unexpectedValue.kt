package com.piperframework.sql.columnTypes

internal fun unexpectedValue(value: Any): Nothing = error("Unexpected value: $value of ${value::class.qualifiedName}")
