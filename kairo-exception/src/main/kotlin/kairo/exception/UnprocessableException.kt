package kairo.exception

import io.ktor.http.HttpStatusCode

public class UnprocessableException internal constructor(
  private val e: NotFoundException,
) : KairoException(e.message, e.cause) {
  override val statusCode: HttpStatusCode = HttpStatusCode.UnprocessableEntity

  override val response: Map<String, Any>
    get() = e.response
}

public fun unprocessable(e: NotFoundException): UnprocessableException =
  UnprocessableException(e)
