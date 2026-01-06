package kairo.exception

import io.ktor.http.HttpStatusCode

/**
 * "Logical failures" describe situations not deemed successful in your domain,
 * but still within the realms of that domain.
 * For example, a user record not being found is a logical failure, not a real exception.
 * Whereas a network timeout or stack overflow is a "real exception".
 *
 * Roughly conforms to RFC 9457, but not strictly.
 */
public abstract class LogicalFailure(
  message: String,
  cause: Throwable? = null,
) : Exception(message, cause) {
  public abstract val type: String
  public abstract val status: HttpStatusCode
  public open val detail: String? = null

  public open val json: Map<String, Any?> by lazy {
    buildMap {
      put("type", type)
      put("status", status)
      put("message", message)
      put("detail", detail)
      buildJson()
    }
  }

  protected open fun MutableMap<String, Any?>.buildJson(): Unit =
    Unit
}
