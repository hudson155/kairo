package limber.feature.rest.wrapper

import io.ktor.http.HttpMethod
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * This format matches the Log4J2 format.
 */
private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

internal suspend fun withMdc(mdc: Map<String, Any>, block: suspend () -> Unit) {
  val contextMap = MDC.getCopyOfContextMap() + mdc.mapValues { (_, value) ->
    when (value) {
      is HttpMethod -> value.value
      is String -> value
      is UUID -> value.toString()
      is ZonedDateTime -> dateTimeFormatter.format(value.withZoneSameInstant(ZoneId.systemDefault()))
      else -> error("Unsupported MDC type: ${value::class.simpleName}")
    }
  }

  withContext(MDCContext(contextMap)) {
    block()
  }
}
