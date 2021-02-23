package io.limberapp.logging

import kotlin.reflect.KClass

@Suppress("UnusedPrivateMember") // TODO: https://github.com/detekt/detekt/issues/2636.
actual object LoggerFactory {
  actual fun getLogger(kClass: KClass<*>): Logger = LoggerImpl
}
