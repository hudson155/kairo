package kairo.exception

import io.ktor.http.HttpStatusCode

/**
 * Represents exceptions that Kairo knows how to handle.
 * This is primarily used within a REST context.
 */
public abstract class KairoException(override val message: String?) : Exception(message) {
  init {
    check(!this::class.simpleName!!.endsWith("Exception")) { "Exceptions should not end with \"Exception\"." }
  }

  public abstract val statusCode: HttpStatusCode

  public open val response: Map<String, Any>
    get() = buildMap {
      put("type", this::class.simpleName!!)
      message?.let { put("message", it) }
    }
}
