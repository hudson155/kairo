package io.limberapp.auth.auth

import io.limberapp.auth.jwt.Jwt
import io.limberapp.auth.jwt.JwtOrg
import io.limberapp.permissions.limber.LimberPermission
import io.limberapp.permissions.limber.LimberPermissions
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.permissions.org.OrgPermissions
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AuthOrgMemberTest {
  @Test
  fun `No JWT`() {
    val requiredOrgGuid = UUID.randomUUID()
    val result = AuthOrgMember(requiredOrgGuid)
        .authorize(null)
    assertFalse(result)
  }

  @Test
  fun `JWT has no org`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid)
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Org GUID mismatch`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = UUID.randomUUID(),
            name = "",
            isOwner = false,
            permissions = OrgPermissions.none(),
        ),
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid)
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Org GUID match (happy path)`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = requiredOrgGuid,
            name = "",
            isOwner = false,
            permissions = OrgPermissions.none(),
        ),
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid)
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `Missing org permission`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = requiredOrgGuid,
            name = "",
            isOwner = false,
            permissions = OrgPermissions.fromBitString("1101"),
        ),
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid, permission = OrgPermission.MANAGE_ORG_METADATA)
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Has org permission with null guid (happy path)`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = requiredOrgGuid,
            name = "",
            isOwner = false,
            permissions = OrgPermissions.fromBitString("0010"),
        ),
        features = null,
        user = null,
    )
    val result = AuthOrgMember(null, permission = OrgPermission.MANAGE_ORG_METADATA)
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `Has org permission with correct guid (happy path)`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = requiredOrgGuid,
            name = "",
            isOwner = false,
            permissions = OrgPermissions.fromBitString("0010"),
        ),
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid, permission = OrgPermission.MANAGE_ORG_METADATA)
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `Is not org owner`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = requiredOrgGuid,
            name = "",
            isOwner = false,
            permissions = OrgPermissions.none(),
        ),
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid, isOwner = true)
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Is org owner (happy path)`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = requiredOrgGuid,
            name = "",
            isOwner = true,
            permissions = OrgPermissions.none(),
        ),
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid, isOwner = true)
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `JWT superuser override - org permission (happy path)`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions(setOf(LimberPermission.SUPERUSER)),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid, permission = OrgPermission.MANAGE_ORG_METADATA)
        .authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `JWT superuser override - org owner (happy path)`() {
    val requiredOrgGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions(setOf(LimberPermission.SUPERUSER)),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthOrgMember(requiredOrgGuid, isOwner = true)
        .authorize(jwt)
    assertTrue(result)
  }
}
