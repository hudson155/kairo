package com.piperframework.mongo

import org.litote.kmongo.property.KCollectionSimplePropertyPath
import org.litote.kmongo.property.KPropertyPath
import kotlin.reflect.KProperty1

/**
 * Using this instead of the built-in one due to the fact that the built-in one does not support nullable collection
 * properties, and therefore does not support deep paths.
 *
 * See https://github.com/Litote/kmongo/pull/166.
 */
fun <T> KProperty1<out Any?, Iterable<T>?>.filteredPosOp(identifier: String): KPropertyPath<Any?, T?> =
    KCollectionSimplePropertyPath<Any?, T>(null, this).filteredPosOp(identifier)
