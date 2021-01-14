package io.limberapp.common.logging

import kotlin.reflect.KClass

actual object LoggerFactory {
  actual fun getLogger(kClass: KClass<*>): Logger = org.slf4j.LoggerFactory.getLogger(kClass.java)
}
