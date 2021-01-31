package io.limberapp.common.auth.jwt

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.common.permissions.featurePermissions.TestFeaturePermissionsA
import io.limberapp.common.permissions.featurePermissions.TestFeaturePermissionsB
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import io.limberapp.common.serialization.LimberObjectMapper
import io.limberapp.common.typeConversion.typeConverter.FeaturePermissionsTypeConverter
import io.limberapp.common.typeConversion.typeConverter.LimberPermissionsTypeConverter
import io.limberapp.common.typeConversion.typeConverter.OrgPermissionsTypeConverter
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class JwtTest {
  private val objectMapper: LimberObjectMapper = LimberObjectMapper(typeConverters = listOf(
      LimberPermissionsTypeConverter,
      OrgPermissionsTypeConverter,
      FeaturePermissionsTypeConverter(mapOf(
          'A' to TestFeaturePermissionsA,
          'B' to TestFeaturePermissionsB,
      )),
  ))

  private val orgGuid: UUID = UUID.randomUUID()
  private val featureAGuid: UUID = UUID.randomUUID()
  private val featureBGuid: UUID = UUID.randomUUID()
  private val userGuid: UUID = UUID.randomUUID()

  private val emptyJwtString: String = "{" +
      "\"https://limberapp.io/permissions\":\"2.0\"," +
      "\"https://limberapp.io/org\":null," +
      "\"https://limberapp.io/features\":null," +
      "\"https://limberapp.io/user\":null" +
      "}"

  private val fullJwtString: String = "{" +
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

  private val emptyJwt: Jwt = Jwt(
      permissions = LimberPermissions.none(),
      org = null,
      features = null,
      user = null,
  )

  private val fullJwt: Jwt = Jwt(
      permissions = LimberPermissions.fromBitString("1"),
      org = JwtOrg(
          guid = orgGuid,
          name = "Test org name",
          isOwner = true,
          permissions = OrgPermissions.fromBitString("0110"),
      ),
      features = mapOf(
          featureAGuid to JwtFeature(TestFeaturePermissionsA.fromBitString("01")),
          featureBGuid to JwtFeature(TestFeaturePermissionsB.fromBitString("1")),
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
