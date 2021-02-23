package io.limberapp.auth.auth

import io.limberapp.auth.jwt.Jwt
import io.limberapp.permissions.limber.LimberPermission
import io.limberapp.permissions.limber.LimberPermissions
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AuthLimberPermissionTest {
  @Test
  fun `No JWT`() {
    val requiredOrgGuid = UUID.randomUUID()
    val result = AuthOrgMember(requiredOrgGuid)
        .authorize(null)
    assertFalse(result)
  }

  @Test
  fun `Missing Limber permission`() {
    val jwt = Jwt(
        permissions = LimberPermissions.fromBitString("01"),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthLimberPermission(permission = LimberPermission.IDENTITY_PROVIDER)
        .authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `Has Limber permission (happy path)`() {
    val jwt = Jwt(
        permissions = LimberPermissions.fromBitString("11"),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthLimberPermission(permission = LimberPermission.IDENTITY_PROVIDER)
        .authorize(jwt)
    assertTrue(result)
  }
}
