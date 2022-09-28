package limber.rest.wrapper

import kotlinx.coroutines.withContext
import org.slf4j.MDC

internal suspend fun withMdc(mdc: Map<String, Any>, block: suspend () -> Unit) {
  withContext(MdcElement(mdc)) {
    try {
      block()
    } finally {
      mdc.forEach { (key) -> MDC.remove(key) }
    }
  }
}
