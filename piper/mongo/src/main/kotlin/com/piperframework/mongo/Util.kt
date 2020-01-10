package com.piperframework.mongo

import org.litote.kmongo.property.KCollectionSimplePropertyPath
import org.litote.kmongo.property.KPropertyPath
import kotlin.reflect.KProperty1

fun <T> KProperty1<out Any?, Iterable<T>?>.filteredPosOp(identifier: String): KPropertyPath<Any?, T?> =
    KCollectionSimplePropertyPath<Any?, T>(null, this).filteredPosOp(identifier)
