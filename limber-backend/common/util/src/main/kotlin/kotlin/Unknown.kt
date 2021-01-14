package kotlin

import kotlin.reflect.KClass

/**
 * Intended to be thrown when an unknown subtype is encountered. Sealed classes protect against this
 * sort of thing, but sealed classes are not always possible.
 */
fun <T : Any, U : T> unknownType(abstractType: KClass<T>, concreteType: KClass<U>): Nothing =
    error("${concreteType.simpleName} is an unknown type of ${abstractType.simpleName}")
