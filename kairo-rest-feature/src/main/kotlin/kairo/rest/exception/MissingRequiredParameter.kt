package kairo.rest.exception

import kairo.exception.BadRequestException

public class MissingRequiredParameter(
  private val path: String?,
) : BadRequestException("Missing required parameter.") {
  override val response: Map<String, Any>
    get() = super.response + buildMap {
      if (path != null) put("path", path)
    }
}
