package kairo.exception

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

/**
 * "Logical failures" describe situations not deemed successful in your domain
 * but still within the realms of that domain.
 * For example, a library book not being found is a logical failure, not a real exception.
 */
public abstract class LogicalFailure : Exception() {
  public abstract val type: String
  public abstract val status: HttpStatusCode
  public abstract val title: String
  public open val detail: String? = null

  public open val json: JsonElement by lazy {
    buildJsonObject {
      put("type", JsonPrimitive(type))
      put("status", JsonPrimitive(status.value))
      put("title", JsonPrimitive(title))
      put("detail", JsonPrimitive(detail))
      buildJson()
    }
  }

  protected open fun JsonObjectBuilder.buildJson(): Unit =
    Unit
}
