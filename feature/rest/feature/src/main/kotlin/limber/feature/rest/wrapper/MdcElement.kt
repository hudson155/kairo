package limber.feature.rest.wrapper

import io.ktor.http.HttpMethod
import kotlinx.coroutines.ThreadContextElement
import org.slf4j.MDC
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.coroutines.CoroutineContext

/**
 * This format matches the Log4J2 format.
 */
private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

/**
 * [ThreadContextElement] is used for MDC
 * so that values don't escape the coroutine beyond its time on any thread.
 */
internal class MdcElement(mdc: Map<String, Any>) : ThreadContextElement<Map<String, String>> {
  override val key: CoroutineContext.Key<*> get() = Key

  private val mdc: Map<String, String> = mdc.mapValues { (_, value) ->
    when (value) {
      is HttpMethod -> value.value
      is String -> value
      is UUID -> value.toString()
      is ZonedDateTime -> dateTimeFormatter.format(value.withZoneSameInstant(ZoneId.systemDefault()))
      else -> error("Unsupported MDC type: ${value::class.simpleName}")
    }
  }

  override fun updateThreadContext(context: CoroutineContext): Map<String, String> {
    val oldState = MDC.getCopyOfContextMap().orEmpty()
    MDC.setContextMap(oldState + mdc)
    return oldState
  }

  override fun restoreThreadContext(context: CoroutineContext, oldState: Map<String, String>) {
    MDC.setContextMap(oldState)
  }

  companion object Key : CoroutineContext.Key<MdcElement>
}
