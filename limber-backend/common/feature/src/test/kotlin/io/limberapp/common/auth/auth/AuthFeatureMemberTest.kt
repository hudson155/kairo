package io.limberapp.common.auth.auth

import io.limberapp.backend.permissions.featurePermissions.TestFeaturePermissionA
import io.limberapp.backend.permissions.featurePermissions.TestFeaturePermissionB
import io.limberapp.backend.permissions.featurePermissions.TestFeaturePermissionsA
import io.limberapp.backend.permissions.featurePermissions.TestFeaturePermissionsB
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.auth.jwt.JwtFeature
import io.limberapp.common.auth.jwt.JwtOrg
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.permissions.orgPermissions.OrgPermission
import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AuthFeatureMemberTest {
  @Test
  fun `No JWT`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val result = AuthFeatureMember(requiredFeatureGuid)
        .authorize(null)
    assertFalse(result)
  }

  @Test
  fun `JWT has no features`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthFeatureMember(requiredFeatureGuid)
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Feature GUID mismatch`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = mapOf(
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsA.none()),
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsB.none()),
        ),
        user = null,
    )
    val result = AuthFeatureMember(requiredFeatureGuid)
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Feature GUID match (happy path)`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = mapOf(
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsA.none()),
            requiredFeatureGuid to JwtFeature(TestFeaturePermissionsB.none()),
        ),
        user = null,
    )
    val result = AuthFeatureMember(requiredFeatureGuid)
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `JWT has no org`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = mapOf(
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsA.none()),
            requiredFeatureGuid to JwtFeature(TestFeaturePermissionsB.none()),
        ),
        user = null,
    )
    val result = AuthFeatureMember(
        featureGuid = requiredFeatureGuid,
        permission = OrgPermission.MANAGE_ORG_METADATA,
    )
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Missing org permission`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = requiredFeatureGuid,
            name = "",
            isOwner = false,
            permissions = OrgPermissions.fromBitString("1101"),
        ),
        features = mapOf(
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsA.none()),
            requiredFeatureGuid to JwtFeature(TestFeaturePermissionsB.none()),
        ),
        user = null,
    )
    val result = AuthFeatureMember(
        featureGuid = requiredFeatureGuid,
        permission = OrgPermission.MANAGE_ORG_METADATA,
    )
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Has org permission (happy path)`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = requiredFeatureGuid,
            name = "",
            isOwner = false,
            permissions = OrgPermissions.fromBitString("0010"),
        ),
        features = mapOf(
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsA.none()),
            requiredFeatureGuid to JwtFeature(TestFeaturePermissionsB.none()),
        ),
        user = null,
    )
    val result = AuthFeatureMember(
        featureGuid = requiredFeatureGuid,
        permission = OrgPermission.MANAGE_ORG_METADATA,
    )
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `Missing feature permission`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = mapOf(
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsA.none()),
            requiredFeatureGuid to JwtFeature(TestFeaturePermissionsB.fromBitString("10")),
        ),
        user = null,
    )
    val result = AuthFeatureMember(
        featureGuid = requiredFeatureGuid,
        permission = TestFeaturePermissionB.TEST_FEATURE_PERMISSION_B2,
    )
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Feature permission for wrong feature`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = mapOf(
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsA.none()),
            requiredFeatureGuid to JwtFeature(TestFeaturePermissionsB.fromBitString("11")),
        ),
        user = null,
    )
    val result = AuthFeatureMember(
        featureGuid = requiredFeatureGuid,
        permission = TestFeaturePermissionA.TEST_FEATURE_PERMISSION_A2,
    )
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Has feature permission (happy path)`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = mapOf(
            UUID.randomUUID() to JwtFeature(TestFeaturePermissionsA.none()),
            requiredFeatureGuid to JwtFeature(TestFeaturePermissionsB.fromBitString("01")),
        ),
        user = null,
    )
    val result = AuthFeatureMember(
        featureGuid = requiredFeatureGuid,
        permission = TestFeaturePermissionB.TEST_FEATURE_PERMISSION_B2,
    )
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `JWT superuser override - org permission (happy path)`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions(setOf(LimberPermission.SUPERUSER)),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthFeatureMember(
        featureGuid = requiredFeatureGuid,
        permission = OrgPermission.MANAGE_ORG_METADATA,
    )
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `JWT superuser override - feature permission (happy path)`() {
    val requiredFeatureGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions(setOf(LimberPermission.SUPERUSER)),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthFeatureMember(
        featureGuid = requiredFeatureGuid,
        permission = TestFeaturePermissionB.TEST_FEATURE_PERMISSION_B2,
    )
        .authorize(jwt)
    assertTrue(result)
  }
}
