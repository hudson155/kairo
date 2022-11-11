package limber.auth

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

@JsonDeserialize(using = PlatformPermission.Deserializer::class)
public enum class PlatformPermission(internal val value: String) {
  FeatureRead("feature:read"),
  FeatureCreate("feature:create"),
  FeatureUpdate("feature:update"),
  FeatureDelete("feature:delete"),
  OrganizationRead("organization:read"),
  OrganizationCreate("organization:create"),
  OrganizationUpdate("organization:update"),
  OrganizationAuthSet("organizationAuth:set"),
  OrganizationAuthDelete("organizationAuth:delete"),
  OrganizationHostnameRead("organizationHostname:read"),
  OrganizationHostnameCreate("organizationHostname:create"),
  OrganizationHostnameDelete("organizationHostname:delete");

  /**
   * Uses [PlatformPermission]'s [value] field to deserialize.
   *
   * Gracefully fails by returning null if the permission is unknown.
   */
  internal class Deserializer : StdDeserializer<PlatformPermission>(PlatformPermission::class.java) {
    private val byValue = PlatformPermission.values().associateBy { it.value }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): PlatformPermission? {
      val string = p.readValueAs(String::class.java) ?: return null
      return byValue[string]
    }
  }
}
