package io.limberapp.logging

import kotlin.reflect.KClass

@Suppress("UnusedPrivateMember") // TODO: https://github.com/detekt/detekt/issues/2636.
expect object LoggerFactory {
  fun getLogger(kClass: KClass<*>): Logger
}
