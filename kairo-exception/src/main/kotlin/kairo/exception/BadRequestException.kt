package kairo.exception

import io.ktor.http.HttpStatusCode

public open class BadRequestException protected constructor(
  message: String?,
) : KairoException(message) {
  final override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest

  public constructor() : this(null)
}
