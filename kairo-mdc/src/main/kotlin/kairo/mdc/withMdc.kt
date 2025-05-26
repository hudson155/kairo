package kairo.mdc

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.jsonMapper
import kairo.serialization.util.writeValueAsStringSpecial
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC

private val mdcMapper: JsonMapper = jsonMapper().build()

/**
 * Use this to make MDC work properly with Kotlin coroutines.
 * Null values will be excluded.
 */
public suspend fun <T> withMdc(mdc: Map<String, Any?>, block: suspend () -> T): T {
  val contextMap = buildMap {
    putAll(MDC.getCopyOfContextMap())
    mdc.forEach { (key, value) ->
      value ?: return@forEach // Don't include null values in MDC.
      put(key, mdcMapper.writeValueAsStringSpecial(value))
    }
  }

  return withContext(MDCContext(contextMap)) {
    return@withContext block()
  }
}
