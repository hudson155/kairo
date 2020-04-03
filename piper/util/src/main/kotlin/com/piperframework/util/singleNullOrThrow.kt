package com.piperframework.util

fun <T> Iterable<T>.singleNullOrThrow(): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    val single = iterator.next()
    require(!iterator.hasNext()) { "Collection contains more than one matching element." }
    return single
}
