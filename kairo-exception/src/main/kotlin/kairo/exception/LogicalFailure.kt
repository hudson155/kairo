package kairo.exception

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

/**
 * "Logical failures" describe situations not deemed successful in your domain,
 * but still within the realms of that domain.
 * For example, a user record not being found is a logical failure, not a real exception.
 * Whereas a network timeout or stack overflow is a "real exception".
 *
 * Roughly conforms to RFC 9457, but not strictly.
 */
public abstract class LogicalFailure(
  override val message: String,
  cause: Throwable? = null,
) : Exception(message, cause) {
  public abstract val type: String
  public abstract val status: HttpStatusCode
  public open val detail: String? = null

  public open val json: JsonElement by lazy {
    buildJsonObject {
      put("type", JsonPrimitive(type))
      put("status", JsonPrimitive(status.value))
      put("message", JsonPrimitive(message))
      put("detail", JsonPrimitive(detail))
      buildJson()
    }
  }

  protected open fun JsonObjectBuilder.buildJson(): Unit =
    Unit
}
