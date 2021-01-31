package io.limberapp.common.auth

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthAllTest {
  @Test
  fun test() {
    assertFalse(Auth.All().authorize(null))
    assertFalse(Auth.All(Auth.Deny, Auth.Deny, Auth.Deny).authorize(null))
    assertFalse(Auth.All(Auth.Deny, Auth.Allow).authorize(null))
    assertTrue(Auth.All(Auth.Allow, Auth.Allow, Auth.Allow).authorize(null))
  }
}
