package limber.auth

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

/**
 * Represents permissions that apply across the platform
 * (rather than to a particular Organization or Feature).
 */
@Suppress("EnumEntryName", "EnumEntryNameCase", "EnumNaming")
@JsonDeserialize(using = PlatformPermission.Deserializer::class)
public enum class PlatformPermission {
  Organization_List,
  Organization_Create,
  ;

  @JsonValue
  internal val value: String = permissionKey(this)

  /**
   * Uses [PlatformPermission]'s [value] field to deserialize.
   *
   * Gracefully fails by returning null if the permission is unknown.
   */
  internal class Deserializer : StdDeserializer<PlatformPermission>(PlatformPermission::class.java) {
    private val byValue = entries.associateBy { it.value }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): PlatformPermission? {
      val string = p.readValueAs(String::class.java) ?: return null
      return byValue[string]
    }
  }
}

public fun permissionKey(permission: Enum<*>): String {
  val name = permission.name
  val match = Regex("(?<entity>([A-Z][a-z]+)+)_(?<operation>([A-Z][a-z]+)+)").matchEntire(name)
  requireNotNull(match) { "$name doesn't match the required permission format." }
  val entity = checkNotNull(match.groups["entity"]).value
  val operation = checkNotNull(match.groups["operation"]).value
  return buildString {
    append(entity[0].lowercase() + entity.substring(1))
    append(":")
    append(operation[0].lowercase() + operation.substring(1))
  }
}
