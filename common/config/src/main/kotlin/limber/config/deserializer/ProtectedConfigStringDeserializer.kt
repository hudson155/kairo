package limber.config.deserializer

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import limber.type.ProtectedString
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

@Suppress("UseIfInsteadOfWhen")
public class ProtectedConfigStringDeserializer : StdNodeBasedDeserializer<ProtectedString>(
  ProtectedString::class.java,
) {
  override fun convert(root: JsonNode?, ctxt: DeserializationContext): ProtectedString? {
    logger.info { "Deserializing protected config string..." }
    root ?: return null
    val result = from(root) ?: error("Could not deserialize protected config string.")
    return result.value
  }

  public companion object {
    internal fun from(json: JsonNode): ConfigResult<ProtectedString>? {
      when (json) {
        is ObjectNode -> fromObject(json)?.let { return@from it }
      }
      logger.debug { "Falling back to ConfigStringDeserializer." }
      return ConfigStringDeserializer.from(json)?.map { value -> value?.let { ProtectedString(it) } }
    }

    private fun fromObject(json: ObjectNode): ConfigResult<ProtectedString>? =
      when (json["type"].textValue()) {
        "GcpSecret" -> fromGcpSecret(json)
        "Command" -> fromCommand(json)
        else -> null
      }

    private fun fromGcpSecret(json: ObjectNode): ConfigResult<ProtectedString> {
      val id = requireNotNull(json["id"]?.textValue())
      logger.info { "Config string is from GCP secret. Accessing secret with ID $id." }
      val value = GcpSecretSource[id]
      logger.info { "Retrieved config string from GCP secret. Not logging due to sensitivity." }
      return ConfigResult(ProtectedString(value))
    }

    private fun fromCommand(json: ObjectNode): ConfigResult<ProtectedString> {
      val command = requireNotNull(json["command"]?.textValue())
      logger.info { "Config string is from command. Running command \"$command\"." }
      val value = CommandSource[command]
      logger.info { "Retrieved config string from command. Not logging due to possible sensitivity." }
      return ConfigResult(ProtectedString(value))
    }
  }
}
