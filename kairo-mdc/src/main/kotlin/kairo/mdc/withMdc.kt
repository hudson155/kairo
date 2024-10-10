package kairo.mdc

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC

private val mdcMapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

/**
 * Use this to make MDC work properly with Kotlin coroutines.
 * Null values will be excluded.
 */
public suspend fun withMdc(mdc: Map<String, Any?>, block: suspend () -> Unit) {
  val contextMap = buildMap {
    putAll(MDC.getCopyOfContextMap())
    mdc.forEach { (key, value) ->
      value ?: return@forEach // Don't include null values in MDC.
      put(key, mdcMapper.writeValueAsString(value))
    }
  }

  withContext(MDCContext(contextMap)) {
    block()
  }
}
