package io.limberapp.common.auth.jwt

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.common.permissions.Permissions
import io.limberapp.common.permissions.PermissionsSerializer
import io.limberapp.common.permissions.featurePermissions.FeaturePermissions
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.permissions.limberPermissions.LimberPermissionsDeserializer
import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import io.limberapp.common.permissions.orgPermissions.OrgPermissionsDeserializer
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

internal class JwtTest {
  private val objectMapper = jacksonObjectMapper()
      .registerModule(SimpleModule()
          .addSerializer(Permissions::class.java, PermissionsSerializer())
          .addDeserializer(LimberPermissions::class.java, LimberPermissionsDeserializer())
          .addDeserializer(OrgPermissions::class.java, OrgPermissionsDeserializer())
          .addDeserializer(FeaturePermissions::class.java, TestFeaturePermissionsDeserializer()))

  private val orgGuid = UUID.randomUUID()
  private val featureAGuid = UUID.randomUUID()
  private val featureBGuid = UUID.randomUUID()
  private val userGuid = UUID.randomUUID()

  private val emptyJwtString = "{" +
      "\"https://limberapp.io/permissions\":\"2.0\"," +
      "\"https://limberapp.io/org\":null," +
      "\"https://limberapp.io/features\":null," +
      "\"https://limberapp.io/user\":null" +
      "}"

  private val fullJwtString = "{" +
      "\"https://limberapp.io/permissions\":\"2.8\"," +
      "\"https://limberapp.io/org\":{" +
      "\"guid\":\"$orgGuid\"," +
      "\"name\":\"Test org name\"," +
      "\"isOwner\":true," +
      "\"permissions\":\"4.6\"" +
      "}," +
      "\"https://limberapp.io/features\":{" +
      "\"$featureAGuid\":{" +
      "\"permissions\":\"A.2.4\"" +
      "}," +
      "\"$featureBGuid\":{" +
      "\"permissions\":\"B.2.8\"" +
      "}" +
      "}," +
      "\"https://limberapp.io/user\":{" +
      "\"guid\":\"$userGuid\"," +
      "\"firstName\":\"Jeff\"," +
      "\"lastName\":\"Hudson\"" +
      "}" +
      "}"

  private val emptyJwt = Jwt(
      permissions = LimberPermissions.none(),
      org = null,
      features = null,
      user = null,
  )

  private val fullJwt = Jwt(
      permissions = LimberPermissions.fromBitString("1"),
      org = JwtOrg(
          guid = orgGuid,
          name = "Test org name",
          isOwner = true,
          permissions = OrgPermissions.fromBitString("0110"),
      ),
      features = mapOf(
          featureAGuid to JwtFeature(TestFeaturePermissionsA.fromBitString("A01")),
          featureBGuid to JwtFeature(TestFeaturePermissionsB.fromBitString("B1")),
      ),
      user = JwtUser(
          guid = userGuid,
          firstName = "Jeff",
          lastName = "Hudson",
      ),
  )

  @Test
  fun `serialize - empty`() {
    assertEquals(emptyJwtString, objectMapper.writeValueAsString(emptyJwt))
  }

  @Test
  fun `serialize - full`() {
    assertEquals(fullJwtString, objectMapper.writeValueAsString(fullJwt))
  }

  @Test
  fun `deserialize - empty`() {
    assertEquals(emptyJwt, objectMapper.readValue(emptyJwtString))
  }

  @Test
  fun `deserialize - full`() {
    assertEquals(fullJwt, objectMapper.readValue(fullJwtString))
  }
}
