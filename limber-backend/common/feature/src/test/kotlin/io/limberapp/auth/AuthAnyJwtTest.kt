package io.limberapp.auth

import io.limberapp.auth.jwt.Jwt
import io.limberapp.permissions.limber.LimberPermissions
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthAnyJwtTest {
  @Test
  fun `No JWT`() {
    val result = Auth.AnyJwt.authorize(null)
    assertFalse(result)
  }

  @Test
  fun `Random JWT (happy path)`() {
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = null,
        user = null,
    )
    val result = Auth.AnyJwt.authorize(jwt)
    assertTrue(result)
  }
}