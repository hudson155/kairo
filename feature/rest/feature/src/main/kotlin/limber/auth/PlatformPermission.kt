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
  Organization_Delete,
  OrganizationAuth_Create,
  OrganizationAuth_Update,
  OrganizationAuth_Delete,
  OrganizationHostname_Create,
  OrganizationHostname_Delete,
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
  val match = Regex("(([A-Z][a-z]+)+)_(([A-Z][a-z]+)+)").matchEntire(name)
  requireNotNull(match) { "$name doesn't match the required permission format." }
  return buildString {
    append(match.groupValues[1][0].lowercase())
    append(match.groupValues[1].substring(1))
    append(":")
    append(match.groupValues[3][0].lowercase())
    append(match.groupValues[3].substring(1))
  }
}
