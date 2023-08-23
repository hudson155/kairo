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

public class ConfigStringDeserializer : StdNodeBasedDeserializer<String>(
  String::class.java,
) {
  override fun convert(root: JsonNode?, ctxt: DeserializationContext): String? {
    logger.info { "Deserializing config string..." }
    root ?: return null
    val result = from(root) ?: error("Could not deserialize config string.")
    return result.value
  }

  public companion object {
    internal fun from(json: JsonNode): ConfigResult<String>? =
      when (json) {
        is NullNode -> ConfigResult(null)
        is TextNode -> fromRaw(json)
        is ObjectNode -> fromObject(json)
        else -> null
      }

    private fun fromObject(json: ObjectNode): ConfigResult<String>? =
      when (json["type"].textValue()) {
        "EnvironmentVariable" -> fromEnvironmentVariable(json)
        else -> null
      }

    private fun fromRaw(json: TextNode): ConfigResult<String> {
      logger.info { "Config value is from raw." }
      val value = json.textValue()
      logger.info { "Config value is from raw. Value is $value." }
      return ConfigResult(value)
    }

    private fun fromEnvironmentVariable(json: ObjectNode): ConfigResult<String> {
      val name = requireNotNull(json["name"]?.textValue())
      logger.info {
        "Config value is from environment variable." +
          " Accessing environment variable with name $name."
      }
      val value = EnvironmentVariableSource[name]
      logger.info {
        "Retrieved config value from environment variable." +
          " Not logging due to possible sensitivity."
      }
      return ConfigResult(value)
    }
  }
}
