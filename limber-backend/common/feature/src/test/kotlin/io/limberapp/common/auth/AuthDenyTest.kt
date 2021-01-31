package io.limberapp.common.auth

import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.auth.jwt.JwtOrg
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFalse

class AuthDenyTest {
  @Test
  fun `No JWT`() {
    val result = Auth.Deny.authorize(null)
    assertFalse(result)
  }

  @Test
  fun `Random JWT`() {
    val jwt = Jwt(
        permissions = LimberPermissions.all(),
        org = JwtOrg(
            guid = UUID.randomUUID(),
            name = "Random Inc.",
            isOwner = true,
            permissions = OrgPermissions.all(),
        ),
        features = null,
        user = null,
    )
    val result = Auth.Deny.authorize(jwt)
    assertFalse(result)
  }
}
