package io.limberapp.auth

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthConditionalTest {
  @Test
  fun test() {
    assertFalse(Auth.Conditional(on = false, ifTrue = Auth.Allow, Auth.Deny).authorize(null))
    assertTrue(Auth.Conditional(on = true, ifTrue = Auth.Allow, Auth.Deny).authorize(null))
    assertTrue(Auth.Conditional(on = false, ifTrue = Auth.Deny, Auth.Allow).authorize(null))
    assertFalse(Auth.Conditional(on = true, ifTrue = Auth.Deny, Auth.Allow).authorize(null))
  }
}
