package kairo.rest.exception

import com.fasterxml.jackson.core.JsonLocation
import com.fasterxml.jackson.databind.JsonMappingException
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.exception.BadRequestException

private val logger: KLogger = KotlinLogging.logger {}

public abstract class JsonBadRequestException(
  message: String,
  cause: Exception,
) : BadRequestException(message, cause) {
  public data class Location(
    val line: Int,
    val column: Int,
  )

  public open val path: String? = null

  public open val location: Location? = null

  override val response: Map<String, Any>
    get() = super.response + buildMap {
      path?.let { put("path", it) }
      location?.let { put("location", it) }
    }

  public companion object {
    public fun metadata(
      cause: JsonMappingException,
    ): Pair<String?, Location?> {
      val path = parsePath(cause.path)?.ifEmpty { null }
      val location = cause.location?.let { parseLocation(it) }
      return Pair(path, location)
    }

    private fun parsePath(path: List<JsonMappingException.Reference>): String? {
      try {
        return path.joinToString("") { reference ->
          buildString {
            when {
              reference.isNamed() -> append(".${reference.fieldName}")
              reference.isNumbered() -> append("[${reference.index}]")
              else -> error("Unsupported reference: $reference.")
            }
          }
        }.drop(1)
      } catch (e: Exception) {
        logger.error(e) { "Failed to derive path." }
        return null
      }
    }

    private fun JsonMappingException.Reference.isNamed(): Boolean =
      fieldName != null && index < 0

    private fun JsonMappingException.Reference.isNumbered(): Boolean =
      fieldName == null && index >= 0

    private fun parseLocation(location: JsonLocation): Location =
      Location(
        line = location.lineNr,
        column = location.columnNr,
      )
  }
}
