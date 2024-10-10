package kairo.mdc

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext

private val mdcMapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

public suspend fun withMdc(mdc: Map<String, Any?>, block: suspend () -> Unit) {
  val contextMap = buildMap {
    putAll(MDC.getCopyOfContextMap())
    mdc.forEach { (key, value) ->
      value ?: return@forEach // Don't include null values in MDC.
      put(key, mdcMapper.convertValue<String>(value))
    }
  }

  withContext(MDCContext(contextMap)) {
    block()
  }
}
