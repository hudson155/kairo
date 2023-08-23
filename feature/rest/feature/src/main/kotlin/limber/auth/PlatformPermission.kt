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
@JsonDeserialize(using = PlatformPermission.Deserializer::class)
public enum class PlatformPermission(@JsonValue internal val value: String) {
  OrganizationList("organization:list"),
  OrganizationCreate("organization:create"),
  OrganizationDelete("organization:delete"),
  OrganizationAuthCreate("organizationAuth:create"),
  OrganizationAuthUpdate("organizationAuth:update"),
  OrganizationAuthDelete("organizationAuth:delete"),
  OrganizationHostnameCreate("organizationHostname:create"),
  OrganizationHostnameDelete("organizationHostname:delete"),
  ;

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
