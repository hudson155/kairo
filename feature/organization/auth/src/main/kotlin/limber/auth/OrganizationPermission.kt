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
@Suppress("EnumEntryName", "EnumEntryNameCase", "EnumNaming")
@JsonDeserialize(using = OrganizationPermission.Deserializer::class)
public enum class OrganizationPermission {
  Feature_Read,
  Feature_List,
  Feature_Create,
  Feature_Update,
  Feature_Delete,

  Organization_Read,
  Organization_Update,
  Organization_Delete,

  OrganizationAuth_Create,
  OrganizationAuth_Update,
  OrganizationAuth_Delete,

  OrganizationHostname_Read,
  OrganizationHostname_List,
  OrganizationHostname_Create,
  OrganizationHostname_Delete,
  ;

  @JsonValue
  internal val value: String = permissionKey(this)

  /**
   * Uses [OrganizationPermission]'s [value] field to deserialize.
   *
   * Gracefully fails by returning null if the permission is unknown.
   */
  internal class Deserializer : StdDeserializer<OrganizationPermission>(OrganizationPermission::class.java) {
    private val byValue = entries.associateBy { it.value }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): OrganizationPermission? {
      val string = p.readValueAs(String::class.java) ?: return null
      return byValue[string]
    }
  }
}
