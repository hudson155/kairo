package limber.auth

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

/**
 * Represents permissions that apply within an Organization,
 * but not to a particular Feature.
 */
@JsonDeserialize(using = OrganizationPermission.Deserializer::class)
public enum class OrganizationPermission(@JsonValue internal val value: String) {
  FeatureRead("feature:read"),
  FeatureCreate("feature:create"),
  FeatureUpdate("feature:update"),
  FeatureDelete("feature:delete"),
  OrganizationRead("organization:read"),
  OrganizationUpdate("organization:update"),
  OrganizationAuthSet("organizationAuth:set"),
  OrganizationAuthDelete("organizationAuth:delete"),
  OrganizationHostnameRead("organizationHostname:read"),
  OrganizationHostnameCreate("organizationHostname:create"),
  OrganizationHostnameDelete("organizationHostname:delete"),
  ;

  /**
   * Uses [OrganizationPermission]'s [value] field to deserialize.
   *
   * Gracefully fails by returning null if the permission is unknown.
   */
  internal class Deserializer : StdDeserializer<OrganizationPermission>(OrganizationPermission::class.java) {
    private val byValue = OrganizationPermission.values().associateBy { it.value }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): OrganizationPermission? {
      val string = p.readValueAs(String::class.java) ?: return null
      return byValue[string]
    }
  }
}
