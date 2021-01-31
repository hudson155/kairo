package io.limberapp.common.auth

import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class AuthAllowTest {
  @Test
  fun `No JWT (happy path)`() {
    val result = Auth.Allow.authorize(null)
    assertTrue(result)
  }

  @Test
  fun `Random JWT (happy path)`() {
    val jwt = Jwt(
        permissions = LimberPermissions.none(),
        org = null,
        features = null,
        user = null,
    )
    val result = Auth.Allow.authorize(jwt)
    assertTrue(result)
  }
}
