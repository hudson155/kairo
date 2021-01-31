package io.limberapp.common.auth.auth

import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.auth.jwt.JwtUser
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AuthUserTest {
  @Test
  fun `No JWT`() {
    val requiredUserGuid = UUID.randomUUID()
    val result = AuthUser(requiredUserGuid).authorize(null)
    assertFalse(result)
  }

  @Test
  fun `JWT has no user`() {
    val requiredUserGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthUser(requiredUserGuid).authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `User GUID mismatch`() {
    val requiredUserGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = null,
        user = JwtUser(guid = UUID.randomUUID(), firstName = null, lastName = null),
    )
    val result = AuthUser(requiredUserGuid).authorize(jwt)
    assertFalse(result)
  }

  @Test
  fun `User GUID match (happy path)`() {
    val requiredUserGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = null,
        user = JwtUser(guid = requiredUserGuid, firstName = null, lastName = null),
    )
    val result = AuthUser(requiredUserGuid).authorize(jwt)
    assertTrue(result)
  }

  @Test
  fun `JWT superuser override (happy path)`() {
    val requiredUserGuid = UUID.randomUUID()
    val jwt = Jwt(
        permissions = LimberPermissions(setOf(LimberPermission.SUPERUSER)),
        org = null,
        features = null,
        user = null,
    )
    val result = AuthUser(requiredUserGuid).authorize(jwt)
    assertTrue(result)
  }
}
