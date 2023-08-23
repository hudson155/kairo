package limber.config.deserializer

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

public class ConfigIntegerDeserializer : StdNodeBasedDeserializer<Int>(
  Int::class.java,
) {
  override fun convert(root: JsonNode?, ctxt: DeserializationContext): Int? {
    logger.info { "Deserializing config integer..." }
    root ?: return null
    val result = from(root) ?: error("Could not deserialize config integer.")
    return result.value
  }

  public companion object {
    internal fun from(json: JsonNode): ConfigResult<Int>? =
      when (json) {
        is NullNode -> ConfigResult(null)
        is IntNode -> fromRaw(json)
        is ObjectNode -> fromObject(json)
        else -> null
      }

    private fun fromObject(json: ObjectNode): ConfigResult<Int>? =
      when (json["type"].textValue()) {
        "EnvironmentVariable" -> fromEnvironmentVariable(json)
        else -> null
      }

    private fun fromRaw(json: IntNode): ConfigResult<Int> {
      logger.info { "Config value is from raw." }
      val value = json.intValue()
      logger.info { "Config value is from raw. Value is $value." }
      return ConfigResult(value)
    }

    private fun fromEnvironmentVariable(json: ObjectNode): ConfigResult<Int> {
      val name = requireNotNull(json["name"]?.textValue())
      logger.info { "Config value is from environment variable. Accessing variable with name $name." }
      val value = EnvironmentVariableSource[name]?.toInt()
      logger.info { "Retrieved config value from environment variable. Not logging due to possible sensitivity." }
      return ConfigResult(value)
    }
  }
}
