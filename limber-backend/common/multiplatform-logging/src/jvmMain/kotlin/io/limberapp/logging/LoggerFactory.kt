package io.limberapp.logging

import kotlin.reflect.KClass
import org.slf4j.LoggerFactory as Slf4jLoggerFactory

actual object LoggerFactory {
  actual fun getLogger(kClass: KClass<*>): Logger = Slf4jLoggerFactory.getLogger(kClass.java)
}
