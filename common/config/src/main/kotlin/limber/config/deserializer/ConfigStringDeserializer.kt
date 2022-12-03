package limber.config.deserializer

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

@Suppress("UseIfInsteadOfWhen")
public class ConfigStringDeserializer : StdNodeBasedDeserializer<String>(
  String::class.java,
) {
  internal data class Result<out T : Any>(val value: T?) {
    fun <R : Any> map(transform: (value: T?) -> R?): Result<R> = Result(transform(value))
  }

  override fun convert(root: JsonNode?, ctxt: DeserializationContext): String? {
    logger.info { "Deserializing config string..." }
    root ?: return null
    val result = from(root) ?: error("Could not deserialize config string.")
    return result.value
  }

  public companion object {
    internal fun from(json: JsonNode): Result<String>? =
      when (json) {
        is NullNode -> Result(null)
        is TextNode -> fromPlaintext(json)
        is ObjectNode -> fromObject(json)
        else -> null
      }

    private fun fromObject(json: ObjectNode): Result<String>? =
      when (json["type"].textValue()) {
        "EnvironmentVariable" -> fromEnvironmentVariable(json)
        else -> null
      }

    private fun fromPlaintext(json: TextNode): Result<String> {
      logger.info { "Config string is from plaintext." }
      val value = json.textValue()
      logger.info { "Config string is from plaintext. Value is $value." }
      return Result(value)
    }

    private fun fromEnvironmentVariable(json: ObjectNode): Result<String> {
      val name = requireNotNull(json["name"]?.textValue())
      logger.info { "Config string is from environment variable. Accessing variable with name $name." }
      val value = EnvironmentVariableSource[name]
      logger.info { "Retrieved config string from environment variable. Not logging due to possible sensitivity." }
      return Result(value)
    }
  }
}
