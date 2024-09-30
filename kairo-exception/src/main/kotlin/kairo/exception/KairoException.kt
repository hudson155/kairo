package kairo.exception

import io.ktor.http.HttpStatusCode

/**
 * Represents exceptions that Kairo knows how to handle.
 * This is primarily used within a REST context.
 */
public sealed class KairoException(
  override val message: String?,
  override val cause: Exception?,
) : Exception(message, cause) {
  public abstract val statusCode: HttpStatusCode

  public open val response: Map<String, Any>
    get() = buildMap {
      put("type", this@KairoException::class.simpleName!!)
      message?.let { put("message", it) }
    }
}
